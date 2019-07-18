package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.QTradingItem
import com.suyuancheng.backend.domain.Trading
import com.suyuancheng.backend.domain.TradingItem
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

/**
 * @author hsj
 */
interface TradingItemRepository : JpaRepository<TradingItem, UUID>,
    QuerydslPredicateExecutor<TradingItem> {

    @JvmDefault
    fun query(
        goodsName: String?,
        tradingId: UUID?,
        pageable: Pageable
    ): Page<TradingItem> {
        val query = BooleanBuilder()
        if (goodsName != null) {
            query += QTradingItem.tradingItem.goods.name.contains(goodsName)
        }
        if (tradingId != null) {
            query += QTradingItem.tradingItem.trading.uuid.eq(tradingId)
        }
        return findAll(query, pageable)
    }

    fun findByTrading(trading: Trading): List<TradingItem>
}