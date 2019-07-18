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
class TradingItem(

    @ManyToOne
    var trading: Trading,

    @ManyToOne
    var goods: Goods,

    var weight: Float,

    val unit: String,

    var price: Float,

    var amount: Float,

    var updateTime: LocalDateTime,

    @Id
    @Column(columnDefinition = "BINARY(16)")
    val uuid: UUID
)