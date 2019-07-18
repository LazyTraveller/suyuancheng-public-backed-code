package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.service.MarketAdminService
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
@RequestMapping("api/rest/market-admin")
class MarketAdminController(
    private val marketAdminService: MarketAdminService
) {

    data class MarketAdminDTO(
        val username: String,
        val password: String,
        val realName: String,
        val marketId: UUID
    )

    data class MarketAdminDTO2(
        val realName: String,
        val roles: MutableSet<Role>
    )

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) realName: String?,
        @RequestParam(required = false) marketId: UUID?,
        @PageableDefault(
            sort = ["id"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ) = marketAdminService.query(realName, marketId, pageable)

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody marketAdminDTO2: MarketAdminDTO2
    ) = marketAdminService.update(id, marketAdminDTO2.realName, marketAdminDTO2.roles)

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/")
    fun create(
        @RequestBody marketAdminDTO: MarketAdminDTO
    ) = marketAdminService.create(
        marketAdminDTO.username,
        marketAdminDTO.password,
        marketAdminDTO.realName,
        marketAdminDTO.marketId
    )

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Int
    ) {
        marketAdminService.delete(id)
    }
}
