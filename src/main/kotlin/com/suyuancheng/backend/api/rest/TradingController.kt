package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.domain.Trading
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.exception.CanNotNullException
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.TradingService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.Digits

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/trading")
class TradingController(
    private val tradingService: TradingService,
    private val authenticationService: AuthenticationService
) {
    data class TradingDTO(
        val suYuanChengId: UUID,
        val sequence: String,
        @field:Digits(integer = 12, fraction = 2)
        val amount: Float,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        val time: LocalDateTime
    )

    //@PreAuthorize("hasAuthority('STORE_MANAGER')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) storeName: String?,
        @RequestParam(required = false) suYuanChengId: UUID?,
        @RequestParam(required = false) sequence: String?,
        @RequestParam(required = false) storeId: UUID?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) before: LocalDateTime? = LocalDateTime.now().withYear(
            2010
        ),
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) after: LocalDateTime? = LocalDateTime.now(),
        @AuthenticationPrincipal user: User,
        @PageableDefault(
            size = 10,
            sort = ["updateTime"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): Page<Trading> = when (user) {
        is StoreAdmin -> {
            tradingService.query(
                storeName,
                suYuanChengId,
                sequence,
                user.store.uuid,
                before,
                after,
                pageable
            )
        }
        else -> {
            if (storeId == null) {
                throw CanNotNullException("storeId can not be null")
            }
            authenticationService.authenticationStore(user, storeId)
            tradingService.query(
                storeName,
                suYuanChengId,
                sequence,
                storeId,
                before,
                after,
                pageable
            )
        }
    }

    //@PreAuthorize("hasAuthority('STORE_MANAGER')")
    @PostMapping("/")
    fun create(
        @RequestBody tradingDTO: TradingDTO
    ) = tradingService.create(
        tradingDTO.suYuanChengId, tradingDTO.sequence, tradingDTO.amount, tradingDTO.time
    )
}