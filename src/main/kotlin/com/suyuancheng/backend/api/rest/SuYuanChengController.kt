package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.domain.SuYuanCheng
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.CanNotNullException
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.SuYuanChengService
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
@RequestMapping("api/rest/su-yuan-cheng")
class SuYuanChengController(
    private val suYuanChengService: SuYuanChengService,
    private val authenticationService: AuthenticationService
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SuYuanChengDTO(
        val machineId: String,
        val storeId: UUID
    )

    @PreAuthorize("hasAnyAuthority('STORE_MANAGER', 'ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) machineId: String?,
        @RequestParam(required = false) storeId: UUID?,
        @AuthenticationPrincipal user: User,
        @PageableDefault(
            size = 10,
            sort = ["updateTime"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ) = when (user) {
        is StoreAdmin -> {
            suYuanChengService.query(machineId, user.store.uuid, pageable)
        }
        else -> {
            if (storeId == null) {
                throw CanNotNullException("storeId can not be null")
            }
            authenticationService.authenticationStore(user, storeId)
            suYuanChengService.query(machineId, storeId, pageable)
        }
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PostMapping("/")
    fun create(
        @RequestBody suYuanChengDTO: SuYuanChengDTO,
        @AuthenticationPrincipal user: User
    ): SuYuanCheng {
        authenticationService.authentication(user, suYuanChengDTO.storeId)
        return suYuanChengService.create(suYuanChengDTO.machineId, suYuanChengDTO.storeId)
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PutMapping("{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody suYuanChengDTO: SuYuanChengDTO,
        @AuthenticationPrincipal user: User
    ): SuYuanCheng {
        authenticationService.authentication(user, suYuanChengDTO.storeId)
        return suYuanChengService.update(uuid, suYuanChengDTO.storeId, suYuanChengDTO.machineId)
    }


    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @DeleteMapping("{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
        @AuthenticationPrincipal user: User
    ) {
        authenticationService.authenticationSuYuanCheng(user, uuid)
        suYuanChengService.delete(uuid)
    }

}
