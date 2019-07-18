package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.ProviderService
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
@WebMvcTest(ProviderController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class ProviderControllerTest {

    @MockBean
    private lateinit var authenticationService: AuthenticationService

    @MockBean
    private lateinit var providerService: ProviderService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser
    fun list() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        given {
            providerService.list(store)
        }.willReturn {
            arrayListOf(provider)
        }
        given {
            providerService.list(store.uuid)
        }.willReturn {
            arrayListOf(provider)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/provider/list")
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
                .param("storeId", provider.store.uuid.toString())

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "provider/list",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("storeId").description("Optional.店铺管理员隐藏该字段")
                    )
                )
            )
    }

    @Test
    @WithMockUser
    fun query() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        given {
            providerService.query(any(), any(), any())
        }.willReturn {
            PageImpl(
                arrayListOf(provider),
                PageRequest.of(1, 10, Sort.Direction.DESC, "updateTime"),
                2
            )
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/provider/")
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
                .param("name", provider.name)
                .param("storeId", provider.store.uuid.toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "provider/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("name").description("Optional"),
                        RequestDocumentation.parameterWithName("storeId").description("Optional.店铺管理员隐藏该字段"),
                        RequestDocumentation.parameterWithName("page").description("Optional"),
                        RequestDocumentation.parameterWithName("size").description("Optional"),
                        RequestDocumentation.parameterWithName("sort").description("Optional")
                    )
                )
            )
    }

    @Test
    @WithMockUser
    fun update() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val providerDTO = randomProviderDTO(provider.name, provider.remark, storeId = store.uuid)
        given {
            providerService.update(any(), any(), any())
        }.willReturn {
            provider
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/provider/{uuid}", provider.uuid)
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
                .content(mapper.writeValueAsString(providerDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "provider/update",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("uuid").description("Required")
                    )
                )
            )
    }

    @Test
    @WithMockUser
    fun create() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val providerDTO = randomProviderDTO(provider.name, provider.remark, storeId = store.uuid)
        given {
            providerService.create(any(), any(), any())
        }.willReturn {
            provider
        }
        doNothing().`when`(authenticationService).authentication(any(), any())

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/provider/")
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
                .content(mapper.writeValueAsString(providerDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "provider/create"
                )
            )
    }

    @Test
    @WithMockUser
    fun delete() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)

        doNothing().`when`(providerService).delete(provider.uuid)
        doNothing().`when`(authenticationService).authentication(any(), any())

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/rest/provider/{uuid}", provider.uuid)
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
                    "provider/delete",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("uuid").description("Required")
                    )
                )
            )
    }
}