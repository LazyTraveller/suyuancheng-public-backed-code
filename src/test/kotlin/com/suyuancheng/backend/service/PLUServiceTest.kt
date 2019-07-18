package com.suyuancheng.backend.service

import com.suyuancheng.backend.api.rest.PLUController
import com.suyuancheng.backend.dao.*
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import kotlin.math.roundToInt

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class PLUServiceTest {

    @Autowired
    private lateinit var pluRepository: PLURepository

    @Autowired
    private lateinit var pluService: PLUService

    @Autowired
    private lateinit var marketRepository: MarketRepository

    @Autowired
    private lateinit var goodsRepository: GoodsRepository

    @Autowired
    private lateinit var providerRepository: ProviderRepository

    @Autowired
    lateinit var suYuanRepository: SuYuanChengRepository

    @Autowired
    private lateinit var storeRepository: StoreRepository

    private lateinit var market1: Market
    private lateinit var store1: Store
    private lateinit var suYuanCheng1: SuYuanCheng
    private lateinit var goods1: Goods
    private lateinit var provider1: Provider
    private lateinit var plu1: PLU
    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        store1 = randomStore(market = market1).let(storeRepository::save)
        suYuanCheng1 = randomSuYuanCheng(store = store1).let(suYuanRepository::save)
        provider1 = randomProvider(store = store1).let(providerRepository::save)
        goods1 = randomGoods(provider = provider1).let(goodsRepository::save)
        plu1 = randomPLU(
            id = PLU.PLUId(suYuanCheng1.uuid, goods1.uuid),
            goods = goods1,
            suYuanCheng = suYuanCheng1
        ).let(pluRepository::save)
    }

    @After
    fun clean() {
        pluRepository.deleteAll()
        goodsRepository.deleteAll()
        providerRepository.deleteAll()
        suYuanRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()

    }

    @Test
    fun query() {
        assertEquals(
            plu1.price.roundToInt(),
            pluService.query(
                null,
                suYuanCheng1.uuid,
                plu1.code,
                Pageable.unpaged()
            ).content[0].price.roundToInt()
        )
    }

    @Test
    fun listBySuYuanCheng(){
        assertEquals(plu1.code, pluService.listBySuYuanCheng(suYuanCheng1.machineId)[0].code)
    }

    @Test
    fun findByIdSuYuanChengId() {
        assertEquals(1, pluRepository.findByIdSuYuanChengId(suYuanCheng1.uuid).size)

        assertEquals(1, pluRepository.findByIdGoodsId(goods1.uuid).size)
    }

    @Test
    fun create() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val suYuanCheng = randomSuYuanCheng(store = store).let(suYuanRepository::save)
        val provider = randomProvider(store = store).let(providerRepository::save)
        val goods = randomGoods(provider = provider).let(goodsRepository::save)
        val plu = randomPLU(
            id = PLU.PLUId(suYuanCheng.uuid, goods.uuid),
            goods = goods,
            suYuanCheng = suYuanCheng
        )
        pluService.create(
            arrayListOf(
                PLUController.PluDTO(
                    plu.suYuanCheng.uuid,
                    plu.goods.uuid,
                    plu.code,
                    plu.price
                )
            )
        )
        assertEquals(2, pluRepository.findAll().size)
    }

    @Test
    fun update() {
        val market = randomMarket().let(marketRepository::save)
        val store = randomStore(market = market).let(storeRepository::save)
        val suYuanCheng = randomSuYuanCheng(store = store).let(suYuanRepository::save)
        val provider = randomProvider(store = store).let(providerRepository::save)
        val goods = randomGoods(provider = provider).let(goodsRepository::save)
        val plu = randomPLU(
            id = PLU.PLUId(suYuanCheng.uuid, goods.uuid),
            goods = goods,
            suYuanCheng = suYuanCheng
        )
        pluService.update(goods1.uuid, suYuanCheng1.uuid, plu.code, plu.price)
        assertEquals(
            plu.price.roundToInt(),
            pluRepository.findById(plu1.id).get().price.roundToInt()
        )
    }

    @Test
    fun delete() {
        pluService.delete(plu1.id.suYuanChengId, plu1.id.goodsId)
        assertEquals(0, pluRepository.findAll().size)
    }
}