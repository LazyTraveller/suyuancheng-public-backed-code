package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.MarketAdminController
import org.apache.commons.lang3.RandomStringUtils

/**
 * @author hsj
 */
fun randomMarketAdminDTO2(
    realName: String = RandomStringUtils.randomAlphanumeric(5),
    roles: MutableSet<Role> = mutableSetOf(Role.MARKET_MANAGER, Role.STORE_MANAGER)
) = MarketAdminController.MarketAdminDTO2(
    realName,
    roles
)