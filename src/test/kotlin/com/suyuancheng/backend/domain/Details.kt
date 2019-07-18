package com.suyuancheng.backend.domain

import java.math.MathContext
import java.math.RoundingMode
import java.util.*

/**
 * @author hsj
 */
fun randomDetail(
    TransNo: String,
    ProductName: String,
    PLU: Int,
    Qty: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    Unit: String = "KG",
    PriceIncTax: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat()
) = TradingInfo.DetailClass(
    TransNo,
    ProductName,
    PLU,
    Qty,
    Unit,
    PriceIncTax
)