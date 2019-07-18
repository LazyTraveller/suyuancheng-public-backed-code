package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.dao.SuYuanChengRepository
import com.suyuancheng.backend.domain.SuYuanCheng
import com.suyuancheng.backend.exception.ObjectAlreadyExistException
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class SuYuanChengService(
    private val suYuanChengRepository: SuYuanChengRepository,
    private val storeRepository: StoreRepository
) {

    fun query(
        machineId: String?,
        storeId: UUID?,
        pageable: Pageable
    ) = suYuanChengRepository.query(machineId, storeId, pageable)

    fun create(machineId: String, storeId: UUID): SuYuanCheng {
        val store = storeRepository.findById(storeId)
            .orElseThrow(::ObjectNotFoundException)
        if (suYuanChengRepository.findByMachineId(machineId).isPresent) {
            throw ObjectAlreadyExistException("Object already exist")
        }
        return suYuanChengRepository.save(
            SuYuanCheng(
                store,
                machineId,
                LocalDateTime.now(),
                UUID.randomUUID()
            )
        )
    }

    fun update(uuid: UUID, storeId: UUID, machineId: String): SuYuanCheng {
        val store = storeRepository.findById(storeId)
            .orElseThrow(::ObjectNotFoundException)
        val suYuanCheng = suYuanChengRepository.findById(uuid)
            .orElseThrow(::ObjectNotFoundException)
        if (suYuanChengRepository.findByMachineId(machineId).isPresent && machineId != suYuanCheng.machineId) {
            throw ObjectAlreadyExistException("Object already exist")
        }
        suYuanCheng.machineId = machineId
        suYuanCheng.store = store
        return suYuanChengRepository.save(suYuanCheng)
    }

    fun delete(uuid: UUID) {
        suYuanChengRepository.findById(uuid).orElseThrow(::ObjectNotFoundException)
        suYuanChengRepository.deleteById(uuid)
    }
}