package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.PLURepository
import com.suyuancheng.backend.dao.SuYuanChengRepository
import com.suyuancheng.backend.dao.TradingItemRepository
import com.suyuancheng.backend.dao.TradingRepository
import com.suyuancheng.backend.domain.ReTradingInfo
import com.suyuancheng.backend.domain.TradingInfo
import com.suyuancheng.backend.exception.ObjectAlreadyExistException
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * @author hsj
 */
@Service
class TradingInfoService(
    private val tradingService: TradingService,
    private val tradingItemService: TradingItemService,
    private val pluRepository: PLURepository,
    private val tradingRepository: TradingRepository,
    private val tradingItemRepository: TradingItemRepository,
    private val suYuanChengRepository: SuYuanChengRepository
) {

    fun push(tradingInfo: TradingInfo) {
        val suYuanCheng =
            suYuanChengRepository.findByMachineId(tradingInfo.Main.MachineId)
                .orElseThrow(::ObjectNotFoundException)
        if (tradingRepository.findBySuYuanChengAndSequence(
                suYuanCheng,
                tradingInfo.Main.TransNo
            ).isPresent
        ) {
            throw ObjectAlreadyExistException("Object already exist")
        }

        val trading = tradingService.create(
            suYuanCheng.uuid,
            tradingInfo.Main.TransNo,
            tradingInfo.Main.Subtotal,
            tradingInfo.Main.TransDate
        )
        val pluList = pluRepository.findByIdSuYuanChengId(suYuanCheng.uuid)
        tradingInfo.Detail.forEachIndexed { _, it ->
            val plu = pluList.run {
                this.first { plu ->
                    plu.code == it.PLU
                }
            }
            val goods = plu.goods
            tradingItemService.create(
                trading.uuid,
                goods.uuid,
                it.Qty,
                it.Unit,
                it.PriceIncTax,
                it.Qty * it.PriceIncTax
            )
        }
    }

    fun pull(MachineId: String, TransNo: String): ReTradingInfo {
        val suYuanCheng =
            suYuanChengRepository.findByMachineId(MachineId)
                .orElseThrow(::ObjectNotFoundException)
        val trading =
            tradingRepository.findBySuYuanChengAndSequence(suYuanCheng, TransNo)
                .orElseThrow(::ObjectNotFoundException)
        val reTradingInfo = ReTradingInfo(
            ReTradingInfo.MainClass(
                trading.sequence,
                trading.amount,
                trading.time,
                trading.suYuanCheng.store.market.name,
                trading.suYuanCheng.store.name
            ), mutableListOf()
        )
        tradingItemRepository.query(
            null,
            trading.uuid,
            PageRequest.of(0, 1000, Sort.Direction.ASC, "updateTime")
        ).forEachIndexed { index, tradingItem ->
            val detail = ReTradingInfo.DetailClass(
                tradingItem.goods.name,
                tradingItem.goods.provider.name,
                tradingItem.weight,
                tradingItem.unit,
                tradingItem.price,
                tradingItem.amount,
                tradingItem.goods.purchaseDate
            )
            reTradingInfo.Detail.add(index, detail)
        }
        return reTradingInfo
    }
}