package com.derek.hotelrevenue.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derek.hotelrevenue.enums.BookingStatus;
import com.derek.hotelrevenue.enums.RoomType;
import com.derek.hotelrevenue.model.Booking;
import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.BookingRepository;
import com.derek.hotelrevenue.repository.GuestRepository;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.PricingRuleRepository;
import com.derek.hotelrevenue.repository.RoomRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private PricingRuleRepository pricingRuleRepository;

    private Hotel hotel;
    private Room room;
    private Guest guest;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        pricingRuleRepository.deleteAll();
        roomRepository.deleteAll();
        guestRepository.deleteAll();
        hotelRepository.deleteAll();

        hotel = hotelRepository.save(
                new Hotel("Grand Hotel", "Pune", 100)
        );

        room = roomRepository.save(
                new Room(
                        hotel,
                        "101",
                        RoomType.DELUXE,
                        new BigDecimal("5000.00")
                )
        );

        guest = guestRepository.save(
                new Guest(
                        "John Smith",
                        "john@example.com",
                        "+919876543210"
                )
        );
    }

    @Test
    void shouldGetAllBookings() throws Exception {
        createBooking();

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hotelId").value(hotel.getId()))
                .andExpect(jsonPath("$[0].roomId").value(room.getId()))
                .andExpect(jsonPath("$[0].guestId").value(guest.getId()));
    }

    @Test
    void shouldGetBookingById() throws Exception {
        Booking booking = createBooking();

        mockMvc.perform(get("/api/bookings/" + booking.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotelId").value(hotel.getId()))
                .andExpect(jsonPath("$.roomId").value(room.getId()))
                .andExpect(jsonPath("$.guestId").value(guest.getId()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldCreateBookingAndCalculateTotalAmount() throws Exception {
        String requestBody = """
                {
                    "hotelId": %d,
                    "roomId": %d,
                    "guestId": %d,
                    "checkIn": "2026-11-10",
                    "checkOut": "2026-11-13"
                }
                """.formatted(
                hotel.getId(),
                room.getId(),
                guest.getId()
        );

        mockMvc.perform(
                        post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hotelId").value(hotel.getId()))
                .andExpect(jsonPath("$.roomId").value(room.getId()))
                .andExpect(jsonPath("$.guestId").value(guest.getId()))
                .andExpect(jsonPath("$.numberOfNights").value(3))
                .andExpect(jsonPath("$.totalAmount").value(15000.00))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldGetBookingsByHotel() throws Exception {
        createBooking();

        mockMvc.perform(
                        get("/api/bookings/hotel/" + hotel.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hotelId").value(hotel.getId()))
                .andExpect(jsonPath("$[0].roomId").value(room.getId()));
    }

    @Test
    void shouldGetBookingsByRoom() throws Exception {
        createBooking();

        mockMvc.perform(
                        get("/api/bookings/room/" + room.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomId").value(room.getId()))
                .andExpect(jsonPath("$[0].guestId").value(guest.getId()));
    }

    @Test
    void shouldRejectInvalidBookingDates() throws Exception {
        String requestBody = """
                {
                    "hotelId": %d,
                    "roomId": %d,
                    "guestId": %d,
                    "checkIn": "2026-11-15",
                    "checkOut": "2026-11-10"
                }
                """.formatted(
                hotel.getId(),
                room.getId(),
                guest.getId()
        );

        mockMvc.perform(
                        post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectOverlappingBooking() throws Exception {
        createBooking();

        String requestBody = """
                {
                    "hotelId": %d,
                    "roomId": %d,
                    "guestId": %d,
                    "checkIn": "2026-11-12",
                    "checkOut": "2026-11-15"
                }
                """.formatted(
                hotel.getId(),
                room.getId(),
                guest.getId()
        );

        mockMvc.perform(
                        post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectRoomFromDifferentHotel() throws Exception {
        Hotel anotherHotel = hotelRepository.save(
                new Hotel("Another Hotel", "Mumbai", 50)
        );

        String requestBody = """
                {
                    "hotelId": %d,
                    "roomId": %d,
                    "guestId": %d,
                    "checkIn": "2026-12-01",
                    "checkOut": "2026-12-03"
                }
                """.formatted(
                anotherHotel.getId(),
                room.getId(),
                guest.getId()
        );

        mockMvc.perform(
                        post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCancelBooking() throws Exception {
        Booking booking = createBooking();

        mockMvc.perform(
                        patch("/api/bookings/" + booking.getId() + "/cancel")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        Booking booking = createBooking();

        mockMvc.perform(
                        delete("/api/bookings/" + booking.getId())
                )
                .andExpect(status().isNoContent());

        mockMvc.perform(
                        get("/api/bookings/" + booking.getId())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundForMissingBooking() throws Exception {
        mockMvc.perform(
                        get("/api/bookings/999999")
                )
                .andExpect(status().isNotFound());
    }

    private Booking createBooking() {
        LocalDate checkIn = LocalDate.of(2026, 11, 10);
        LocalDate checkOut = LocalDate.of(2026, 11, 13);

        BigDecimal totalAmount = room.getBasePrice()
                .multiply(
                        BigDecimal.valueOf(
                                checkIn.until(checkOut).getDays()
                        )
                );

        Booking booking = new Booking(
                hotel,
                room,
                guest,
                checkIn,
                checkOut,
                totalAmount
        );

        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }
}