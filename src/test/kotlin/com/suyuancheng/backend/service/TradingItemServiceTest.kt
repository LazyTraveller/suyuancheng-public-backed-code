package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.*
import com.suyuancheng.backend.domain.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TradingItemServiceTest {

    @Autowired
    private lateinit var goodsRepository: GoodsRepository

    @Autowired
    private lateinit var providerRepository: ProviderRepository


    @Autowired
    private lateinit var tradingRepository: TradingRepository

    @Autowired
    private lateinit var tradingItemService: TradingItemService

    @Autowired
    private lateinit var tradingItemRepository: TradingItemRepository

    @Autowired
    private lateinit var marketRepository: MarketRepository

    @Autowired
    lateinit var suYuanRepository: SuYuanChengRepository

    @Autowired
    private lateinit var storeRepository: StoreRepository

    private lateinit var market1: Market
    private lateinit var store1: Store
    private lateinit var suYuanCheng1: SuYuanCheng
    private lateinit var trading1: Trading
    private lateinit var goods1: Goods
    private lateinit var provider1: Provider
    private lateinit var tradingItem1: TradingItem

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        suYuanCheng1 = randomSuYuanCheng(store = store1).let(suYuanRepository::save)
        trading1 = randomTrading(suYuanCheng = suYuanCheng1).let(tradingRepository::save)
        provider1 = randomProvider(store = store1).let(providerRepository::save)
        goods1 = randomGoods(provider = provider1).let(goodsRepository::save)
        tradingItem1 = randomTradingItem(
            trading = trading1,
            goods = goods1
        ).let(tradingItemRepository::save)
    }

    @After
    fun clean() {
        tradingItemRepository.deleteAll()
        goodsRepository.deleteAll()
        providerRepository.deleteAll()
        tradingRepository.deleteAll()
        suYuanRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun query() {
        assertEquals(
            1, tradingItemService.query(
                tradingItem1.goods.name, trading1.uuid, Pageable.unpaged()
            ).totalElements
        )
    }

    @Test
    fun create() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val suYuanCheng = randomSuYuanCheng(store = store).let(suYuanRepository::save)
        val trading = randomTrading(suYuanCheng = suYuanCheng).let(tradingRepository::save)
        val provider = randomProvider(store = store).let(providerRepository::save)
        val goods = randomGoods(provider = provider).let(goodsRepository::save)
        val tradingItem =
            randomTradingItem(trading = trading, goods = goods).let(tradingItemRepository::save)

        val result = tradingItemService.create(
            tradingItem.trading.uuid,
            tradingItem.goods.uuid,
            tradingItem.weight,
            tradingItem.unit,
            tradingItem.price,
            tradingItem.amount
        )
        assertEquals(
            market.name,
            tradingItemRepository.findById(result.uuid).get().trading.suYuanCheng.store.market.name
        )
    }
}