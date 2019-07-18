package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

/**
 * @author hsj
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class Market(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var address: String,

    var remark: String?,

    var updateTime: LocalDateTime,

    @Id
    @Column(columnDefinition = "BINARY(16)")
    val uuid: UUID
)