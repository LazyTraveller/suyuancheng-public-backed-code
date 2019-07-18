package com.suyuancheng.backend.domain

import com.suyuancheng.backend.util.setScale
import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomGoods(
    name: String = RandomStringUtils.randomAlphanumeric(5),
    fullName: String = RandomStringUtils.randomAlphanumeric(10),
    purchaseDate: LocalDate = LocalDate.now(),
    remark: String? = RandomStringUtils.randomAlphanumeric(20),
    updateTime: LocalDateTime = LocalDateTime.now(),
    sellOut: Boolean = false,
    qty: Float = (Random().nextFloat() * 100).setScale(2),
    unit: String = "kg",
    provider: Provider,
    uuid: UUID = UUID.randomUUID()
) = Goods(
    name,
    fullName,
    purchaseDate,
    remark,
    updateTime,
    sellOut,
    qty,
    unit,
    provider,
    uuid
)