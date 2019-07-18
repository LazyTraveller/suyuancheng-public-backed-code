package com.suyuancheng.backend.util

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import java.math.BigDecimal

/**
 * @author hsj
 */
operator fun BooleanBuilder.plusAssign(right: Predicate?) {
    and(right)
}
