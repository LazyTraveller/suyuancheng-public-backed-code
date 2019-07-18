package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.QSuYuanCheng
import com.suyuancheng.backend.domain.SuYuanCheng
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

/**
 * @author hsj
 */
interface SuYuanChengRepository : JpaRepository<SuYuanCheng, UUID>,
    QuerydslPredicateExecutor<SuYuanCheng> {

    @JvmDefault
    fun query(
        machineId: String?,
        storeId: UUID?,
        pageable: Pageable
    ): Page<SuYuanCheng> {
        val query = BooleanBuilder()
        if (machineId != null) {
            query += QSuYuanCheng.suYuanCheng.machineId.contains(machineId)
        }
        if (storeId != null) {
            query += QSuYuanCheng.suYuanCheng.store.uuid.eq(storeId)
        }
        return findAll(query, pageable)
    }

    fun findByMachineId(machineId: String): Optional<SuYuanCheng>
}