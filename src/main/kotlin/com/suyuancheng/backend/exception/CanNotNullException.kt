package com.suyuancheng.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.KClass

/**
 * @author hsj
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class CanNotNullException : ApiException {
    constructor(message: String) : super(message)
    constructor(message: String, className: KClass<out Any>) : super(message, className)
}
