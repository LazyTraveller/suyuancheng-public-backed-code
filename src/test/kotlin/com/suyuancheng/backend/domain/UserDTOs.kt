package com.suyuancheng.backend.domain

import com.suyuancheng.backend.api.rest.UserController
import org.apache.commons.lang3.RandomStringUtils

/**
 * @author hsj
 */
fun randomUserDTO(
    username: String = RandomStringUtils.randomNumeric(13),
    password: String = RandomStringUtils.randomNumeric(10),
    realName: String = RandomStringUtils.randomAlphanumeric(5)
) = UserController.UserDTO(
    username,
    password,
    realName
)