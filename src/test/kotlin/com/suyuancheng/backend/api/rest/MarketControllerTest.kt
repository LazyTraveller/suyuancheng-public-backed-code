package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.randomMarket
import com.suyuancheng.backend.domain.randomMarketDTO
import com.suyuancheng.backend.service.MarketService
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
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(MarketController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class MarketControllerTest {

    @MockBean
    private lateinit var marketService: MarketService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun list() {
        val market = randomMarket()
        given {
            marketService.list()
        }.willReturn {
            arrayListOf(market)
        }

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/rest/market/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market/list"
                )
            )
    }


    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun query() {
        val market = randomMarket()
        given {
            marketService.query(any(), any())
        }.willReturn {
            PageImpl(
                arrayListOf(market),
                PageRequest.of(1, 10, Sort.Direction.DESC, "updateTime"),
                2
            )
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/market/")
                .param("name", market.name)
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("name").description("Optional"),
                        RequestDocumentation.parameterWithName("page").description("Optional"),
                        RequestDocumentation.parameterWithName("size").description("Optional"),
                        RequestDocumentation.parameterWithName("sort").description("Optional")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun update() {
        val market = randomMarket()
        val marketDTO = randomMarketDTO(market.name, market.address, market.remark)
        given {
            marketService.update(any(), any(), any(), any())
        }.willReturn {
            market
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/market/{uuid}", market.uuid)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(marketDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market/update",
                    pathParameters(
                        parameterWithName("uuid").description("Required")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun create() {
        val market = randomMarket()
        val marketDTO = randomMarketDTO(market.name, market.address, market.remark)
        given {
            marketService.create(any(), any(), any())
        }.willReturn {
            market
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/market/")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(marketDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market/create"
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun delete() {
        val market = randomMarket()

        doNothing().`when`(marketService).delete(market.uuid)

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/rest/market/{uuid}", market.uuid)
                .with(csrf().asHeader())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market/delete",
                    pathParameters(
                        parameterWithName("uuid").description("Required")
                    )
                )
            )
    }
}