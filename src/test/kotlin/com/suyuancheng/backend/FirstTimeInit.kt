package com.suyuancheng.backend

import com.suyuancheng.backend.dao.*
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @author hsj
 */
@Component
class FirstTimeInit(
    private val marketRepository: MarketRepository,
    private val storeRepository: StoreRepository,
    private val goodsRepository: GoodsRepository,
    private val providerRepository: ProviderRepository,
    private val suYuanChengRepository: SuYuanChengRepository,
    private val pluRepository: PLURepository,
    private val marketAdminRepository: MarketAdminRepository,
    private val storeAdminRepository: StoreAdminRepository,
    private val tradingItemRepository: TradingItemRepository,
    private val tradingRepository: TradingRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        tradingItemRepository.deleteAll()
        tradingRepository.deleteAll()
        storeAdminRepository.deleteAll()
        marketAdminRepository.deleteAll()
        pluRepository.deleteAll()
        goodsRepository.deleteAll()
        providerRepository.deleteAll()
        suYuanChengRepository.deleteAll()
        storeRepository.deleteAll()
        marketRepository.deleteAll()
    }
}