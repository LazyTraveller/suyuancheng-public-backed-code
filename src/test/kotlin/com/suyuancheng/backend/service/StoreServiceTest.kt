package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.domain.Store
import com.suyuancheng.backend.domain.randomMarket
import com.suyuancheng.backend.domain.randomStore
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
class StoreServiceTest {

    @Autowired
    private lateinit var storeService: StoreService

    @Autowired
    private lateinit var storeRepository: StoreRepository

    @Autowired
    private lateinit var marketRepository: MarketRepository

    private lateinit var store1: Store
    private lateinit var market1: Market

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
    }

    @After
    fun clean() {
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun list() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)

        assertEquals(1, storeService.list(market).size)
        assertEquals(store.remark, storeService.list(market)[0].remark)
    }
    @Test
    fun query() {
        assertEquals(
            1,
            storeService.query(null, market1.uuid, Pageable.unpaged()).totalElements
        )
    }

    @Test
    fun create() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market)
        val result = storeService.create(store.name, market.uuid, market.remark)
        assertEquals(market.uuid, storeRepository.findById(result.uuid).get().market.uuid)
    }

    @Test
    fun update() {
        val market = randomMarket().let(marketRepository::save)
        storeService.update(store1.uuid, store1.name, market.uuid, store1.remark)
        assertEquals(market.remark, storeRepository.findById(store1.uuid).get().market.remark)
    }

    @Test
    fun delete() {
        storeService.delete(store1.uuid)
        assertEquals(false, storeRepository.findById(store1.uuid).isPresent)
    }
}