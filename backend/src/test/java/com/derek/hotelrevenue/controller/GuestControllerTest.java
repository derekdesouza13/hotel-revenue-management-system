package com.derek.hotelrevenue.controller;

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

import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.repository.GuestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GuestRepository guestRepository;

    private Guest guest;

    @BeforeEach
    void setUp() {
        guestRepository.deleteAll();

        guest = guestRepository.save(
                new Guest(
                        "John Smith",
                        "john@example.com",
                        "+919876543210"
                )
        );
    }

    @Test
    void shouldGetAllGuests() throws Exception {
        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void shouldGetGuestById() throws Exception {
        mockMvc.perform(get("/api/guests/" + guest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldCreateGuest() throws Exception {
        String requestBody = """
                {
                    "name": "Jane Doe",
                    "email": "jane@example.com",
                    "phone": "+919999999999"
                }
                """;

        mockMvc.perform(
                        post("/api/guests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    void shouldUpdateGuest() throws Exception {
        String requestBody = """
                {
                    "name": "John Updated",
                    "email": "john.updated@example.com",
                    "phone": "+918888888888"
                }
                """;

        mockMvc.perform(
                        put("/api/guests/" + guest.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        String requestBody = """
                {
                    "name": "Another John",
                    "email": "john@example.com",
                    "phone": "+917777777777"
                }
                """;

        mockMvc.perform(
                        post("/api/guests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteGuest() throws Exception {
        mockMvc.perform(delete("/api/guests/" + guest.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/guests/" + guest.getId()))
                .andExpect(status().isNotFound());
    }
}