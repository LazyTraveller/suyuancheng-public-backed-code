package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.PLU
import com.suyuancheng.backend.domain.QPLU
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.util.*

/**
 * @author hsj
 */
interface PLURepository : JpaRepository<PLU, PLU.PLUId>, QuerydslPredicateExecutor<PLU> {

    @JvmDefault
    fun query(goodsName: String?, suYuanChengId: UUID, code: Int?, pageable: Pageable): Page<PLU> {
        val query = BooleanBuilder()
        if (goodsName != null) {
            query += QPLU.pLU.goods.name.contains(goodsName)
        }
        query += QPLU.pLU.suYuanCheng.uuid.eq(suYuanChengId)
        if (code != null) {
            query += QPLU.pLU.code.eq(code)
        }
        return findAll(query, pageable)
    }

    fun findByIdSuYuanChengId(suYuanChengId: UUID): List<PLU>

    fun findByIdGoodsId(goodsId: UUID): List<PLU>

    fun findByCode(code: Int): Optional<PLU>
}