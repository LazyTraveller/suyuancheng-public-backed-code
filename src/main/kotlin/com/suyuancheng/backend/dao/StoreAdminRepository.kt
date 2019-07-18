package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.QStoreAdmin
import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

/**
 * @author hsj
 */
interface StoreAdminRepository : JpaRepository<StoreAdmin, Int>,
    QuerydslPredicateExecutor<StoreAdmin> {

    fun findByUsername(username: String): StoreAdmin?

    @JvmDefault
    fun query(realName: String?, storeId: UUID?, pageable: Pageable): Page<StoreAdmin> {
        val query = BooleanBuilder()
        if (realName != null) {
            query += QStoreAdmin.storeAdmin.realName.contains(realName)
        }
        if (storeId != null) {
            query += QStoreAdmin.storeAdmin.store.uuid.eq(storeId)
        }
        return findAll(query, pageable)
    }
}