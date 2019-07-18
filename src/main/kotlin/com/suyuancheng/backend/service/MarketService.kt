package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class MarketService(private val marketRepository: MarketRepository) {

    fun list(): List<Market> = marketRepository.findAll()
    fun query(name: String?, pageable: Pageable) =
        marketRepository.query(name, pageable)

    fun create(name: String, address: String, remark: String?): Market {
        return marketRepository.save(
            Market(name, address, remark, LocalDateTime.now(), UUID.randomUUID())
        )
    }

    fun update(uuid: UUID,name: String, address: String, remark: String?): Market{
        return marketRepository.findById(uuid).orElseThrow(::ObjectNotFoundException).apply {
            this.name = name
            this.address = address
            this.remark = remark
            this.updateTime = LocalDateTime.now()
        }.let(marketRepository::save)
    }

    fun delete(uuid: UUID){
        marketRepository.findById(uuid).orElseThrow(::ObjectNotFoundException)
        marketRepository.deleteById(uuid)
    }
}