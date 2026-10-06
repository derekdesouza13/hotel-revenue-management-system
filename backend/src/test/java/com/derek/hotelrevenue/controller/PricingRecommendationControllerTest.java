package com.derek.hotelrevenue.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derek.hotelrevenue.dto.PricingRecommendationResponse;
import com.derek.hotelrevenue.service.PricingRecommendationService;

@WebMvcTest(PricingRecommendationController.class)
class PricingRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PricingRecommendationService pricingRecommendationService;

    @Test
    void shouldReturnPricingRecommendationsForHotel()
            throws Exception {

        PricingRecommendationResponse recommendation =
                new PricingRecommendationResponse(
                        1L,
                        "101",
                        new BigDecimal("5000.00"),
                        new BigDecimal("20.00"),
                        new BigDecimal("10.00"),
                        new BigDecimal("5500.00"),
                        "INCREASE",
                        "High occupancy detected"
                );

        when(
                pricingRecommendationService
                        .getRecommendationsForHotel(1L)
        ).thenReturn(List.of(recommendation));

        mockMvc.perform(
                get("/api/pricing-recommendations/hotel/1")
        )
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(
                                "application/json"
                        )
                )
                .andExpect(jsonPath("$[0].roomId").value(1))
                .andExpect(jsonPath("$[0].roomNumber").value("101"))
                .andExpect(jsonPath("$[0].currentPrice").value(5000.00))
                .andExpect(jsonPath("$[0].occupancyRate").value(20.00))
                .andExpect(
                        jsonPath("$[0].adjustmentPercentage")
                                .value(10.00)
                )
                .andExpect(
                        jsonPath("$[0].recommendedPrice")
                                .value(5500.00)
                )
                .andExpect(
                        jsonPath("$[0].recommendation")
                                .value("INCREASE")
                )
                .andExpect(
                        jsonPath("$[0].reason")
                                .value("High occupancy detected")
                );
    }

    @Test
    void shouldReturnPricingRecommendationsForSpecificDate()
            throws Exception {

        PricingRecommendationResponse recommendation =
                new PricingRecommendationResponse(
                        1L,
                        "101",
                        new BigDecimal("5000.00"),
                        new BigDecimal("20.00"),
                        new BigDecimal("10.00"),
                        new BigDecimal("5500.00"),
                        "INCREASE",
                        "High occupancy detected"
                );

        when(
                pricingRecommendationService
                        .getRecommendationsForHotel(
                                1L,
                                LocalDate.of(2026, 10, 6)
                        )
        ).thenReturn(List.of(recommendation));

        mockMvc.perform(
                get("/api/pricing-recommendations/hotel/1")
                        .param("date", "2026-10-06")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomId").value(1))
                .andExpect(jsonPath("$[0].roomNumber").value("101"))
                .andExpect(
                        jsonPath("$[0].recommendedPrice")
                                .value(5500.00)
                )
                .andExpect(
                        jsonPath("$[0].recommendation")
                                .value("INCREASE")
                );
    }

    @Test
    void shouldReturnEmptyListWhenNoRecommendationsExist()
            throws Exception {

        when(
                pricingRecommendationService
                        .getRecommendationsForHotel(1L)
        ).thenReturn(List.of());

        mockMvc.perform(
                get("/api/pricing-recommendations/hotel/1")
        )
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}