package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.PLU
import com.suyuancheng.backend.service.PLUService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/plu")
class PLUController(
    private val pluService: PLUService
) {

    data class PluDTO(

        val suYuanChengId: UUID,

        val goodsId: UUID,

        @field:DecimalMin("1")
        @field:DecimalMax("81")
        val code: Int,

        @field:Digits(integer = 12, fraction = 2)
        val price: Float
    )

    data class PluDTO2(
        @field:DecimalMin("1")
        @field:DecimalMax("81")
        val code: Int,

        @field:Digits(integer = 12, fraction = 2)
        val price: Float
    )

    @PreAuthorize("hasAnyAuthority('STORE_MANAGER', 'ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) goodsName: String?,
        @RequestParam suYuanChengId: UUID,
        @RequestParam(required = false) code: Int?,
        @PageableDefault(
            size = 10,
            sort = ["code"],
            direction = Sort.Direction.ASC
        ) pageable: Pageable
    ): Page<PLU> = pluService.query(goodsName, suYuanChengId, code, pageable)

    @GetMapping("/list")
    fun list(
        @RequestParam machineId: String
    ): List<PLU> = pluService.listBySuYuanCheng(machineId)

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PostMapping("/")
    fun create(
        @Valid @RequestBody pluDTOs: List<PluDTO>
    ): List<PLU> {
        return pluService.create(pluDTOs)
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PutMapping("{suYuanChengId}/{goodsId}")
    fun update(
        @PathVariable suYuanChengId: UUID,
        @PathVariable goodsId: UUID,
        @Valid @RequestBody pluDTO2: PluDTO2
    ) = pluService.update(goodsId, suYuanChengId, pluDTO2.code, pluDTO2.price)

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @DeleteMapping("{suYuanChengId}/{goodsId}")
    fun delete(
        @PathVariable suYuanChengId: UUID,
        @PathVariable goodsId: UUID
    ) {
        pluService.delete(suYuanChengId, goodsId)
    }
}
