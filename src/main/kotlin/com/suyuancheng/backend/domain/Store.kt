package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

/**
 * @author hsj
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class Store(
    @Column(nullable = false)
    var name: String,

    var remark: String?,

    var updateTime: LocalDateTime,

    @ManyToOne(optional = false)
    var market: Market,

    @Id
    @Column(columnDefinition = "BINARY(16)")
    val uuid: UUID
)