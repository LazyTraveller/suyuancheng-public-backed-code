package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.GoodsController
import com.suyuancheng.backend.util.setScale
import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDate
import java.util.*

/**
 * @author hsj
 */
fun randomGoodsDTOs(
    name: String = RandomStringUtils.randomAlphanumeric(4),
    fullName: String = RandomStringUtils.randomAlphanumeric(4),
    purchaseDate: LocalDate = LocalDate.now(),
    remark: String? = RandomStringUtils.randomAlphanumeric(4),
    sellOut: Boolean = false,
    qty: Float = (Random().nextFloat() * 100).setScale(2),
    unit: String = "kg",
    providerId: UUID
) = GoodsController.GoodsDTO(
    name,
    fullName,
    purchaseDate,
    sellOut,
    remark,
    qty,
    unit,
    providerId
)