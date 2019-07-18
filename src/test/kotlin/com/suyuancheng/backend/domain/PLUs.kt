package com.suyuancheng.backend.domain

import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomPLU(
    code: Int = Random().nextInt(80) + 1,
    price: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    goods: Goods,
    suYuanCheng: SuYuanCheng,
    id: PLU.PLUId
) = PLU(
    code,
    price,
    LocalDateTime.now(),
    goods,
    suYuanCheng,
    id
)