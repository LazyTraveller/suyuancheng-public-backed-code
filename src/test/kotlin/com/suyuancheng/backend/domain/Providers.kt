package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomProvider(
    name: String = RandomStringUtils.randomAlphanumeric(4),
    remark: String? = RandomStringUtils.randomAlphanumeric(20),
    updateTime: LocalDateTime = LocalDateTime.now(),
    store: Store,
    uuid: UUID = UUID.randomUUID()
) = Provider(
    name,
    remark,
    updateTime,
    store,
    uuid
)