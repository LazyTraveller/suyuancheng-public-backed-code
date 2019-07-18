package com.suyuancheng.backend.api.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.willReturn
import com.suyuancheng.backend.domain.*
import com.suyuancheng.backend.service.TradingInfoService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
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
@WebMvcTest(TradingInfoController::class)
@EnableSpringDataWebSupport
@AutoConfigureRestDocs("build/snippets")
class TradingInfoControllerTest {

    @MockBean
    private lateinit var tradingInfoService: TradingInfoService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    @WithMockUser
    fun pull() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val suYuanCheng = randomSuYuanCheng(store)
        val trading = randomTrading(suYuanCheng = suYuanCheng)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val main = randomReMain(
            TransNo = trading.sequence,
            MarketName = market.name,
            StoreName = store.name
        )
        val detail = randomReDetail(
            ProductName = goods.name,
            ProviderName = provider.name,
            purchaseDate = goods.purchaseDate
        )
        val reTradingInfo = randomReTradingInfo(main, arrayListOf(detail))
        given {
            tradingInfoService.pull(any(), any())
        } willReturn {
            reTradingInfo
        }
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/rest/trading-info/")
                .param("TransNo", trading.sequence)
                .param("MachineId", suYuanCheng.machineId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "trading-info/pull",
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("TransNo").description("Optional"),
                        RequestDocumentation.parameterWithName("MachineId").description("Optional")
                    )
                )
            )
    }

    @Test
    @WithMockUser
    fun push() {
        val market = randomMarket()
        val store = randomStore(market = market)
        val suYuanCheng = randomSuYuanCheng(store)
        val trading = randomTrading(suYuanCheng = suYuanCheng)
        val provider = randomProvider(store = store)
        val goods = randomGoods(provider = provider)
        val plu = randomPLU(
            id = PLU.PLUId(suYuanCheng.uuid, goods.uuid),
            goods = goods,
            suYuanCheng = suYuanCheng
        )
        val main = randomeMain(TransNo = trading.sequence, MachineId = suYuanCheng.machineId)
        val detail =
            randomDetail(TransNo = trading.sequence, ProductName = goods.name, PLU = plu.code)
        val payment = randomPayment(TransNo = trading.sequence, Amount = 55.55f)
        val tradingInfo = randomTradingInfo(main, arrayListOf(detail), arrayListOf(payment))
        doNothing().`when`(tradingInfoService).push(tradingInfo)
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/rest/trading-info/")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(tradingInfo))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "trading-info/push"
                )
            )
    }
}