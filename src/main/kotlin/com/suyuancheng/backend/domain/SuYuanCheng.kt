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
class SuYuanCheng(
    @ManyToOne(optional = false)
    var store: Store,

    var machineId: String,

    var updateTime: LocalDateTime,

    @Id
    @Column(columnDefinition = "BINARY(16)")
    val uuid: UUID
)