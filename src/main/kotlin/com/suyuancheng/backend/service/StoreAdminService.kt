package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.StoreAdminRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.domain.Store
import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.ASDException
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author hsj
 */
@Service
class StoreAdminService(
    private val storeAdminRepository: StoreAdminRepository,
    private val storeRepository: StoreRepository
) {
    fun query(realName: String?, storeId: UUID?, pageable: Pageable) =
        storeAdminRepository.query(realName, storeId, pageable)

    fun create(username: String, password: String, realName: String, storeId: UUID): User {
        val store = storeRepository.findById(storeId)
        if (!store.isPresent) {
            throw ObjectNotFoundException(className = Store::class)
        }
        return storeAdminRepository.save(
            StoreAdmin(
                username,
                "{noop}$password",
                realName,
                mutableSetOf(Role.STORE_MANAGER),
                store.get()
            )
        )
    }

    fun update(id: Int, realName: String, roles: MutableSet<Role>): StoreAdmin {
        val storeAdmin = storeAdminRepository.findById(id)
        if (roles.contains(Role.ADMINISTRATOR)) {
            throw ASDException("Unable to get ADMINISTRATOR permission")
        }
        if (roles.contains(Role.MARKET_MANAGER)) {
            throw ASDException("Unable to get MARKET_MANAGER permission")
        }
        if (!storeAdmin.isPresent) {
            throw ObjectNotFoundException(className = StoreAdmin::class)
        }
        if (!roles.contains(Role.STORE_MANAGER)) {
            roles.add(Role.STORE_MANAGER)
        }
        storeAdmin.get().realName = realName
        storeAdmin.get().roles = roles
        return storeAdminRepository.save(storeAdmin.get())
    }

    fun delete(id: Int) {
        val storeAdmin = storeAdminRepository.findById(id)
        if (!storeAdmin.isPresent) {
            throw ObjectNotFoundException(className = StoreAdmin::class)
        }
        storeAdminRepository.deleteById(id)
    }
}