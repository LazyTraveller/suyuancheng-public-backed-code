package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.service.TradingItemService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.validation.constraints.Digits

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/trading-item")
class TradingItemController(
    private val tradingItemService: TradingItemService
) {
    data class TradingItemDTO(
        var tradingId: UUID,
        var goodsId: UUID,
        @field:Digits(integer = 12, fraction = 2)
        var weight: Float,
        @field:Digits(integer = 12, fraction = 2)
        var price: Float,
        @field:Digits(integer = 12, fraction = 2)
        val amount: Float
    )

    //@PreAuthorize("hasAuthority('STORE_MANAGER')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) goodsName: String?,
        @RequestParam(required = false) tradingId: UUID?,
        @PageableDefault(
            size = 10,
            sort = ["updateTime"],
            direction = Sort.Direction.ASC
        ) pageable: Pageable
    ) = tradingItemService.query(goodsName, tradingId, pageable)

    //@PreAuthorize("hasAuthority('STORE_MANAGER')")
    /* @PostMapping("/")
     fun create(
         @RequestBody tradingItemDTO: TradingItemDTO
     ) = tradingItemService.create(
         tradingItemDTO.tradingId,
         tradingItemDTO.goodsId,
         tradingItemDTO.weight,
         tradingItemDTO.price,
         tradingItemDTO.amount
     )*/
}