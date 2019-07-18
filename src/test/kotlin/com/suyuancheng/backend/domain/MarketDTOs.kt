package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.MarketController
import org.apache.commons.lang3.RandomStringUtils

/**
 * @author hsj
 */
fun randomMarketDTO(
    name: String = RandomStringUtils.randomAlphanumeric(4),
    address: String = RandomStringUtils.randomAlphanumeric(20),
    remark: String? = RandomStringUtils.randomAlphanumeric(20)
) = MarketController.MarketDTO(
    name,
    address,
    remark!!
)