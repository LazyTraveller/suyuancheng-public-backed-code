package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

/**
 * @author hsj
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class PLU(

    @Column(nullable = false)
    var code: Int,

    var price: Float,

    var updateTime: LocalDateTime,

    @ManyToOne
    var goods: Goods,

    @ManyToOne
    var suYuanCheng: SuYuanCheng,

    @EmbeddedId
    val id: PLUId

) {
    @Embeddable
    data class PLUId(
        @Column(columnDefinition = "BINARY(16)") val suYuanChengId: UUID
        , @Column(columnDefinition = "BINARY(16)") val goodsId: UUID
    ) : Serializable
}