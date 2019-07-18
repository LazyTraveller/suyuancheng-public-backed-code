package com.suyuancheng.backend.dao

import com.querydsl.core.BooleanBuilder
import com.suyuancheng.backend.domain.QUser
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.util.plusAssign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor


/**
 * @author hsj
 */
interface UserRepository : JpaRepository<User, Int>, QuerydslPredicateExecutor<User> {
    fun findByUsername(username: String): User?

    @JvmDefault
    fun query(realName: String?, pageable: Pageable): Page<User> {
        val query = BooleanBuilder()
        if (realName != null) {
            query += QUser.user.realName.contains(realName)
        }
        return findAll(query, pageable)
    }
}


