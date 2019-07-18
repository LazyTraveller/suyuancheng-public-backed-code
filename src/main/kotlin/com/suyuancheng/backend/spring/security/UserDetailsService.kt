package com.suyuancheng.backend.spring.security

import com.suyuancheng.backend.dao.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

/**
 * @author hsj
 */
@Service
class UserDetailsService(private val userRepository: UserRepository): UserDetailsService{
    override fun loadUserByUsername(username: String?): UserDetails {
        println("$username running")
        return userRepository.findByUsername(username!!)
            ?: throw UsernameNotFoundException("$username not found")
    }
}