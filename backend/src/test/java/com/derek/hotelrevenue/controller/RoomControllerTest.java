package com.derek.hotelrevenue.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derek.hotelrevenue.enums.RoomType;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.PricingRuleRepository;
import com.derek.hotelrevenue.repository.RoomRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private PricingRuleRepository pricingRuleRepository;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {
        pricingRuleRepository.deleteAll();
        roomRepository.deleteAll();
        hotelRepository.deleteAll();

        hotel = hotelRepository.save(
                new Hotel("Test Hotel", "Pune", 50)
        );

        room = roomRepository.save(
                new Room(
                        hotel,
                        "101",
                        RoomType.DELUXE,
                        new BigDecimal("5000.00")
                )
        );
    }

    @Test
    void shouldGetAllRooms() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value("101"));
    }

    @Test
    void shouldGetRoomById() throws Exception {
        mockMvc.perform(get("/api/rooms/" + room.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value("101"))
                .andExpect(jsonPath("$.basePrice").value(5000.00));
    }

    @Test
    void shouldGetRoomsByHotel() throws Exception {
        mockMvc.perform(get("/api/rooms/hotel/" + hotel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value("101"));
    }

    @Test
    void shouldGetAvailableRooms() throws Exception {
        mockMvc.perform(
                        get("/api/rooms/hotel/" + hotel.getId() + "/available")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value("101"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }

    @Test
    void shouldUpdateRoomStatus() throws Exception {
        mockMvc.perform(
                        patch("/api/rooms/" + room.getId() + "/status")
                                .param("status", "OCCUPIED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OCCUPIED"));
    }

    @Test
    void shouldDeleteRoom() throws Exception {
        mockMvc.perform(delete("/api/rooms/" + room.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/rooms/" + room.getId()))
                .andExpect(status().isNotFound());
    }
}