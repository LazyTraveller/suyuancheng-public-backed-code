package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.*
import com.suyuancheng.backend.domain.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class EChartsServiceTest {

    @Autowired
    private lateinit var goodsRepository: GoodsRepository

    @Autowired
    private lateinit var providerRepository: ProviderRepository

    @Autowired
    private lateinit var tradingRepository: TradingRepository

    @Autowired
    private lateinit var tradingItemRepository: TradingItemRepository

    @Autowired
    private lateinit var eChartsService: EChartsService

    @Autowired
    private lateinit var marketRepository: MarketRepository

    @Autowired
    lateinit var suYuanRepository: SuYuanChengRepository

    @Autowired
    private lateinit var storeRepository: StoreRepository

    private lateinit var market1: Market
    private val storeList: MutableList<Store> = mutableListOf()
    private val suYuanChengList: MutableList<SuYuanCheng> = mutableListOf()
    private val tradingList: MutableList<Trading> = mutableListOf()
    private val goodsList: MutableList<Goods> = mutableListOf()
    private val providerList: MutableList<Provider> = mutableListOf()


    @Before
    fun setup() {
        market1 = randomMarket().let(marketRepository::save)
        for (i in 0..20) {
            storeList.add(randomStore(market = market1).let(storeRepository::save))
        }
        for (i in 0..80) {
            suYuanChengList.add(randomSuYuanCheng(store = storeList[i % 21]).let(suYuanRepository::save))
        }
        for (i in 0..250) {
            tradingList.add(
                randomTrading(suYuanCheng = suYuanChengList[i % 81]).let(
                    tradingRepository::save
                )
            )
        }

        /*   for (i in 0..80) {
               providerList.add(randomProvider(store = storeList[i % 21]).let(providerRepository::save))
           }
           for (i in 0..150) {
               goodsList.add(randomGoods(provider = providerList[i % 81]).let(goodsRepository::save))
           }


           for (i in 0..1000) {
               randomTradingItem(
                   trading = tradingList[i % 251],
                   goods = goodsList[i % 151]
               ).let(tradingItemRepository::save)
           }*/

    }

    @After
    fun clean() {
        tradingItemRepository.deleteAll()
        goodsRepository.deleteAll()
        providerRepository.deleteAll()
        tradingRepository.deleteAll()
        suYuanRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }

    @Test
    fun storeSales() {
        println(
            "销售额:" +
                    eChartsService.storeSales(
                        storeList[15].uuid,
                        LocalDateTime.of(2005, 5, 12, 18, 6)
                        , LocalDateTime.of(2016, 5, 12, 18, 6)
                    )
        )
    }

    @Test
    fun marketSales() {
        println(
            "销售额:" +
                    eChartsService.marketSales(
                        market1.uuid,
                        LocalDateTime.of(2005, 5, 12, 18, 6)
                        , LocalDateTime.of(2016, 5, 12, 18, 6)
                    )
        )
    }

    /*@Test
    fun goodsSales() {
        println(
            "销售额:" +
                    eChartsService.goodsSales(
                        storeList[15].uuid,
                        LocalDateTime.of(2005, 5, 12, 18, 6)
                        , LocalDateTime.of(2016, 5, 12, 18, 6)
                    )
        )
    }*/
}