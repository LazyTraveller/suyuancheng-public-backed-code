package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils

/**
 * @author hsj
 */
fun randomStoreAdmins(
    username: String = RandomStringUtils.randomNumeric(13),
    password: String = RandomStringUtils.randomNumeric(10),
    realName: String = RandomStringUtils.randomAlphanumeric(5),
    roles: MutableSet<Role> = mutableSetOf(Role.STORE_MANAGER),
    store: Store
) = StoreAdmin(
    username,
    password,
    realName,
    roles,
    store
)