package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.GoodsRepository
import com.suyuancheng.backend.dao.TradingItemRepository
import com.suyuancheng.backend.dao.TradingRepository
import com.suyuancheng.backend.domain.TradingItem
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class TradingItemService(
    private val tradingItemRepository: TradingItemRepository,
    private val tradingRepository: TradingRepository,
    private val goodsRepository: GoodsRepository
) {
    fun query(
        goodsName: String?,
        tradingId: UUID?,
        pageable: Pageable
    ): Page<TradingItem> =
        tradingItemRepository.query(goodsName, tradingId, pageable)

    fun create(
        tradingId: UUID,
        goodsId: UUID,
        weight: Float,
        unit: String,
        price: Float,
        amount: Float
    ): TradingItem {
        val trading = tradingRepository.findById(tradingId).orElseThrow(::ObjectNotFoundException)
        val goods = goodsRepository.findById(goodsId).orElseThrow(::ObjectNotFoundException)
        return tradingItemRepository.save(
            TradingItem(
                trading,
                goods,
                weight,
                unit,
                price,
                amount,
                LocalDateTime.now(),
                UUID.randomUUID()
            )
        )
    }
}