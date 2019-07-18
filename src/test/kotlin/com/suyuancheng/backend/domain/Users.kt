package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils

/**
 * @author hsj
 */
fun randomUser(
    username: String = RandomStringUtils.randomNumeric(13),
    password: String = RandomStringUtils.randomNumeric(10),
    realName: String = RandomStringUtils.randomAlphanumeric(5),
    roles: MutableSet<Role> = mutableSetOf(Role.ADMINISTRATOR),
    id: Int = -1
) = User(
    username,
    password,
    realName,
    roles,
    id
)