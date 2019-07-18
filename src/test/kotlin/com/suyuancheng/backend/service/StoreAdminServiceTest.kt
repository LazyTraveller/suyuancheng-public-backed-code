package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.dao.StoreAdminRepository
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
class StoreAdminServiceTest {


    @Autowired
    private lateinit var marketRepository: MarketRepository

    @Autowired
    private lateinit var storeAdminRepository: StoreAdminRepository

    @Autowired
    private lateinit var storeAdminService: StoreAdminService
    @Autowired
    private lateinit var storeRepository: StoreRepository

    private lateinit var store1: Store
    private lateinit var market1: Market
    lateinit var storeAdmin: StoreAdmin


    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        storeAdmin = randomStoreAdmins(store = store1).let(storeAdminRepository::save)
    }

    @After
    fun clean() {
        storeAdminRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun query() {
        assertEquals(
            1,
            storeAdminService.query(storeAdmin.realName, null, Pageable.unpaged()).totalElements
        )
    }

    @Test
    fun update() {
        val roles = mutableSetOf(Role.STORE_MANAGER)
        assertEquals(
            storeAdmin.realName,
            storeAdminRepository.findById(storeAdmin.id).get().realName
        )
        storeAdminService.update(storeAdmin.id, "hsj", roles)
        assertEquals("hsj", storeAdminRepository.findById(storeAdmin.id).get().realName)
    }

    @Test
    fun create() {
        var user = randomUser()
        user = storeAdminService.create(user.username, user.password, user.realName, store1.uuid)
        assertEquals(user.realName, storeAdminRepository.findById(user.id).get().realName)
    }

    @Test
    fun delete() {
        storeAdminService.delete(storeAdmin.id)
        assertEquals(0, storeAdminRepository.findAll().size)
    }
}