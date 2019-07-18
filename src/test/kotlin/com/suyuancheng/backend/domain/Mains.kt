package com.suyuancheng.backend.domain

import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author hsj
 */
fun randomeMain(
    TransNo: String,
    TransDate: LocalDateTime = LocalDateTime.parse(
        "2015-05-08 13:15:21",
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    ),
    Subtotal: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    MachineId: String
) = TradingInfo.MainClass(
    TransNo,
    TransDate,
    Subtotal,
    MachineId
)