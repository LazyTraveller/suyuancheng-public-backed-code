package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.UserRepository
import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.domain.randomUser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    lateinit var user1: User
    lateinit var user2: User
    lateinit var user3: User
    @Before
    fun setup() {
        user1 = randomUser().let(userRepository::save)
        user2 = randomUser().let(userRepository::save)
        user3 = randomUser().let(userRepository::save)
    }

    @Test
    fun query() {
        assertEquals(1, userService.query(user1.realName, Pageable.unpaged()).totalElements)
    }

    @Test
    fun update() {
        val roles = mutableSetOf(Role.MARKET_MANAGER, Role.ADMINISTRATOR)
        assertEquals(1, userRepository.findById(user1.id).get().roles.size)
        userService.update(user1.id, roles)
        assertEquals(2, userRepository.findById(user1.id).get().roles.size)
    }

    @Test
    fun create() {
        var user = randomUser()
        user = userService.create(user.username, user.password, user.realName)
        assertEquals(user.realName, userRepository.findById(user.id).get().realName)
    }
}