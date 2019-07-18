package com.suyuancheng.backend.domain

/**
 * @author hsj
 */
fun randomPayment(
    TransNo: String,
    Amount: Float
) = TradingInfo.PaymentClass(
    TransNo,
    Amount
)