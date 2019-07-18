package com.suyuancheng.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author hsj
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class ASDException(message: String = "Forbidden") : ApiException(message)