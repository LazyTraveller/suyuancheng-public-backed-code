package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketAdminRepository
import com.suyuancheng.backend.dao.MarketRepository
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
class MarketAdminServiceTest {

    @Autowired
    private lateinit var marketAdminRepository: MarketAdminRepository

    @Autowired
    private lateinit var marketAdminService: MarketAdminService
    @Autowired
    private lateinit var marketRepository: MarketRepository

    private lateinit var market1: Market
    lateinit var marketAdmin: MarketAdmin

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        marketAdmin = randomMarketAdmin(market = market1).let(marketAdminRepository::save)
    }

    @After
    fun clean() {
        marketAdminRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun query() {
        assertEquals(
            1,
            marketAdminService.query(marketAdmin.realName, null, Pageable.unpaged()).totalElements
        )
    }

    @Test
    fun update() {
        val roles = mutableSetOf(Role.MARKET_MANAGER)
        assertEquals(
            marketAdmin.realName,
            marketAdminRepository.findById(marketAdmin.id).get().realName
        )
        marketAdminService.update(marketAdmin.id, "hsj", roles)
        assertEquals("hsj", marketAdminRepository.findById(marketAdmin.id).get().realName)
    }

    @Test
    fun create() {
        var user = randomUser()
        user = marketAdminService.create(user.username, user.password, user.realName, market1.uuid)
        assertEquals(user.realName, marketAdminRepository.findById(user.id).get().realName)
    }

    @Test
    fun delete() {
        marketAdminService.delete(marketAdmin.id)
        assertEquals(0, marketAdminRepository.findAll().size)
    }
}