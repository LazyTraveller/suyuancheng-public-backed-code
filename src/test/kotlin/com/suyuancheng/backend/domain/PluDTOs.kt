package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.PLUController
import java.math.MathContext
import java.math.RoundingMode
import java.util.*

/**
 * @author hsj
 */
fun randomePluDTO(
    suYuanChengId: UUID,
    goodsId: UUID,
    code: Int = Random().nextInt(80) + 1,
    price: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat()
) = PLUController.PluDTO(
    suYuanChengId,
    goodsId,
    code,
    price
)