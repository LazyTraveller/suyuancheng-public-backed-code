package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.Provider
import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.CanNotNullException
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.ProviderService
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
@RequestMapping("api/rest/provider")
class ProviderController(
    private val providerService: ProviderService,
    private val authenticationService: AuthenticationService
) {
    data class ProviderDTO(
        val name: String,
        val remark: String?,
        val storeId: UUID
    )

    @PreAuthorize("hasAnyAuthority('STORE_MANAGER', 'ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/list")
    fun list(
        @RequestParam(required = false) storeId: UUID?,
        @AuthenticationPrincipal user: User
    ): List<Provider> = when (user) {
        is StoreAdmin -> {
            providerService.list(user.store)
        }
        else -> {
            if (storeId == null) {
                throw CanNotNullException("storeId can not be null")
            }
            authenticationService.authenticationStore(user, storeId)
            providerService.list(storeId)
        }
    }


    @PreAuthorize("hasAnyAuthority('STORE_MANAGER', 'ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) storeId: UUID?,
        @AuthenticationPrincipal user: User,
        @PageableDefault(
            size = 10,
            sort = ["updateTime"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ) = when (user) {
        is StoreAdmin -> {
            providerService.query(name, user.store.uuid, pageable)
        }
        else -> {
            if (storeId == null) {
                throw CanNotNullException("storeId can not be null")
            }
            authenticationService.authenticationStore(user, storeId)
            providerService.query(name, storeId, pageable)
        }
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PutMapping("{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody providerDTO: ProviderDTO,
        @AuthenticationPrincipal user: User
    ) = providerService.update(uuid, providerDTO.name, providerDTO.remark)

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PostMapping("/")
    fun create(
        @RequestBody providerDTO: ProviderDTO,
        @AuthenticationPrincipal user: User
    ): Provider {
        authenticationService.authentication(user, providerDTO.storeId)
        return providerService.create(providerDTO.name, providerDTO.storeId, providerDTO.remark)
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @DeleteMapping("{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
        @AuthenticationPrincipal user: User
    ) {
        authenticationService.authenticationProvider(user, uuid)
        providerService.delete(uuid)
    }
}
