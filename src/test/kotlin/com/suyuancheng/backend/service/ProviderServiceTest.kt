package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.dao.ProviderRepository
import com.suyuancheng.backend.dao.StoreRepository
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
class ProviderServiceTest {

    @Autowired
    private lateinit var providerRepository: ProviderRepository

    @Autowired
    private lateinit var storeRepository: StoreRepository

    @Autowired
    private lateinit var marketRepository: MarketRepository

    @Autowired
    private lateinit var providerService: ProviderService

    private lateinit var provider1: Provider
    private lateinit var store1: Store
    private lateinit var market1: Market

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        provider1 = randomProvider(store = store1).let(providerRepository::save)
    }

    @After
    fun clean() {
        providerRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun query() {
        assertEquals(
            provider1.uuid, providerRepository.findById(provider1.uuid).get().uuid
        )
        assertEquals(
            1, providerService.query(
                provider1.name, store1.uuid, Pageable.unpaged()
            ).totalElements
        )
    }

    @Test
    fun create() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val provider = randomProvider(store = store)
        val result = providerService.create(provider.name, store1.uuid, provider.remark)
        assertEquals(true, providerRepository.findById(result.uuid).isPresent)
    }

    @Test
    fun update() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val provider = randomProvider(store = store)
        providerService.update(provider1.uuid, provider.name, provider.remark)
        assertEquals(provider.remark, providerRepository.findById(provider1.uuid).get().remark)
    }

    @Test
    fun delete() {
        providerService.delete(provider1.uuid)
        assertEquals(false, providerRepository.findById(provider1.uuid).isPresent)
    }
}