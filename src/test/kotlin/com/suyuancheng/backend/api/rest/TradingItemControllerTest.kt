package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.TradingItemService
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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * @author hsj
 */
@RunWith(SpringRunner::class)
@WebMvcTest(TradingItemController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class TradingItemControllerTest {

    @MockBean
    private lateinit var tradingItemService: TradingItemService

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
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val tradingItem = randomTradingItem(trading = trading, goods = goods)
        given {
            tradingItemService.query(any(), any(), any())
        }.willReturn {
            PageImpl(
                arrayListOf(tradingItem),
                PageRequest.of(1, 10, Sort.Direction.DESC, "updateTime"),
                2
            )
        }

        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/trading-item/")
                .param("goodsName", tradingItem.goods.name)
                .param("tradingId", tradingItem.trading.uuid.toString())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "id,DESC")

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "trading-item/query",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("goodsName").description("Optional"),
                        RequestDocumentation.parameterWithName("tradingId").description("Optional"),
                        RequestDocumentation.parameterWithName("page").description("Optional"),
                        RequestDocumentation.parameterWithName("size").description("Optional"),
                        RequestDocumentation.parameterWithName("sort").description("Optional")
                    )
                )
            )
    }

    /* @Test
     @WithMockUser
     fun create() {
         val market = randomMarket()
         val store = randomStore(market = market)
         val suYuanCheng = randomSuYuanCheng(store)
         val trading = randomTrading(suYuanCheng = suYuanCheng)
         val provider = randomProvider(store = store)
         val goods = randomGoods(provider = provider)
         val tradingItem = randomTradingItem(trading = trading, goods = goods)
         val tradingItemDTO = randomTradingItemDTO(
             tradingId = trading.uuid,
             goodsId = goods.uuid
         )
         given {
             tradingItemService.create(any(), any(), any(), any(), any(), any())
         }.willReturn {
             tradingItem
         }

         this.mockMvc.perform(
             RestDocumentationRequestBuilders.post("/api/rest/trading-item/")
                 .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                 .contentType(MediaType.APPLICATION_JSON_UTF8)
                 .content(mapper.writeValueAsString(tradingItemDTO))

         )
             .andExpect(MockMvcResultMatchers.status().isOk)
             .andDo(
                 MockMvcRestDocumentation.document(
                     "trading-item/create"
                 )
             )
     }*/
}