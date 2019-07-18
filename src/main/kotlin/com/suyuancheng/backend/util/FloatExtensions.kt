package com.suyuancheng.backend.util

import java.math.BigDecimal

/**
 * @author hsj
 */
fun Float.setScale(newScale: Int): Float =
    this.toBigDecimal().setScale(newScale, BigDecimal.ROUND_HALF_UP).toFloat()