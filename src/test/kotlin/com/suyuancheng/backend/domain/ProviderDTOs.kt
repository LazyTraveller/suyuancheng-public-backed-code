package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.ProviderController
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

/**
 * @author hsj
 */
fun randomProviderDTO(
    name: String = RandomStringUtils.randomAlphanumeric(4),
    remark: String? = RandomStringUtils.randomAlphanumeric(20),
    storeId: UUID
) = ProviderController.ProviderDTO(
    name,
    remark,
    storeId
)