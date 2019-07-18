package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.SuYuanChengRepository
import com.suyuancheng.backend.dao.TradingRepository
import com.suyuancheng.backend.domain.Trading
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
class TradingService(
    private val tradingRepository: TradingRepository,
    private val suYuanChengRepository: SuYuanChengRepository
) {

    fun query(
        storeName: String?,
        suYuanChengId: UUID?,
        sequence: String?,
        storeId: UUID,
        before: LocalDateTime?,
        after: LocalDateTime?,
        pageable: Pageable
    ): Page<Trading> =
        tradingRepository.query(
            storeName,
            suYuanChengId,
            sequence,
            storeId,
            before,
            after,
            pageable
        )

    fun create(
        suYuanChengId: UUID,
        sequence: String,
        amount: Float,
        time: LocalDateTime
    ): Trading {
        val suYuanCheng =
            suYuanChengRepository.findById(suYuanChengId).orElseThrow(::ObjectNotFoundException)
        return tradingRepository.save(
            Trading(
                suYuanCheng,
                sequence,
                amount,
                time,
                LocalDateTime.now(),
                UUID.randomUUID()
            )
        )
    }

}