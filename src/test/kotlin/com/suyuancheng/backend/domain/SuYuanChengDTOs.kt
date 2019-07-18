package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.SuYuanChengController
import org.apache.commons.lang3.RandomStringUtils
import java.util.*

/**
 * @author hsj
 */
fun randomSuYuanChengDTO(
    machineId: String = RandomStringUtils.randomAlphanumeric(12),
    storeId: UUID
) = SuYuanChengController.SuYuanChengDTO(
    machineId,
    storeId
)