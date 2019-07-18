package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.UserRepository
import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * @author hsj
 */
@Service
class UserService(private val userRepository: UserRepository) {

    fun query(realName: String?, pageable: Pageable) =
        userRepository.query(realName, pageable)

    fun update(id: Int, roles: MutableSet<Role>): User {
        val user = userRepository.findById(id).orElseThrow(::ObjectNotFoundException)
        user.roles = roles
        return userRepository.save(user)
    }

    fun create(username: String, password: String, realName: String): User {
        return userRepository.save(
            User(
                username,
                "{noop}$password",
                realName,
                mutableSetOf()
            )
        )
    }
}