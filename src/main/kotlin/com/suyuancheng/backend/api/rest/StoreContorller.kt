package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.MarketAdmin
import com.suyuancheng.backend.domain.Store
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.ASDException
import com.suyuancheng.backend.exception.CanNotNullException
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.StoreService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @author hsj
 */

@RestController
@RequestMapping("api/rest/store")
class StoreController(
    private val storeService: StoreService,
    private val authenticationService: AuthenticationService
) {
    data class StoreDTO(
        val name: String,
        val remark: String?,
        val marketId: UUID
    )

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/list")
    fun list(
        @RequestParam(required = false) marketId: UUID?,
        @AuthenticationPrincipal user: User
    ): List<Store> = if (user is MarketAdmin) {
        storeService.list(user.market)
    } else {
        if (marketId == null) {
            throw CanNotNullException("marketId can not be null")
        }
        storeService.list(marketId)
    }

    @PreAuthorize("hasAuthority('MARKET_MANAGER')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) marketId: UUID?,
        @AuthenticationPrincipal user: User,
        @PageableDefault(
            size = 10,
            sort = ["updateTime"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): Page<Store> = if (user is MarketAdmin) {
        storeService.query(name, user.market.uuid, pageable)
    } else {
        storeService.query(name, marketId, pageable)
    }

    @PreAuthorize("hasAuthority('MARKET_MANAGER')")
    @PutMapping("{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody storeDTO: StoreDTO,
        @AuthenticationPrincipal user: User
    ): Store = if (user is MarketAdmin) {
        if (storeDTO.marketId != user.market.uuid) {
            throw ASDException("Forbidden")
        }
        storeService.update(
            uuid, storeDTO.name, user.market.uuid, storeDTO.remark
        )
    } else {
        storeService.update(
            uuid, storeDTO.name, storeDTO.marketId, storeDTO.remark
        )
    }

    @PreAuthorize("hasAuthority('MARKET_MANAGER')")
    @PostMapping("/")
    fun create(
        @RequestBody storeDTO: StoreDTO,
        @AuthenticationPrincipal user: User
    ): Store = if (user is MarketAdmin) {
        if (storeDTO.marketId != user.market.uuid) {
            throw ASDException("Forbidden")
        }
        storeService.create(
            storeDTO.name, user.market.uuid, storeDTO.remark
        )
    } else {
        storeService.create(
            storeDTO.name, storeDTO.marketId, storeDTO.remark
        )
    }

    @PreAuthorize("hasAuthority('MARKET_MANAGER')")
    @DeleteMapping("{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
        @AuthenticationPrincipal user: User
    ) {
        authenticationService.authenticationStore(user, uuid)
        storeService.delete(uuid)
    }

}