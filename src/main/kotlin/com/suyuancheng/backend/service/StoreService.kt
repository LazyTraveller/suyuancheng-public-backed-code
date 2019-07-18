package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.domain.Store
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val marketRepository: MarketRepository
) {

    fun list(market: Market): List<Store> {
        return storeRepository.findByMarket(market)
    }

    fun list(marketId: UUID): List<Store> {
        val market = marketRepository.findById(marketId)
        if (!market.isPresent) {
            throw ObjectNotFoundException(className = Market::class)
        }
        return list(market.get())
    }

    fun query(name: String?, marketId: UUID?, pageable: Pageable) =
        storeRepository.query(name, marketId, pageable)

    fun create(name: String, marketId: UUID, remark: String?): Store {
        val market = marketRepository.findById(marketId).orElseThrow(::ObjectNotFoundException)
        return storeRepository.save(
            Store(name, remark, LocalDateTime.now(), market, UUID.randomUUID())
        )
    }

    fun update(uuid: UUID, name: String, marketId: UUID, remark: String?): Store {
        val market = marketRepository.findById(marketId).orElseThrow(::ObjectNotFoundException)
        return storeRepository.findById(uuid).orElseThrow(::ObjectNotFoundException).apply {
            this.name = name
            this.remark = remark
            this.updateTime = LocalDateTime.now()
            this.market = market
        }.let(storeRepository::save)
    }

    fun delete(uuid: UUID) {
        storeRepository.deleteById(uuid)
    }
}