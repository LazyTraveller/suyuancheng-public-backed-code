package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.SuYuanChengService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(SuYuanChengController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class SuYuanChengControllerTest {

    @MockBean
    private lateinit var suYuanChengService: SuYuanChengService

    @MockBean
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser
    fun query() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val suYuanCheng = randomSuYuanCheng(store)
        given {
            suYuanChengService.query(any(), any(), any())
        }.willReturn {
            PageImpl(
                arrayListOf(suYuanCheng),
                PageRequest.of(1, 10, Sort.Direction.DESC, "updateTime"),
                2
            )
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/su-yuan-cheng/")
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "storeAdmin",
                            "admin",
                            "StoreAdministrator",
                            mutableSetOf(Role.STORE_MANAGER),
                            market
                        )
                    )
                )
                .param("machineId", suYuanCheng.machineId)
                .param("storeId", store.uuid.toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "su-yuan-cheng/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("machineId").description("Optional.溯源秤机号"),
                        RequestDocumentation.parameterWithName("storeId").description("Optional.店铺管理员隐藏"),
                        RequestDocumentation.parameterWithName("page").description("Optional"),
                        RequestDocumentation.parameterWithName("size").description("Optional"),
                        RequestDocumentation.parameterWithName("sort").description("Optional")
                    )
                )
            )
    }
    @Test
    @WithMockUser
    fun create() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val suYuanCheng = randomSuYuanCheng(store)
        val suYuanChengDTO = randomSuYuanChengDTO(storeId = suYuanCheng.store.uuid)
        doNothing().`when`(authenticationService).authentication(any(), any())
        given {
            suYuanChengService.create(any(), any())
        }.willReturn {
            suYuanCheng
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/su-yuan-cheng/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "storeAdmin",
                            "admin",
                            "StoreAdministrator",
                            mutableSetOf(Role.STORE_MANAGER),
                            market
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(suYuanChengDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "su-yuan-cheng/create"
                )
            )
    }

    @Test
    @WithMockUser
    fun update() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val suYuanCheng = randomSuYuanCheng(store)
        val suYuanChengDTO = randomSuYuanChengDTO(storeId = suYuanCheng.store.uuid)
        doNothing().`when`(authenticationService).authentication(any(), any())
        given {
            suYuanChengService.update(any(), any(), any())
        }.willReturn {
            suYuanCheng
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/su-yuan-cheng/{uuid}", suYuanCheng.uuid)
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "storeAdmin",
                            "admin",
                            "StoreAdministrator",
                            mutableSetOf(Role.STORE_MANAGER),
                            market
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(suYuanChengDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "su-yuan-cheng/update"
                )
            )
    }

    @Test
    @WithMockUser
    fun delete() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val suYuanCheng = randomSuYuanCheng(store)
        doNothing().`when`(suYuanChengService).delete(suYuanCheng.uuid)
        doNothing().`when`(authenticationService).authentication(any(), any())

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(
                "/api/rest/su-yuan-cheng/{uuid}",
                suYuanCheng.uuid
            )
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "storeAdmin",
                            "admin",
                            "StoreAdministrator",
                            mutableSetOf(Role.STORE_MANAGER),
                            market
                        )
                    )
                )
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "su-yuan-cheng/delete",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("uuid").description("Required")
                    )
                )
            )
    }
}