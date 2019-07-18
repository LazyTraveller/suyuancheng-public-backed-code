package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.dao.SuYuanChengRepository
import com.suyuancheng.backend.domain.*
import org.apache.commons.lang3.RandomStringUtils
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
class SuYuanChengServiceTest {

    @Autowired
    private lateinit var marketRepository: MarketRepository

    @Autowired
    private lateinit var suYuanChengService: SuYuanChengService

    @Autowired
    lateinit var suYuanRepository: SuYuanChengRepository

    @Autowired
    private lateinit var storeRepository: StoreRepository

    private lateinit var market1: Market
    private lateinit var store1: Store
    private lateinit var suYuanCheng1: SuYuanCheng

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        suYuanCheng1 = randomSuYuanCheng(store = store1).let(suYuanRepository::save)
    }

    @After
    fun clean() {
        suYuanRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun list() {
        assertEquals(
            1,
            suYuanRepository.query(
                suYuanCheng1.machineId,
                suYuanCheng1.store.uuid,
                Pageable.unpaged()
            ).totalElements
        )
    }

    @Test
    fun create() {
        suYuanChengService.create(RandomStringUtils.randomAlphanumeric(12), store1.uuid)
        assertEquals(2, suYuanRepository.findAll().size)
    }

    @Test
    fun update() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        suYuanChengService.update(
            suYuanCheng1.uuid,
            store.uuid,
            RandomStringUtils.randomAlphanumeric(12)
        )
        assertEquals(store.remark, suYuanRepository.findById(suYuanCheng1.uuid).get().store.remark)
    }

    @Test
    fun delete() {
        suYuanChengService.delete(suYuanCheng1.uuid)
        assertEquals(0, suYuanRepository.findAll().size)
    }
}