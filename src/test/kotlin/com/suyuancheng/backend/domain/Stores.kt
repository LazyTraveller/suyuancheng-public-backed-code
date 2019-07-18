package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomStore(
    name: String = RandomStringUtils.randomAlphanumeric(4),
    remark: String? = RandomStringUtils.randomAlphanumeric(20),
    updateTime: LocalDateTime = LocalDateTime.now(),
    market: Market,
    uuid: UUID = UUID.randomUUID()
) = Store(
    name,
    remark,
    updateTime,
    market,
    uuid
)