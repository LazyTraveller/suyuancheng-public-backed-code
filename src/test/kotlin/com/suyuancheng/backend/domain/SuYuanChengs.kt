package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomSuYuanCheng(
    store: Store,
    machineId: String = RandomStringUtils.randomAlphanumeric(12),
    uuid: UUID = UUID.randomUUID()
) = SuYuanCheng(
    store,
    machineId,
    LocalDateTime.now(),
    uuid
)