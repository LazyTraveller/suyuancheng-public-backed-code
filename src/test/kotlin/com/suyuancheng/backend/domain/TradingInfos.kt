package com.suyuancheng.backend.domain

/**
 * @author hsj
 */
fun randomTradingInfo(
    Main: TradingInfo.MainClass,
    Detail: MutableList<TradingInfo.DetailClass>,
    Payment: MutableList<TradingInfo.PaymentClass>
) = TradingInfo(
    Main,
    Detail,
    Payment
)