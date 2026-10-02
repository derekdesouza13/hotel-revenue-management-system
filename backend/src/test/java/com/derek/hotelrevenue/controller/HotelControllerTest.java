package com.derek.hotelrevenue.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.PricingRuleRepository;
import com.derek.hotelrevenue.repository.RoomRepository;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PricingRuleRepository pricingRuleRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        pricingRuleRepository.deleteAll();
            roomRepository.deleteAll();

        hotelRepository.deleteAll();
    }

    @Test
    void shouldCreateHotel() throws Exception {

        String request = """
                {
                    "name": "Test Pune Hotel",
                    "location": "Pune",
                    "totalRooms": 100
                }
                """;

        mockMvc.perform(
                        post("/api/hotels")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("Test Pune Hotel"))
                .andExpect(jsonPath("$.location")
                        .value("Pune"))
                .andExpect(jsonPath("$.totalRooms")
                        .value(100));
    }

    @Test
    void shouldGetAllHotels() throws Exception {

        hotelRepository.save(
                new Hotel("Pune Hotel", "Pune", 100)
        );

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Pune Hotel"));
    }

    @Test
    void shouldGetHotelById() throws Exception {

        Hotel hotel = hotelRepository.save(
                new Hotel("Pune Hotel", "Pune", 100)
        );

        mockMvc.perform(
                        get("/api/hotels/" + hotel.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(hotel.getId()))
                .andExpect(jsonPath("$.name")
                        .value("Pune Hotel"));
    }

    @Test
    void shouldReturnNotFoundForMissingHotel() throws Exception {

        mockMvc.perform(
                        get("/api/hotels/99999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404));
    }
}