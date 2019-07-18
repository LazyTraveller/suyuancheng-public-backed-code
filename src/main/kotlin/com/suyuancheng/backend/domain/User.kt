package com.suyuancheng.backend.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

/**
 * @author hsj
 */
@Entity
@JsonIgnoreProperties("password")
open class User(

    @Column(nullable = false)
    private var username: String,

    @Column(nullable = false)
    private var password: String,

    @Column(nullable = false)
    var realName: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<Role>,

    @Id
    @GeneratedValue
    val id: Int = -1
) : UserDetails {


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authList = mutableListOf<GrantedAuthority>()
        val roles = this.roles
        roles.forEach {
            authList.add(SimpleGrantedAuthority(it.name))
            println(it.name)
        }
        return authList
    }

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username

    override fun isEnabled(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}