package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.QTrading
import com.suyuancheng.backend.domain.SuYuanCheng
import com.suyuancheng.backend.domain.Trading
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
interface TradingRepository : JpaRepository<Trading, UUID>, QuerydslPredicateExecutor<Trading> {

    @JvmDefault
    fun query(
        storeName: String?,
        suYuanChengId: UUID?,
        sequence: String?,
        storeId: UUID,
        before: LocalDateTime?,
        after: LocalDateTime?,
        pageable: Pageable
    ): Page<Trading> {
        val query = BooleanBuilder()
        if (storeName != null) {
            query += QTrading.trading.suYuanCheng.store.name.contains(storeName)
        }
        if (suYuanChengId != null) {
            query += QTrading.trading.suYuanCheng.uuid.eq(suYuanChengId)
        }
        if (sequence != null) {
            query += QTrading.trading.sequence.contains(sequence)
        }
        query += QTrading.trading.suYuanCheng.store.uuid.eq(storeId)
        if (before != null && after != null) {
            query += QTrading.trading.time.between(before, after)
        }

        return findAll(query, pageable)
    }

    fun findBySequence(sequence: String): Optional<Trading>

    fun findBySuYuanChengAndSequence(suYuanCheng: SuYuanCheng, sequence: String): Optional<Trading>
}