package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.StoreAdminController
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

/**
 * @author hsj
 */
fun randomStoreAdminDTO(
    username: String = RandomStringUtils.randomNumeric(13),
    password: String = RandomStringUtils.randomNumeric(10),
    realName: String = RandomStringUtils.randomAlphanumeric(5),
    storeId: UUID
) = StoreAdminController.StoreAdminDTO(
    username,
    password,
    realName,
    storeId
)