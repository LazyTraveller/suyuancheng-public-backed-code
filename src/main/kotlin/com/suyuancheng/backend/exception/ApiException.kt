package com.suyuancheng.backend.exception

import kotlin.reflect.KClass

/**
 * @author hsj
 */
abstract class ApiException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(
        message: String,
        className: KClass<out Any>
    ) : super("${className.simpleName}:$message")
}