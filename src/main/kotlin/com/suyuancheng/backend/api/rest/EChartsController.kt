package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.MarketAdmin
import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.ASDException
import com.suyuancheng.backend.service.EChartsService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

/**
 * @author hsj
 */

@RestController
@RequestMapping("api/rest/e-charts")
class EChartsController(
    private val eChartsService: EChartsService
) {
    @PreAuthorize("hasAnyAuthority('STORE_MANAGER', 'ADMINISTRATOR', 'MARKET_MANAGER')")
    @GetMapping("/")
    fun sales(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) before: LocalDateTime? = LocalDateTime.now().withYear(
            2010
        ),
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) after: LocalDateTime? = LocalDateTime.now(),
        @AuthenticationPrincipal user: User
    ): Map<String, Float> = when (user) {
        is StoreAdmin -> eChartsService.goodsSales(user.store.uuid, before, after)
        is MarketAdmin -> eChartsService.marketSales(user.market.uuid, before, after)
        else -> eChartsService.sales(before, after)
    }

    @PreAuthorize("hasAnyAuthority('STORE_MANAGER')")
    @GetMapping("/store")
    fun storeSales(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) before: LocalDateTime? = LocalDateTime.now().withYear(
            2010
        ),
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) after: LocalDateTime? = LocalDateTime.now(),
        @AuthenticationPrincipal user: User
    ): Float = when (user) {
        is StoreAdmin -> eChartsService.storeSales(user.store.uuid, before, after)
        else -> {
            throw ASDException()
        }
    }
}