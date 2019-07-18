package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.Entity
import javax.persistence.ManyToOne

/**
 * @author hsj
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class MarketAdmin(

    username: String,

    password: String,

    realName: String,

    roles: MutableSet<Role>,

    @ManyToOne
    var market: Market
) : User(username, password, realName, roles)
