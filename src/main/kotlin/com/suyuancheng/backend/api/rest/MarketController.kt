package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.Market
import com.suyuancheng.backend.service.MarketService
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
@RequestMapping("api/rest/market")
class MarketController(
    private val marketService: MarketService
) {
    data class MarketDTO(
        val name: String,
        val address: String,
        val remark: String?
    )

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @GetMapping("/list")
    fun list(): List<Market> = marketService.list()


    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) name: String?,
        @PageableDefault(
            size = 10,
            sort = ["updateTime"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ) = marketService.query(name, pageable)

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody marketDTO: MarketDTO
    ) = marketService.update(
        uuid, marketDTO.name, marketDTO.address, marketDTO.remark
    )

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/")
    fun create(
        @RequestBody marketDTO: MarketDTO
    ) = marketService.create(
        marketDTO.name, marketDTO.address, marketDTO.remark
    )

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @DeleteMapping("{uuid}")
    fun delete(
        @PathVariable uuid: UUID
    ) {
        marketService.delete(uuid)
    }
}