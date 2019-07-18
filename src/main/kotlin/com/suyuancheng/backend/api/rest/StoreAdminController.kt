package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.service.StoreAdminService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/store-admin")
class StoreAdminController(
    private val storeAdminService: StoreAdminService
) {
    data class StoreAdminDTO(
        val username: String,
        val password: String,
        val realName: String,
        val storeId: UUID
    )

    data class StoreAdminDTO2(
        val realName: String,
        val roles: MutableSet<Role>
    )

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) realName: String?,
        @RequestParam(required = false) storeId: UUID?,
        @PageableDefault(
            sort = ["id"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ) = storeAdminService.query(realName, storeId, pageable)

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody storeAdminDTO2: StoreAdminDTO2
    ) = storeAdminService.update(id, storeAdminDTO2.realName, storeAdminDTO2.roles)

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/")
    fun create(
        @RequestBody storeAdminDTO: StoreAdminDTO
    ) = storeAdminService.create(
        storeAdminDTO.username,
        storeAdminDTO.password,
        storeAdminDTO.realName,
        storeAdminDTO.storeId
    )

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Int
    ) {
        storeAdminService.delete(id)
    }
}