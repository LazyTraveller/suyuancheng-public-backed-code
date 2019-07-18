package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.StoreAdminController
import org.apache.commons.lang3.RandomStringUtils

/**
 * @author hsj
 */
fun randomStoreAdminDTO2(
    realName: String = RandomStringUtils.randomAlphanumeric(5),
    roles: MutableSet<Role> = mutableSetOf(Role.STORE_MANAGER)
) = StoreAdminController.StoreAdminDTO2(
    realName,
    roles
)