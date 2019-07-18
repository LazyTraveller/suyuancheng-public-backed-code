package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.TradingItemController
import java.math.MathContext
import java.math.RoundingMode
import java.util.*

/**
 * @author hsj
 */
fun randomTradingItemDTO(
    tradingId: UUID,
    goodsId: UUID,
    weight: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
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
    ).toFloat()
) = TradingItemController.TradingItemDTO(
    tradingId,
    goodsId,
    weight,
    price,
    amount
)