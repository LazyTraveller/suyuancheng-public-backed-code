package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.Goods
import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.CanNotNullException
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.GoodsService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/goods")
class GoodsController(
    private val goodsService: GoodsService,
    private val authenticationService: AuthenticationService
) {
    data class GoodsDTO(
        val name: String,
        val fullName: String,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        val purchaseDate: LocalDate,
        val sellOut: Boolean,
        val remark: String?,
        val qty: Float,
        val unit: String,
        val providerId: UUID
    )

    @PreAuthorize("hasAnyAuthority('STORE_MANAGER', 'ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/list")
    fun list(
        @RequestParam(required = false) storeId: UUID?,
        @AuthenticationPrincipal user: User
    ): List<Goods> = when (user) {
        is StoreAdmin -> {
            goodsService.list(user.store)
        }
        else -> {
            if (storeId == null) {
                throw CanNotNullException("storeId can not be null")
            }
            authenticationService.authenticationStore(user, storeId)
            goodsService.list(storeId)
        }
    }

    @PreAuthorize("hasAnyAuthority('STORE_MANAGER', 'ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) fullName: String?,
        @RequestParam(required = false) sellOut: Boolean?,
        @RequestParam(required = false) storeId: UUID?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) before: LocalDate? = LocalDate.now().withYear(
            2010
        ),
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) after: LocalDate? = LocalDate.now(),
        @AuthenticationPrincipal user: User,
        @PageableDefault(
            size = 10,
            sort = ["purchaseDate"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ) = when (user) {
        is StoreAdmin -> goodsService.query(
            name,
            fullName,
            sellOut,
            user.store.uuid,
            before,
            after,
            pageable
        )
        else -> {
            if (storeId == null) {
                throw CanNotNullException("storeId can not be null")
            }
            authenticationService.authenticationStore(user, storeId)
            goodsService.query(name, fullName, sellOut, storeId, before, after, pageable)
        }
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PutMapping("{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody goodsDTO: GoodsDTO,
        @AuthenticationPrincipal user: User
    ): Goods {
        authenticationService.authenticationProvider(user, goodsDTO.providerId)
        return goodsService.update(
            uuid,
            goodsDTO.name,
            goodsDTO.fullName,
            goodsDTO.purchaseDate,
            goodsDTO.sellOut,
            goodsDTO.qty,
            goodsDTO.unit,
            goodsDTO.providerId,
            goodsDTO.remark
        )
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PostMapping("/")
    fun create(
        @RequestBody goodsDTO: GoodsDTO,
        @AuthenticationPrincipal user: User
    ): Goods {
        authenticationService.authenticationProvider(user, goodsDTO.providerId)
        return goodsService.create(
            goodsDTO.name,
            goodsDTO.fullName,
            goodsDTO.purchaseDate,
            goodsDTO.qty,
            goodsDTO.unit,
            goodsDTO.providerId,
            goodsDTO.remark
        )
    }

    @PreAuthorize("hasAuthority('STORE_MANAGER')")
    @DeleteMapping("{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
        @AuthenticationPrincipal user: User
    ) {
        authenticationService.authenticationGoods(user, uuid)
        goodsService.delete(uuid)
    }
}