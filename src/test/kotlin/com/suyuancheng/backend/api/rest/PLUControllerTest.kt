package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.PLUService
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
@WebMvcTest(PLUController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class PLUControllerTest {

    @MockBean
    private lateinit var pluService: PLUService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun query() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val suYuanCheng = randomSuYuanCheng(store)
        val plu = randomPLU(
            id = PLU.PLUId(suYuanCheng.uuid, goods.uuid),
            goods = goods,
            suYuanCheng = suYuanCheng
        )
        given {
            pluService.query(any(), any(), any(), any())
        }.willReturn {
            PageImpl(arrayListOf(plu), PageRequest.of(1, 10, Sort.Direction.ASC, "code"), 2)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/plu/")
                .param("goodsName", goods.name)
                .param("suYuanChengId", suYuanCheng.uuid.toString())
                .param("code", plu.code.toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "plu/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("goodsName").description("Optional."),
                        RequestDocumentation.parameterWithName("suYuanChengId").description("Required."),
                        RequestDocumentation.parameterWithName("code").description("Optional. min= 1, max = 81"),
                        RequestDocumentation.parameterWithName("page").description("Optional"),
                        RequestDocumentation.parameterWithName("size").description("Optional"),
                        RequestDocumentation.parameterWithName("sort").description("Optional")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun list() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val suYuanCheng = randomSuYuanCheng(store)
        val plu = randomPLU(
            id = PLU.PLUId(suYuanCheng.uuid, goods.uuid),
            goods = goods,
            suYuanCheng = suYuanCheng
        )
        given {
            pluService.listBySuYuanCheng(any())
        }.willReturn {
            arrayListOf(plu)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/plu/list")
                .param("machineId", suYuanCheng.machineId)

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "plu/list",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("machineId").description("Require.")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["STORE_MANAGER"])
    fun create() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val suYuanCheng = randomSuYuanCheng(store)
        val plu = randomPLU(
            id = PLU.PLUId(suYuanCheng.uuid, goods.uuid),
            goods = goods,
            suYuanCheng = suYuanCheng
        )
        val pluDTO = randomePluDTO(
            suYuanChengId = suYuanCheng.uuid,
            goodsId = goods.uuid,
            code = plu.code,
            price = plu.price
        )

        given {
            pluService.create(any())
        }.willReturn {
            arrayListOf(plu)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/plu/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(arrayListOf(pluDTO)))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "plu/create"
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["STORE_MANAGER"])
    fun update() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val suYuanCheng = randomSuYuanCheng(store)
        val plu = randomPLU(
            id = PLU.PLUId(suYuanCheng.uuid, goods.uuid),
            goods = goods,
            suYuanCheng = suYuanCheng
        )
        val pluDTO2 = randomePluDTO2(
            code = plu.code,
            price = plu.price
        )

        given {
            pluService.update(any(), any(), any(), any())
        }.willReturn {
            plu
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put(
                "/api/rest/plu/{suYuanChengId}/{goodsId}",
                suYuanCheng.uuid,
                goods.uuid
            )
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(pluDTO2))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "plu/update",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("suYuanChengId").description("Required"),
                        RequestDocumentation.parameterWithName("goodsId").description("Required")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["STORE_MANAGER"])
    fun delete() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val suYuanCheng = randomSuYuanCheng(store)

        doNothing().`when`(pluService).delete(any(), any())

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete(
                "/api/rest/plu/{suYuanChengId}/{goodsId}",
                suYuanCheng.uuid,
                goods.uuid
            )
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "plu/delete",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("suYuanChengId").description("Required"),
                        RequestDocumentation.parameterWithName("goodsId").description("Required")
                    )
                )
            )
    }
}