package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.Goods
import com.suyuancheng.backend.domain.Provider
import com.suyuancheng.backend.domain.QGoods
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import java.time.LocalDate
import java.util.*

/**
 * @author hsj
 */
interface GoodsRepository : JpaRepository<Goods, UUID>, QuerydslPredicateExecutor<Goods> {
    @JvmDefault
    fun query(
        name: String?,
        fullName: String?,
        sellOut: Boolean?,
        storeId: UUID?,
        before: LocalDate?,
        after: LocalDate?,
        pageable: Pageable
    ): Page<Goods> {
        val query = BooleanBuilder()
        if (name != null) {
            query += QGoods.goods.name.contains(name)
        }
        if (fullName != null) {
            query += QGoods.goods.fullName.contains(fullName)
        }
        if (sellOut != null) {
            query += QGoods.goods.sellOut.eq(sellOut)
        }
        if (before != null && after != null) {
            query += QGoods.goods.purchaseDate.between(before, after)
        }
        if (storeId != null) {
            query += QGoods.goods.provider.store.uuid.eq(storeId)
        }

        return findAll(query, pageable)
    }

    fun findByProvider(provider: Provider): List<Goods>

}