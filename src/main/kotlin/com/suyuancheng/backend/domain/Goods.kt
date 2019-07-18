package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate
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
class Goods(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var fullName: String,

    @Column(nullable = false)
    var purchaseDate: LocalDate,

    var remark: String?,

    var updateTime: LocalDateTime,

    var sellOut: Boolean,

    var qty: Float,

    var unit: String,

    @ManyToOne(optional = false)
    var provider: Provider,

    @Id
    @Column(columnDefinition = "BINARY(16)")
    val uuid: UUID
)