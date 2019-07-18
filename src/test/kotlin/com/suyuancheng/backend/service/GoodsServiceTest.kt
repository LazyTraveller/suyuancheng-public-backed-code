package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.GoodsRepository
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
import java.time.LocalDate

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class GoodsServiceTest {

    @Autowired
    private lateinit var goodsService: GoodsService

    @Autowired
    private lateinit var storeRepository: StoreRepository

    @Autowired
    private lateinit var goodsRepository: GoodsRepository

    @Autowired
    private lateinit var providerRepository: ProviderRepository

    @Autowired
    private lateinit var marketRepository: MarketRepository

    private lateinit var goods1: Goods
    private lateinit var market1: Market
    private lateinit var store1: Store
    private lateinit var provider1: Provider

    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        provider1 = randomProvider(store = store1).let(providerRepository::save)
        goods1 = randomGoods(provider = provider1).let(goodsRepository::save)
    }

    @After
    fun clean() {
        goodsRepository.deleteAll()
        providerRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun query() {
        assertEquals(
            1,
            goodsService.query(
                null,
                null,
                goods1.sellOut,
                store1.uuid,
                goods1.purchaseDate.withYear(2010),
                goods1.purchaseDate.withYear(2022),
                Pageable.unpaged()
            ).totalElements
        )
    }

    @Test
    fun create() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val provider = randomProvider(store = store).let(providerRepository::save)
        val goods = randomGoods(provider = provider)
        val result = goodsService.create(
            goods.name,
            goods.fullName,
            goods.purchaseDate,
            goods.qty,
            goods.unit,
            provider.uuid,
            goods.remark
        )
        assertEquals(store.uuid, goodsRepository.findById(result.uuid).get().provider.store.uuid)
    }

    @Test
    fun update() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val provider = randomProvider(store = store).let(providerRepository::save)
        goodsService.update(
            goods1.uuid,
            goods1.name,
            goods1.fullName,
            LocalDate.now(),
            goods1.sellOut,
            goods1.qty,
            goods1.unit,
            provider.uuid,
            goods1.remark
        )
        assertEquals(
            store.remark,
            goodsRepository.findById(goods1.uuid).get().provider.store.remark
        )
    }

    @Test
    fun delete() {
        goodsService.delete(goods1.uuid)
        assertEquals(false, storeRepository.findById(goods1.uuid).isPresent)
    }
}