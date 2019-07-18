package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.domain.randomUser
import com.suyuancheng.backend.domain.randomUserDTO
import com.suyuancheng.backend.service.UserService
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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(UserController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class UserControllerTest {

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userController: UserController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser
    fun info() {

        val user = randomUser()
        given {
            userController.getUserInfo(any())
        } willReturn {
            user
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/user/user-info")
                .with(
                    SecurityMockMvcRequestPostProcessors.user(
                        User(
                            "admin",
                            "admin",
                            "MarketAdministrator",
                            mutableSetOf(Role.ADMINISTRATOR)
                        )
                    )
                )

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "user/info"
                )
            )
    }
    @Test
    @WithMockUser
    fun query() {

        val user = randomUser()
        given {
            userService.query(any(), any())
        }.willReturn {
            PageImpl(arrayListOf(user), PageRequest.of(1, 10, Sort.Direction.DESC, "id"), 2)
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/user/")
                .param("realName", user.realName)
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "user/query",
                    requestParameters(
                        parameterWithName("realName").description("Optional"),
                        parameterWithName("page").description("Optional"),
                        parameterWithName("size").description("Optional"),
                        parameterWithName("sort").description("Optional")
                    )
                )
            )
    }

    @Test
    @WithMockUser
    fun update() {
        val user = randomUser(id = 5)
        val roles = mutableListOf(Role.STORE_MANAGER, Role.MARKET_MANAGER, Role.ADMINISTRATOR)
        given {
            userService.update(any(), any())
        }.willReturn {
            user
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/rest/user/{id}", user.id)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(roles))

        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "user/update",
                    pathParameters(
                        parameterWithName("id").description("Required")
                    )
                )
            )
    }

    @Test
    @WithMockUser
    fun create() {
        val roles: MutableSet<Role> = mutableSetOf()
        val user = randomUser(id = 5, roles = roles)
        val userDTO = randomUserDTO(
            user.username,
            user.password,
            user.realName
        )
        given {
            userService.create(any(), any(), any())
        }.willReturn {
            user
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/user/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(userDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "user/create"
                )
            )
    }

}

