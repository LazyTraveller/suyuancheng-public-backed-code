package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author hsj
 */
class ReTradingInfo(
    var Main: MainClass,
    var Detail: MutableList<DetailClass>
) {
    data class MainClass(
        var TransNo: String,
        var Subtotal: Float,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        var TransDate: LocalDateTime,
        var MarketName: String,
        var StoreName: String
    )

    data class DetailClass(
        var ProductName: String,
        var ProviderName: String,
        var Qty: Float,
        val Unit: String,
        var PriceIncTax: Float,
        var Amount: Float,
        var purchaseDate: LocalDate
    )
}