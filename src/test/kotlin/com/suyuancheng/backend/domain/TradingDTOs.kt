package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.TradingController
import org.apache.commons.lang3.RandomStringUtils
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomTradingDTO(
    suYuanChengId: UUID,
    sequence: String = RandomStringUtils.randomAlphanumeric(16),
    amount: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    time: LocalDateTime = LocalDateTime.now()
) = TradingController.TradingDTO(
    suYuanChengId,
    sequence,
    amount,
    time
)