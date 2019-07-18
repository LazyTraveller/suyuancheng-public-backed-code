package com.suyuancheng.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.KClass

/**
 * @author hsj
 */
@ResponseStatus(HttpStatus.CONFLICT)
class ObjectAlreadyExistException : ApiException {
    constructor(message: String) : super(message)
    constructor(message: String, className: KClass<out Any>) : super(message, className)
}
