package com.derek.hotelrevenue.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.PricingRule;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.PricingRuleRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PricingRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PricingRuleRepository pricingRuleRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel hotel;
    private PricingRule pricingRule;

    @BeforeEach
    void setUp() {

        pricingRuleRepository.deleteAll();
        hotelRepository.deleteAll();

        hotel = hotelRepository.save(
                new Hotel("Grand Hotel", "Pune", 100)
        );

        pricingRule = pricingRuleRepository.save(
                new PricingRule(
                        hotel,
                        "High Occupancy",
                        new BigDecimal("80.00"),
                        new BigDecimal("20.00")
                )
        );
    }

    @Test
    void shouldGetAllPricingRules() throws Exception {

        mockMvc.perform(get("/api/pricing-rules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ruleName").value("High Occupancy"))
                .andExpect(jsonPath("$[0].occupancyThreshold").value(80.00))
                .andExpect(jsonPath("$[0].adjustmentPercentage").value(20.00))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldGetPricingRuleById() throws Exception {

        mockMvc.perform(
                get("/api/pricing-rules/" + pricingRule.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ruleName").value("High Occupancy"))
                .andExpect(jsonPath("$.occupancyThreshold").value(80.00))
                .andExpect(jsonPath("$.adjustmentPercentage").value(20.00))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldGetPricingRulesByHotel() throws Exception {

        mockMvc.perform(
                get("/api/pricing-rules/hotel/" + hotel.getId())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ruleName").value("High Occupancy"))
                .andExpect(jsonPath("$[0].occupancyThreshold").value(80.00));
    }

    @Test
    void shouldGetActivePricingRules() throws Exception {

        mockMvc.perform(
                get("/api/pricing-rules/hotel/" + hotel.getId() + "/active")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ruleName").value("High Occupancy"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldCreatePricingRule() throws Exception {

        String requestBody = """
                {
                    "hotelId": %d,
                    "ruleName": "Low Occupancy",
                    "occupancyThreshold": 30.00,
                    "adjustmentPercentage": -10.00,
                    "active": true
                }
                """.formatted(hotel.getId());

        mockMvc.perform(
                post("/api/pricing-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ruleName").value("Low Occupancy"))
                .andExpect(jsonPath("$.occupancyThreshold").value(30.00))
                .andExpect(jsonPath("$.adjustmentPercentage").value(-10.00))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldUpdatePricingRule() throws Exception {

        String requestBody = """
                {
                    "ruleName": "Updated Occupancy Rule",
                    "occupancyThreshold": 90.00,
                    "adjustmentPercentage": 25.00,
                    "active": false
                }
                """;

        mockMvc.perform(
                put("/api/pricing-rules/" + pricingRule.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ruleName").value("Updated Occupancy Rule"))
                .andExpect(jsonPath("$.occupancyThreshold").value(90.00))
                .andExpect(jsonPath("$.adjustmentPercentage").value(25.00))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void shouldDeletePricingRule() throws Exception {

        mockMvc.perform(
                delete("/api/pricing-rules/" + pricingRule.getId())
        )
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/api/pricing-rules/" + pricingRule.getId())
        )
                .andExpect(status().isNotFound());
    }
}