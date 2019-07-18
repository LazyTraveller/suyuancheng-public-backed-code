package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

/**
 * @author hsj
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
data class TradingInfo(
    val Main: MainClass,
    val Detail: MutableList<DetailClass>,
    val Payment: MutableList<PaymentClass>
) {
    @JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
    )
    data class MainClass(
        val TransNo: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        val TransDate: LocalDateTime,
        val Subtotal: Float,
        val MachineId: String
    )

    @JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
    )
    data class DetailClass(
        val TransNo: String,
        val ProductName: String,
        val PLU: Int,
        val Qty: Float,
        val Unit: String,
        val PriceIncTax: Float
    )

    @JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
    )
    data class PaymentClass(
        val TransNo: String,
        val Amount: Float
    )
}