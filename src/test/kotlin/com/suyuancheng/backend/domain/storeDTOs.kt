package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.StoreController
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

/**
 * @author hsj
 */
fun randomStoreDTO(
    name: String = RandomStringUtils.randomAlphanumeric(4),
    remark: String? = RandomStringUtils.randomAlphanumeric(20),
    marketId: UUID
) = StoreController.StoreDTO(
    name,
    remark,
    marketId
)