package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomTrading(
    suYuanCheng: SuYuanCheng,
    sequence: String = RandomStringUtils.randomAlphanumeric(16),
    amount: Float = (Random().nextFloat() * 100).toBigDecimal().setScale(
        2,
        BigDecimal.ROUND_HALF_UP
    ).toFloat(),
    time: LocalDateTime = LocalDateTime.now().withYear(Random().nextInt(17) + 2000).withMonth(
        Random().nextInt(12) + 1
    ).withDayOfMonth(Random().nextInt(28) + 1),
    uuid: UUID = UUID.randomUUID()
) = Trading(
    suYuanCheng,
    sequence,
    amount,
    time,
    LocalDateTime.now(),
    uuid
)