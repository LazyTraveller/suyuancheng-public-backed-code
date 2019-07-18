package com.suyuancheng.backend.service

import com.suyuancheng.backend.dao.ProviderRepository
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.domain.Provider
import com.suyuancheng.backend.domain.Store
import com.suyuancheng.backend.exception.ObjectNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * @author hsj
 */
@Service
class ProviderService(
    private val providerRepository: ProviderRepository,
    private val storeRepository: StoreRepository
) {


    fun list(store: Store): List<Provider> {
        return providerRepository.findByStore(store)
    }

    fun list(storeId: UUID): List<Provider> {
        val store = storeRepository.findById(storeId)
        if (!store.isPresent) {
            throw ObjectNotFoundException(className = Store::class)
        }
        return list(store.get())
    }

    fun query(name: String?, storeId: UUID?, pageable: Pageable) =
        providerRepository.query(name, storeId, pageable)

    fun create(name: String, storeId: UUID, remark: String?): Provider {
        val store = storeRepository.findById(storeId)
        if (!store.isPresent) {
            throw ObjectNotFoundException(className = Store::class)
        }
        return providerRepository.save(
            Provider(name, remark, LocalDateTime.now(), store.get(), UUID.randomUUID())
        )
    }

    fun update(uuid: UUID, name: String, remark: String?): Provider {
        return providerRepository.findById(uuid).orElseThrow(::ObjectNotFoundException).apply {
            this.name = name
            this.remark = remark
            this.updateTime = LocalDateTime.now()
        }.let(providerRepository::save)
    }

    fun delete(uuid: UUID) {
        providerRepository.findById(uuid).orElseThrow(::ObjectNotFoundException)
        providerRepository.deleteById(uuid)
    }
}