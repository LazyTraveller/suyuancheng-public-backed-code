package com.suyuancheng.backend.service

import com.suyuancheng.backend.api.rest.PLUController
import com.suyuancheng.backend.dao.GoodsRepository
import com.suyuancheng.backend.dao.PLURepository
import com.suyuancheng.backend.dao.SuYuanChengRepository
import com.suyuancheng.backend.domain.PLU
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class PLUService(
    private val pluRepository: PLURepository,
    private val suYuanChengRepository: SuYuanChengRepository,
    private val goodsRepository: GoodsRepository
) {

    fun query(goodsName: String?, suYuanChengId: UUID, code: Int?, pageable: Pageable): Page<PLU> =
        pluRepository.query(goodsName, suYuanChengId, code, pageable)

    fun listBySuYuanCheng(machineId: String): List<PLU>{
        val suYuanCheng =suYuanChengRepository.findByMachineId(machineId).orElseThrow {
            throw ObjectNotFoundException("machineId not found")
        }
        return pluRepository.findByIdSuYuanChengId(suYuanCheng.uuid)
    }

    fun create(pluDTOs: List<PLUController.PluDTO>): MutableList<PLU> {
        if (pluDTOs.isEmpty()) {
            throw ObjectNotFoundException("list isEmpty")
        }
        val suYuanCheng =
            suYuanChengRepository.findById(pluDTOs[0].suYuanChengId)
                .orElseThrow(::ObjectNotFoundException)
        val pluList: MutableList<PLU> = mutableListOf()
        pluDTOs.forEach {
            val goods = goodsRepository.findById(it.goodsId).orElseThrow(::ObjectNotFoundException)
            val plu = pluRepository.findByCode(it.code)
            if (plu.isPresent) {
                with(plu.get()) {
                    code = it.code
                    this.goods = goods
                    price = it.price
                    pluList.add(this)
                }
            } else {
                pluList.add(
                    PLU(
                        it.code,
                        it.price,
                        LocalDateTime.now(),
                        goods,
                        suYuanCheng,
                        PLU.PLUId(suYuanCheng.uuid, goods.uuid)
                    )
                )
            }
        }

        return pluRepository.saveAll(pluList)
    }

    fun update(goodsId: UUID, suYuanChengId: UUID, code: Int, price: Float): PLU {
        return pluRepository.findById(PLU.PLUId(suYuanChengId, goodsId))
            .orElseThrow(::ObjectNotFoundException).apply {
                this.code = code
                this.price = price
                this.updateTime = LocalDateTime.now()
            }.let(pluRepository::save)
    }

    fun delete(suYuanChengId: UUID, storeId: UUID) {
        pluRepository.findById(PLU.PLUId(suYuanChengId, storeId))
            .orElseThrow(::ObjectNotFoundException)
        pluRepository.deleteById(PLU.PLUId(suYuanChengId, storeId))
    }
}