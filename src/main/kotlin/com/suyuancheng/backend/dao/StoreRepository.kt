package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.domain.QStore
import com.suyuancheng.backend.domain.Store
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

/**
 * @author hsj
 */
interface StoreRepository : JpaRepository<Store, UUID>, QuerydslPredicateExecutor<Store> {

    fun findByMarket(market: Market): List<Store>
    @JvmDefault
    fun query(name: String?, marketId: UUID?, pageable: Pageable): Page<Store> {
        val query = BooleanBuilder()
        if (name != null) {
            query += QStore.store.name.contains(name)
        }
        if (marketId != null) {
            query += QStore.store.market.uuid.eq(marketId)
        }
        return findAll(query, pageable)
    }
}