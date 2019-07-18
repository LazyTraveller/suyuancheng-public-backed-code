package com.suyuancheng.backend.service


import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.domain.randomMarket
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
class MarketServiceTest {

    @Autowired
    private lateinit var marketService: MarketService

    @Autowired
    private lateinit var marketRepository: MarketRepository

    private lateinit var market1: Market

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
    }

    @Test
    fun list() {
        randomMarket().let(marketRepository::save)
        assertEquals(2, marketService.list().size)
    }
    @After
    fun clean(){
        marketRepository.deleteAll()
    }

    @Test
    fun query() {
        assertEquals(
            market1.uuid,marketRepository.findById(market1.uuid).get().uuid
        )
        assertEquals(
            1, marketService.query(
                market1.name, Pageable.unpaged()
            ).totalElements
        )
    }

    @Test
    fun create(){
        val market = randomMarket()
        val result = marketService.create(market.name, market.address, market.remark)
        assertEquals(true, marketRepository.findById(result.uuid).isPresent)
    }

    @Test
    fun update(){
        val market = randomMarket()
        marketService.update(market1.uuid, market.name, market.address, market.remark)
        assertEquals(market.remark, marketRepository.findById(market1.uuid).get().remark)
    }

    @Test
    fun delete(){
        marketService.delete(market1.uuid)
        assertEquals(false, marketRepository.findById(market1.uuid).isPresent)
    }
}
