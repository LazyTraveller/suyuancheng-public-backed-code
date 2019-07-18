package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.AuthenticationService
import com.suyuancheng.backend.service.TradingService
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
import java.time.LocalDateTime

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(TradingController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class TradingControllerTest {

    @MockBean
    private lateinit var tradingService: TradingService

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
        val trading = randomTrading(suYuanCheng = suYuanCheng)
        given {
            tradingService.query(any(), any(), any(), any(), any(), any(), any())
        }.willReturn {
            PageImpl(
                arrayListOf(trading),
                PageRequest.of(1, 10, Sort.Direction.DESC, "updateTime"),
                2
            )
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/trading/")
                .param("storeName", trading.suYuanCheng.store.name)
                .param("suYuanChengId", trading.suYuanCheng.uuid.toString())
                .param("sequence", trading.sequence)
                .param("storeId", store.uuid.toString())
                .param("after", LocalDateTime.now().toString())
                .param("before", LocalDateTime.now().toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")
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
                    "trading/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("storeName").description("Optional"),
                        RequestDocumentation.parameterWithName("suYuanChengId").description("Optional"),
                        RequestDocumentation.parameterWithName("storeId").description("Optional.店铺管理员隐藏"),
                        RequestDocumentation.parameterWithName("sequence").description("Optional.交易流水号"),
                        RequestDocumentation.parameterWithName("before").description("Optional.开始时间，默认值为2010年的本月本日 格式: yyyy-MM-dd'T'HH:mm:ss"),
                        RequestDocumentation.parameterWithName("after").description("Optional.结束时间，默认值为当前时间 格式: yyyy-MM-dd'T'HH:mm:ss"),
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
        val trading = randomTrading(suYuanCheng = suYuanCheng)
        val tradingDTO = randomTradingDTO(
            suYuanChengId = suYuanCheng.uuid,
            sequence = trading.sequence,
            amount = trading.amount,
            time = trading.time
        )
        given {
            tradingService.create(any(), any(), any(), any())
        }.willReturn {
            trading
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/trading/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(tradingDTO))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "trading/create"
                )
            )
    }
}