package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.StoreAdminService
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
@WebMvcTest(StoreAdminController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class StoreAdminControllerTest {

    @MockBean
    private lateinit var storeAdminService: StoreAdminService

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
        val store = randomStore(market = market)
        val storeAdmin = randomStoreAdmins(store = store)
        given {
            storeAdminService.query(any(), any(), any())
        }.willReturn {
            PageImpl(arrayListOf(storeAdmin), PageRequest.of(1, 10, Sort.Direction.DESC, "id"), 2)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/store-admin/")
                .param("realName", storeAdmin.realName)
                .param("storeId", storeAdmin.store.uuid.toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store-admin/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("realName").description("Optional"),
                        RequestDocumentation.parameterWithName("storeId").description("Optional"),
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
        val store = randomStore(market = market)
        val storeAdmin = randomStoreAdmins(store = store)
        val storeAdminDTO2 = randomStoreAdminDTO2()
        given {
            storeAdminService.update(any(), any(), any())
        }.willReturn {
            storeAdmin
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/store-admin/{id}", storeAdmin.id)
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(storeAdminDTO2))

        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store-admin/update",
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
        val store = randomStore(market = market)
        val storeAdmin = randomStoreAdmins(store = store)
        val storeAdminDTO = randomStoreAdminDTO(storeId = store.uuid)
        given {
            storeAdminService.create(any(), any(), any(), any())
        }.willReturn {
            storeAdmin
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/store-admin/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(storeAdminDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store-admin/create"
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["ADMINISTRATOR"])
    fun delete() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val storeAdmin = randomStoreAdmins(store = store)
        doNothing().`when`(storeAdminService).delete(storeAdmin.id)


        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/rest/store-admin/{id}", storeAdmin.id)
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "store-admin/delete",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("Required")
                    )
                )
            )
    }
}