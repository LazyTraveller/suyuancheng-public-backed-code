package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.domain.QMarket
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

/**
 * @author hsj
 */
interface MarketRepository: JpaRepository<Market, UUID>, QuerydslPredicateExecutor<Market>{
    @JvmDefault
    fun query(name: String?, pageable: Pageable): Page<Market> {
        val query = BooleanBuilder()
        if (name != null) {
            query += QMarket.market.name.contains(name)
        }
        return findAll(query, pageable)
    }
}