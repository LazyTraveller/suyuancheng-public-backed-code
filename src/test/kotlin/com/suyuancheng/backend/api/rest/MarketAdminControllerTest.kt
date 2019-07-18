package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.randomMarket
import com.suyuancheng.backend.domain.randomMarketAdmin
import com.suyuancheng.backend.domain.randomMarketAdminDTO
import com.suyuancheng.backend.domain.randomMarketAdminDTO2
import com.suyuancheng.backend.service.MarketAdminService
import org.junit.Before
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(MarketAdminController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class MarketAdminControllerTest {

    @MockBean
    private lateinit var marketAdminService: MarketAdminService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Before
    fun setup() {
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun query() {
        val market = randomMarket()
        val marketAdmin = randomMarketAdmin(market = market)
        given {
            marketAdminService.query(any(), any(), any())
        } willReturn {
            PageImpl(arrayListOf(marketAdmin), PageRequest.of(1, 10, Sort.Direction.DESC, "id"), 2)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/market-admin/")
                .param("realName", marketAdmin.realName)
                .param("marketId", marketAdmin.market.uuid.toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market-admin/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("realName").description("Optional"),
                        RequestDocumentation.parameterWithName("marketId").description("Optional"),
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
        val marketAdmin = randomMarketAdmin(market = market)
        val marketAdminDTO2 = randomMarketAdminDTO2()
        given {
            marketAdminService.update(any(), any(), any())
        }.willReturn {
            marketAdmin
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/market-admin/{id}", marketAdmin.id)
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(marketAdminDTO2))

        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market-admin/update",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("Required")
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun create() {
        val market = randomMarket()
        val marketAdmin = randomMarketAdmin(market = market)
        val marketAdminDTO2 = randomMarketAdminDTO(marketId = market.uuid)
        given {
            marketAdminService.create(any(), any(), any(), any())
        }.willReturn {
            marketAdmin
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/market-admin/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(marketAdminDTO2))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market-admin/create"
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun delete() {
        val market = randomMarket()
        val marketAdmin = randomMarketAdmin(market = market)
        doNothing().`when`(marketAdminService).delete(marketAdmin.id)


        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/rest/market-admin/{id}", marketAdmin.id)
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "market-admin/delete",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("Required")
                    )
                )
            )
    }
}