package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.GoodsRepository
import com.suyuancheng.backend.dao.ProviderRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.domain.Goods
import com.suyuancheng.backend.domain.Provider
import com.suyuancheng.backend.domain.Store
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class GoodsService(
    private val providerRepository: ProviderRepository,
    private val storeRepository: StoreRepository,
    private val goodsRepository: GoodsRepository
) {

    fun list(store: Store): List<Goods> {
        val goodsList = arrayListOf<Goods>()
        providerRepository.findByStore(store).forEach {
            goodsList.addAll(goodsRepository.findByProvider(it))
        }
        return goodsList
    }

    fun list(storeId: UUID): List<Goods> {
        val store = storeRepository.findById(storeId)
        if (!store.isPresent) {
            throw ObjectNotFoundException(className = Store::class)
        }
        return list(store.get())
    }

    fun query(
        name: String?,
        fullName: String?,
        sellOut: Boolean?,
        storeId: UUID?,
        before: LocalDate?,
        after: LocalDate?,
        pageable: Pageable
    ) =
        goodsRepository.query(name, fullName, sellOut, storeId, before, after, pageable)


    fun create(
        name: String,
        fullName: String,
        purchaseDate: LocalDate,
        qty: Float,
        unit: String,
        providerId: UUID,
        remark: String?
    ): Goods {
        val provider = providerRepository.findById(providerId)
        if (!provider.isPresent) {
            throw ObjectNotFoundException(className = Provider::class)
        }
        return goodsRepository.save(
            Goods(
                name,
                fullName,
                purchaseDate,
                remark,
                LocalDateTime.now(),
                true,
                qty,
                unit,
                provider.get(),
                UUID.randomUUID()
            )
        )
    }

    fun update(
        uuid: UUID,
        name: String,
        fullName: String,
        purchaseDate: LocalDate,
        sellOut: Boolean,
        qty: Float,
        unit: String,
        providerId: UUID,
        remark: String?
    ): Goods {
        val provider = providerRepository.findById(providerId)
            .orElseThrow(::ObjectNotFoundException)
        return goodsRepository.findById(uuid)
            .orElseThrow(::ObjectNotFoundException).apply {
                this.name = name
                this.fullName = fullName
                this.provider = provider
                this.purchaseDate = purchaseDate
                this.sellOut = sellOut
                this.qty = qty
                this.unit = unit
                this.remark = remark
                this.updateTime = LocalDateTime.now()
            }.let(goodsRepository::save)
    }

    fun delete(uuid: UUID) {
        goodsRepository.findById(uuid).orElseThrow(::ObjectNotFoundException)
        goodsRepository.deleteById(uuid)
    }
}