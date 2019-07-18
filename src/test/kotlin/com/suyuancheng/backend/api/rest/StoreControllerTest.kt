package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.dao.StoreRepository
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.StoreService
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(StoreController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class StoreControllerTest {

    @MockBean
    private lateinit var authenticationService: AuthenticationService

    @MockBean
    private lateinit var storeService: StoreService

    @MockBean
    private lateinit var storeRepository: StoreRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun query() {
        val market = randomMarket()
        val store = randomStore(market = market)
        given {
            storeService.query(any(), any(), any())
        }.willReturn {
            PageImpl(
                arrayListOf(store),
                PageRequest.of(1, 10, Sort.Direction.DESC, "updateTime"),
                2
            )
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/store/")
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "marketAdmin",
                            "admin",
                            "MarketAdministrator",
                            mutableSetOf(Role.MARKET_MANAGER),
                            market
                        )
                    )
                )
                .param("name", store.name)
                .param("marketId", market.uuid.toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store/query",
                    requestParameters(
                        parameterWithName("name").description("Optional"),
                        parameterWithName("marketId").description("Optional"),
                        parameterWithName("page").description("Optional"),
                        parameterWithName("size").description("Optional"),
                        parameterWithName("sort").description("Optional")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun list() {
        val market = randomMarket()
        val store = randomStore(market = market)
        given {
            storeService.list(UUID.randomUUID())
        }.willReturn {
            arrayListOf(store)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/store/list")
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "marketAdmin",
                            "admin",
                            "MarketAdministrator",
                            mutableSetOf(Role.MARKET_MANAGER),
                            market
                        )
                    )
                )
                .param("marketId", market.uuid.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store/list",
                    requestParameters(
                        parameterWithName("marketId").description("Optional.为市场管理员时隐藏")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun create() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val storeDTO = randomStoreDTO(store.name, store.remark, market.uuid)
        given {
            storeService.create(any(), any(), any())
        }.willReturn {
            store
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/store/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "marketAdmin",
                            "admin",
                            "MarketAdministrator",
                            mutableSetOf(Role.MARKET_MANAGER),
                            market
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(storeDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store/create"
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun update() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val storeDTO = randomStoreDTO(store.name, store.remark, market.uuid)
        given {
            storeService.update(any(), any(), any(), any())
        }.willReturn {
            store
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/store/{uuid}", store.uuid)
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "marketAdmin",
                            "admin",
                            "MarketAdministrator",
                            mutableSetOf(Role.MARKET_MANAGER),
                            market
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(storeDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store/update",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("uuid").description("Required")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["MARKET_MANAGER"])
    fun delete() {
        val market = randomMarket()
        val store = randomStore(market = market)
        doNothing().`when`(authenticationService).authentication(any(), any())
        doNothing().`when`(storeService).delete(store.uuid)

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/rest/store/{uuid}", store.uuid)
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        MarketAdmin(
                            "marketAdmin",
                            "admin",
                            "MarketAdministrator",
                            mutableSetOf(Role.MARKET_MANAGER),
                            market
                        )
                    )
                )
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store/delete",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("uuid").description("Required")
                    )
                )
            )
    }
}