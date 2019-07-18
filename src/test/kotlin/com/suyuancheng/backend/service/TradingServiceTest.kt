package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.dao.SuYuanChengRepository
import com.suyuancheng.backend.dao.TradingRepository
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
class TradingServiceTest {

    @Autowired
    private lateinit var tradingRepository: TradingRepository

    @Autowired
    private lateinit var tradingService: TradingService

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

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        suYuanCheng1 = randomSuYuanCheng(store = store1).let(suYuanRepository::save)
        trading1 = randomTrading(suYuanCheng = suYuanCheng1).let(tradingRepository::save)
    }

    @After
    fun clean() {
        tradingRepository.deleteAll()
        suYuanRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun query() {
        assertEquals(
            1, tradingService.query(
                trading1.suYuanCheng.store.name, null, null, store1.uuid, null, null,
                Pageable.unpaged()
            ).totalElements
        )
    }

    @Test
    fun create() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val suYuanCheng = randomSuYuanCheng(store = store).let(suYuanRepository::save)
        val trading = randomTrading(suYuanCheng = suYuanCheng)

        val result = tradingService.create(
            trading.suYuanCheng.uuid,
            trading.sequence,
            trading.amount,
            trading.time
        )
        assertEquals(
            store.name,
            tradingRepository.findById(result.uuid).get().suYuanCheng.store.name
        )
    }
}