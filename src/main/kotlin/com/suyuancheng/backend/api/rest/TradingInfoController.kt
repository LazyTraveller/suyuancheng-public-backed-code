package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.ReTradingInfo
import com.suyuancheng.backend.domain.TradingInfo
import com.suyuancheng.backend.service.TradingInfoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/trading-info")
class TradingInfoController(
    private val tradingInfoService: TradingInfoService
) {
    @GetMapping("/")
    fun pull(
        @RequestParam TransNo: String,
        @RequestParam MachineId: String
    ): ReTradingInfo = tradingInfoService.pull(MachineId, TransNo)

    @PostMapping("/")
    fun push(@RequestBody tradingInfo: TradingInfo, response: HttpServletResponse): HttpStatus {
        tradingInfoService.push(tradingInfo)
        return HttpStatus.OK
    }
}