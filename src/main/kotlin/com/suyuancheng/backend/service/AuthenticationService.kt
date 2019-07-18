package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.GoodsRepository
import com.suyuancheng.backend.dao.ProviderRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.dao.SuYuanChengRepository
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.exception.ASDException
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author hsj
 */
@Service
class AuthenticationService(
    private val suYuanChengRepository: SuYuanChengRepository,
    private val storeRepository: StoreRepository,
    private val providerRepository: ProviderRepository,
    private val goodsRepository: GoodsRepository
) {
    fun authenticationStore(user: User, uuid: UUID) {
        val store = storeRepository.findById(uuid)
        if (!store.isPresent) {
            throw ObjectNotFoundException(className = Store::class)
        }
        if (user is MarketAdmin) {
            if (!storeRepository.findByMarket(user.market).contains(store.get())) {
                throw ASDException("Forbidden")
            }
        } else {

        }
    }

    fun authentication(user: User, storeId: UUID) {
        when (user) {
            is StoreAdmin -> {
                if (storeId != user.store.uuid) {
                    throw ASDException()
                }
            }
            is MarketAdmin -> {
                val store = storeRepository.findById(storeId)
                if (!store.isPresent) {
                    throw ObjectNotFoundException(className = Store::class)
                }
                if (!storeRepository.findByMarket(user.market).contains(store.get())) {
                    throw ASDException()
                }
            }
            else -> {

            }
        }
    }

    fun authenticationSuYuanCheng(user: User, uuid: UUID) {
        val suYuanCheng = suYuanChengRepository.findById(uuid)
        if (!suYuanCheng.isPresent) {
            throw ObjectNotFoundException(className = SuYuanCheng::class)
        }
        val storeId = suYuanCheng.get().store.uuid
        authentication(user, storeId)
    }

    fun authenticationProvider(user: User, uuid: UUID) {
        val provider = providerRepository.findById(uuid)
        if (!provider.isPresent) {
            throw ObjectNotFoundException(className = Provider::class)
        }
        val storeId = provider.get().store.uuid
        authentication(user, storeId)
    }

    fun authenticationGoods(user: User, uuid: UUID) {
        val goods = goodsRepository.findById(uuid)
        if (!goods.isPresent) {
            throw ObjectNotFoundException(className = Goods::class)
        }
        val storeId = goods.get().provider.store.uuid
        authentication(user, storeId)
    }

}