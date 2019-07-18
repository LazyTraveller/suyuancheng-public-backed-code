package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.TradingItemRepository
import com.suyuancheng.backend.dao.TradingRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class EChartsService(
    private val tradingRepository: TradingRepository,
    private val tradingItemRepository: TradingItemRepository,
    private val goodsService: GoodsService,
    private val storeService: StoreService,
    private val marketService: MarketService
) {

    fun goodsSales(
        storeId: UUID,
        before: LocalDateTime?,
        after: LocalDateTime?
    ): Map<String, Float> {
        val goodsSaleMap = mutableMapOf<String, Float>()
        val page = tradingRepository.query(
            null,
            null,
            null,
            storeId,
            before,
            after,
            Pageable.unpaged()
        )
        // All goods
        goodsService.list(storeId).forEach {
            goodsSaleMap[it.name] = 0f
        }
        page.content.forEach { trading ->
            tradingItemRepository.findByTrading(trading).forEach { tradingItem ->
                goodsSaleMap[tradingItem.goods.name] = goodsSaleMap[tradingItem.goods.name]!!.run {
                    var amount = this
                    amount += tradingItem.amount
                    amount
                }
            }
        }
        //goods with trading information
        /* page.content.forEach { trading ->
        tradingItemRepository.findByTrading(trading).forEach { tradingItem ->
            goodsSaleMap[tradingItem.goods.name] = goodsSaleMap[tradingItem.goods.name]?.run {
                var amount = this
                amount += tradingItem.amount
                amount
            } ?: tradingItem.amount
        }
    }*/
        return goodsSaleMap
    }

    fun storeSales(
        storeId: UUID,
        before: LocalDateTime?,
        after: LocalDateTime?
    ): Float {
        var amount = 0.0f
        val page = tradingRepository.query(
            null,
            null,
            null,
            storeId,
            before,
            after,
            Pageable.unpaged()
        )
        page.content.forEach {
            amount += it.amount
        }
        return amount.setScale(2)
    }

    fun marketSales(
        marketId: UUID,
        before: LocalDateTime?,
        after: LocalDateTime?
    ): Map<String, Float> {
        val storeSaleMap = mutableMapOf<String, Float>()
        val storeList = storeService.list(marketId)
        storeList.forEach {
            storeSaleMap[it.name] = storeSales(it.uuid, before, after)
        }
        return storeSaleMap
    }

    fun sales(
        before: LocalDateTime?,
        after: LocalDateTime?
    ): Map<String, Float> {
        val marketSaleMap = mutableMapOf<String, Float>()
        val marketList = marketService.list()
        marketList.forEach { market ->
            var amount = 0.0f
            marketSales(market.uuid, before, after).forEach {
                amount += it.value
            }
            marketSaleMap[market.name] = amount
        }
        return marketSaleMap
    }


    fun Float.setScale(newScale: Int): Float =
        this.toBigDecimal().setScale(newScale, BigDecimal.ROUND_HALF_UP).toFloat()

}