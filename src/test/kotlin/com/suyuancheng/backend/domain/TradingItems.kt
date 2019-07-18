package com.suyuancheng.backend.domain

import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomTradingItem(
    trading: Trading,
    goods: Goods,
    weight: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    unit: String = "KG",
    price: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    amount: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    uuid: UUID = UUID.randomUUID()
) = TradingItem(
    trading,
    goods,
    weight,
    unit,
    price,
    amount,
    LocalDateTime.now(),
    uuid
)