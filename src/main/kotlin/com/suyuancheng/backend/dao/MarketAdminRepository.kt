package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.MarketAdmin
import com.suyuancheng.backend.domain.QMarketAdmin
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

/**
 * @author hsj
 */
interface MarketAdminRepository : JpaRepository<MarketAdmin, Int>,
    QuerydslPredicateExecutor<MarketAdmin> {
    fun findByUsername(username: String): MarketAdmin?

    @JvmDefault
    fun query(realName: String?, marketId: UUID?, pageable: Pageable): Page<MarketAdmin> {
        val query = BooleanBuilder()
        if (realName != null) {
            query += QMarketAdmin.marketAdmin.realName.contains(realName)
        }
        if (marketId != null) {
            query += QMarketAdmin.marketAdmin.market.uuid.eq(marketId)
        }
        return findAll(query, pageable)
    }
}
