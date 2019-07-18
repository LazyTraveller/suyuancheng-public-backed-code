package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.MarketAdminRepository
import com.suyuancheng.backend.dao.MarketRepository
import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.domain.MarketAdmin
import com.suyuancheng.backend.domain.Role
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
class MarketAdminService(
    private val marketAdminRepository: MarketAdminRepository,
    private val marketRepository: MarketRepository
) {
    fun query(realName: String?, marketId: UUID?, pageable: Pageable) =
        marketAdminRepository.query(realName, marketId, pageable)

    fun create(username: String, password: String, realName: String, marketId: UUID): User {
        val market = marketRepository.findById(marketId)
        if (!market.isPresent) {
            throw ObjectNotFoundException(className = Market::class)
        }
        return marketAdminRepository.save(
            MarketAdmin(
                username,
                "{noop}$password",
                realName,
                mutableSetOf(Role.MARKET_MANAGER, Role.STORE_MANAGER),
                market.get()
            )
        )
    }

    fun update(id: Int, realName: String, roles: MutableSet<Role>): MarketAdmin {
        val marketAdmin = marketAdminRepository.findById(id)
        if (roles.contains(Role.ADMINISTRATOR)) {
            throw ASDException("Unable to get ADMINISTRATOR permission")
        }
        if (!marketAdmin.isPresent) {
            throw ObjectNotFoundException(className = MarketAdmin::class)
        }
        if (!roles.contains(Role.MARKET_MANAGER)) {
            roles.add(Role.MARKET_MANAGER)
        }
        marketAdmin.get().realName = realName
        marketAdmin.get().roles = roles
        return marketAdminRepository.save(marketAdmin.get())
    }

    fun delete(id: Int) {
        val marketAdmin = marketAdminRepository.findById(id)
        if (!marketAdmin.isPresent) {
            throw ObjectNotFoundException(className = MarketAdmin::class)
        }
        marketAdminRepository.deleteById(id)
    }
}
