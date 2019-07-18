package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.domain.StoreAdmin
import com.suyuancheng.backend.domain.randomMarket
import com.suyuancheng.backend.domain.randomStore
import com.suyuancheng.backend.service.EChartsService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(EChartsController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class EChartsControllerTest {
    @MockBean
    private lateinit var eChartsService: EChartsService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser
    fun sales() {
        val market = randomMarket()
        val store = randomStore(market = market)

        given {
            eChartsService.goodsSales(any(), any(), any())
        } willReturn {
            mapOf("大润发" to 88.3f)
        }
        given {
            eChartsService.sales(any(), any())
        } willReturn {
            mapOf("大润发" to 88.3f)
        }
        given {
            eChartsService.marketSales(any(), any(), any())
        }.willReturn {
            mapOf("百草园" to 88.3f)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/e-charts/")
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        StoreAdmin(
                            "StoreAdmin",
                            "admin",
                            "StoreAdministrator",
                            mutableSetOf(Role.STORE_MANAGER),
                            store
                        )
                    )
                )
                .param("after", LocalDateTime.now().toString())
                .param("before", LocalDateTime.now().toString())

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "e-charts/sales",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("before").description("Optional.开始时间，默认值为2010年的本月本日 格式: yyyy-MM-dd'T'HH:mm:ss"),
                        RequestDocumentation.parameterWithName("after").description("Optional.结束时间，默认值为当前时间 格式: yyyy-MM-dd'T'HH:mm:ss")
                    )
                )
            )
    }

    @Test
    @WithMockUser
    fun storeSales() {
        val market = randomMarket()
        val store = randomStore(market = market)
        given {
            eChartsService.storeSales(any(), any(), any())
        } willReturn {
            88.3f
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/e-charts/store")
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        StoreAdmin(
                            "StoreAdmin",
                            "admin",
                            "StoreAdministrator",
                            mutableSetOf(Role.STORE_MANAGER),
                            store
                        )
                    )
                )
                .param("after", LocalDateTime.now().toString())
                .param("before", LocalDateTime.now().toString())

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "e-charts/store-sales",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("before").description("Optional.开始时间，默认值为2010年的本月本日 格式: yyyy-MM-dd'T'HH:mm:ss"),
                        RequestDocumentation.parameterWithName("after").description("Optional.结束时间，默认值为当前时间 格式: yyyy-MM-dd'T'HH:mm:ss")
                    )
                )
            )
    }
}