package com.suyuancheng.backend.domain

import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*

/**
 * @author hsj
 */
fun randomReDetail(
    ProductName: String,
    ProviderName: String,
    Qty: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    unit: String = "KG",
    PriceIncTax: Float = (Random().nextFloat() * 100).toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    Amount: Float = Qty * PriceIncTax.toBigDecimal(
        MathContext(
            4,
            RoundingMode.FLOOR
        )
    ).toFloat(),
    purchaseDate: LocalDate
) = ReTradingInfo.DetailClass(
    ProductName,
    ProviderName,
    Qty,
    unit,
    PriceIncTax,
    Amount,
    purchaseDate
)