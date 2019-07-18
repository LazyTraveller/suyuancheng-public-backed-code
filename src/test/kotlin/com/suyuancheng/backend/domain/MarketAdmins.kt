package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils

/**
 * @author hsj
 */
fun randomMarketAdmin(
    username: String = RandomStringUtils.randomNumeric(13),
    password: String = RandomStringUtils.randomNumeric(10),
    realName: String = RandomStringUtils.randomAlphanumeric(5),
    roles: MutableSet<Role> = mutableSetOf(Role.MARKET_MANAGER, Role.STORE_MANAGER),
    market: Market
) = MarketAdmin(
    username,
    password,
    realName,
    roles,
    market
)