package com.suyuancheng.backend.domain

import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomReMain(
    TransNo: String,
    Subtotal: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    TransDate: LocalDateTime = LocalDateTime.now(),
    MarketName: String,
    StoreName: String
) = ReTradingInfo.MainClass(
    TransNo,
    Subtotal,
    TransDate,
    MarketName,
    StoreName
)