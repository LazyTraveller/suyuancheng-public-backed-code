package com.suyuancheng.backend.domain

import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
fun randomMarket(
    name: String = RandomStringUtils.randomAlphanumeric(4),
    address: String = RandomStringUtils.randomAlphanumeric(15),
    remark: String? = RandomStringUtils.randomAlphanumeric(20),
    updateTime: LocalDateTime = LocalDateTime.now(),
    uuid: UUID = UUID.randomUUID()
) = Market(
    name,
    address,
    remark,
    updateTime,
    uuid
)