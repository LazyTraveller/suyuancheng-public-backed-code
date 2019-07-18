package com.suyuancheng.backend

import com.suyuancheng.backend.dao.*
import com.suyuancheng.backend.domain.*
import org.springframework.boot.CommandLineRunner
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Component
class FirstTimeInit(
    private val userRepository: UserRepository,
    private val marketRepository: MarketRepository,
    private val storeRepository: StoreRepository,
    private val goodsRepository: GoodsRepository,
    private val providerRepository: ProviderRepository,
    private val suYuanChengRepository: SuYuanChengRepository,
    private val pluRepository: PLURepository,
    private val marketAdminRepository: MarketAdminRepository,
    private val storeAdminRepository: StoreAdminRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (userRepository.findByUsername("admin") == null) {
            User(
                "admin",
                "{noop}admin",
                "Administrator",
                mutableSetOf(Role.ADMINISTRATOR, Role.MARKET_MANAGER, Role.STORE_MANAGER)
            ).let(userRepository::save)
        }
        if (marketRepository.query(null, Pageable.unpaged()).content.isEmpty()) {
            val market = Market(
                "大润发",
                "广东省中山市石岐区新都汇2楼",
                "广东省中山市石岐区新都汇2楼",
                LocalDateTime.now(),
                UUID.randomUUID()
            ).let(marketRepository::save)

            if (marketAdminRepository.findByUsername("marketAdmin") == null) {
                MarketAdmin(
                    "marketAdmin",
                    "{noop}admin",
                    "MarketAdministrator",
                    mutableSetOf(Role.MARKET_MANAGER, Role.STORE_MANAGER),
                    market
                ).let(marketAdminRepository::save)
            }
            if (storeRepository.query(null, null, Pageable.unpaged()).content.isEmpty()) {
                val store = Store(
                    "百草园",
                    "百草园",
                    LocalDateTime.now(),
                    market,
                    UUID.randomUUID()
                ).let(storeRepository::save)
                if (storeAdminRepository.findByUsername("storeAdmin") == null) {
                    StoreAdmin(
                        "storeAdmin",
                        "{noop}admin",
                        "StoreAdministrator",
                        mutableSetOf(Role.STORE_MANAGER),
                        store
                    ).let(storeAdminRepository::save)
                    if (suYuanChengRepository.query(
                            null,
                            null,
                            Pageable.unpaged()
                        ).content.isEmpty()
                    ) {
                        SuYuanCheng(
                            store,
                            "XS1806290002",
                            LocalDateTime.now(),
                            UUID.randomUUID()
                        ).let(suYuanChengRepository::save)
                    }
                    if (providerRepository.query(
                            null,
                            null,
                            Pageable.unpaged()
                        ).content.isEmpty()
                    ) {
                        Provider(
                            "百草园水果供应",
                            "百草园水果供应",
                            LocalDateTime.now(),
                            store,
                            UUID.randomUUID()
                        ).let(providerRepository::save)

                    }
                }
            }
        }


    }
}