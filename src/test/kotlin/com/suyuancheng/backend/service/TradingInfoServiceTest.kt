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
class TradingInfoServiceTest {

    @Autowired
    private lateinit var goodsRepository: GoodsRepository

    @Autowired
    private lateinit var providerRepository: ProviderRepository

    @Autowired
    private lateinit var pluRepository: PLURepository

    @Autowired
    private lateinit var tradingRepository: TradingRepository

    @Autowired
    private lateinit var tradingInfoService: TradingInfoService

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
    private lateinit var plu1: PLU
    private lateinit var tradingItem1: TradingItem
    private lateinit var Main: TradingInfo.MainClass
    private lateinit var Detail: TradingInfo.DetailClass
    private lateinit var Payment: TradingInfo.PaymentClass
    private lateinit var tradingInfo: TradingInfo

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        suYuanCheng1 = randomSuYuanCheng(store = store1).let(suYuanRepository::save)
        trading1 = randomTrading(suYuanCheng = suYuanCheng1)
        provider1 = randomProvider(store = store1).let(providerRepository::save)
        goods1 = randomGoods(provider = provider1).let(goodsRepository::save)
        plu1 = randomPLU(
            id = PLU.PLUId(suYuanCheng1.uuid, goods1.uuid),
            goods = goods1,
            suYuanCheng = suYuanCheng1
        ).let(pluRepository::save)
        tradingItem1 = randomTradingItem(
            trading = trading1,
            goods = goods1
        )
        Main = randomeMain(TransNo = trading1.sequence, MachineId = suYuanCheng1.machineId)
        Detail = randomDetail(
            TransNo = trading1.sequence,
            ProductName = goods1.name,
            PLU = plu1.code
        )
        Payment = randomPayment(TransNo = trading1.sequence, Amount = 55.55f)
        tradingInfo = randomTradingInfo(Main, arrayListOf(Detail), arrayListOf(Payment))
    }

    @After
    fun clean() {
        pluRepository.deleteAll()
        tradingItemRepository.deleteAll()
        goodsRepository.deleteAll()
        providerRepository.deleteAll()
        tradingRepository.deleteAll()
        suYuanRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun push() {
        tradingInfoService.push(tradingInfo)
        assertEquals(
            1,
            tradingRepository.query(
                null, null, trading1.sequence, store1.uuid, null, null, Pageable.unpaged()
            ).totalElements
        )

        assertEquals(
            1,
            tradingItemRepository.findAll().size
        )
        println("plu-code:${pluRepository.findByIdGoodsId(tradingItemRepository.findAll()[0].goods.uuid)[0].code}")
    }

    @Test
    fun pull() {
        val market1 = randomMarket().let(marketRepository::save)
        val store1 = randomStore(market = market1).let(storeRepository::save)
        val suYuanCheng1 = randomSuYuanCheng(store = store1).let(suYuanRepository::save)
        val trading1 = randomTrading(suYuanCheng = suYuanCheng1).let(tradingRepository::save)
        val provider1 = randomProvider(store = store1).let(providerRepository::save)
        val goods1 = randomGoods(provider = provider1).let(goodsRepository::save)
        randomTradingItem(
            goods = goods1,
            trading = trading1
        ).let(tradingItemRepository::save)

        randomTradingItem(
            goods = goods1,
            trading = trading1
        ).let(tradingItemRepository::save)

        randomTradingItem(
            goods = goods1,
            trading = trading1
        ).let(tradingItemRepository::save)

        val reTradingInfo =
            tradingInfoService.pull(trading1.suYuanCheng.machineId, trading1.sequence)
        assertEquals(3, reTradingInfo.Detail.size)
        println(reTradingInfo.Detail)
        assertEquals(reTradingInfo.Main.StoreName, store1.name)
    }
}