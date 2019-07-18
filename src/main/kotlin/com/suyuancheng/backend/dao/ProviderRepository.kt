package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.Provider
import com.suyuancheng.backend.domain.QProvider
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
interface ProviderRepository : JpaRepository<Provider, UUID>, QuerydslPredicateExecutor<Provider> {
    @JvmDefault
    fun query(name: String?, storeId: UUID?, pageable: Pageable): Page<Provider> {
        val query = BooleanBuilder()
        if (name != null) {
            query += QProvider.provider.name.contains(name)
        }
        if (storeId != null) {
            query += QProvider.provider.store.uuid.eq(storeId)
        }
        return findAll(query, pageable)
    }

    fun findByStore(store: Store): List<Provider>
}