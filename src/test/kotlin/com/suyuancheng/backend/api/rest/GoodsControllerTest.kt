package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.GoodsService
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
import java.time.LocalDate

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(GoodsController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class GoodsControllerTest {

    @MockBean
    private lateinit var authenticationService: AuthenticationService

    @MockBean
    private lateinit var goodsService: GoodsService

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
        val goods = randomGoods(provider = provider)
        given {
            goodsService.list(store)
        }.willReturn {
            arrayListOf(goods)
        }
        given {
            goodsService.list(store.uuid)
        }.willReturn {
            arrayListOf(goods)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/goods/list")
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
                .param("storeId", goods.provider.store.uuid.toString())

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "goods/list",
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
        val goods = randomGoods(provider = provider)
        given {
            goodsService.query(any(), any(), any(), any(), any(), any(), any())
        }.willReturn {
            PageImpl(
                arrayListOf(goods),
                PageRequest.of(1, 10, Sort.Direction.DESC, "purchaseDate"),
                2
            )
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/goods/")
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
                .param("name", store.name)
                .param("fullName", goods.fullName)
                .param("sellOut", goods.sellOut.toString())
                .param("storeId", store.uuid.toString())
                .param("before", LocalDate.now().toString())
                .param("after", LocalDate.now().toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "goods/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("name").description("Optional"),
                        RequestDocumentation.parameterWithName("fullName").description("Optional"),
                        RequestDocumentation.parameterWithName("sellOut").description("Optional"),
                        RequestDocumentation.parameterWithName("storeId").description("Optional.店铺管理员不使用"),
                        RequestDocumentation.parameterWithName("before").description("Optional.开始时间，默认值为2010年的本月本日 格式:yyyy-MM-dd"),
                        RequestDocumentation.parameterWithName("after").description("Optional.结束时间，默认值为当前时间 格式:yyyy-MM-dd"),
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
        val goods = randomGoods(provider = provider)
        val goodsDTO = randomGoodsDTOs(
            goods.name,
            goods.fullName,
            goods.purchaseDate,
            goods.remark,
            goods.sellOut,
            goods.qty,
            goods.unit,
            provider.uuid
        )
        given {
            goodsService.update(any(), any(), any(), any(), any(), any(), any(), any(), any())
        }.willReturn {
            goods
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/goods/{uuid}", goods.uuid)
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
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(goodsDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "goods/update",
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
        val goods = randomGoods(provider = provider)
        val goodsDTO = randomGoodsDTOs(
            goods.name,
            goods.fullName,
            goods.purchaseDate,
            goods.remark,
            goods.sellOut,
            goods.qty,
            goods.unit,
            provider.uuid
        )
        given {
            goodsService.create(any(), any(), any(), any(), any(), any(), any())
        }.willReturn {
            goods
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/goods/")
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
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(goodsDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "goods/create"
                )
            )
    }

    @Test
    @WithMockUser
    fun delete() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)

        doNothing().`when`(goodsService).delete(any())

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/rest/goods/{uuid}", goods.uuid)
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
                    "goods/delete",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("uuid").description("Required")
                    )
                )
            )
    }
}