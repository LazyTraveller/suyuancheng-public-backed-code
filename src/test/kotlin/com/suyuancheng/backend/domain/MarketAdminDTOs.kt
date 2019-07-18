package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.MarketAdminController
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

/**
 * @author hsj
 */
fun randomMarketAdminDTO(
    username: String = RandomStringUtils.randomNumeric(13),
    password: String = RandomStringUtils.randomNumeric(10),
    realName: String = RandomStringUtils.randomAlphanumeric(5),
    marketId: UUID
) = MarketAdminController.MarketAdminDTO(
    username,
    password,
    realName,
    marketId
)