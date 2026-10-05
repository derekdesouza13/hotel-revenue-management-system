package com.derek.hotelrevenue.service.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.derek.hotelrevenue.dto.analytics.DashboardSummaryResponse;
import com.derek.hotelrevenue.dto.analytics.RecentBookingResponse;
import com.derek.hotelrevenue.enums.BookingStatus;
import com.derek.hotelrevenue.enums.RoomType;
import com.derek.hotelrevenue.model.Booking;
import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.BookingRepository;
import com.derek.hotelrevenue.repository.GuestRepository;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.RoomRepository;

class AnalyticsServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private Hotel hotel;
    private Guest guest;
    private Room room;
    private RoomType roomType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        roomType = RoomType.values()[0];

        hotel = new Hotel(
                "Test Hotel",
                "Pune",
                10
        );

        guest = new Guest(
                "John Smith",
                "john@example.com",
                "9876543210"
        );

        room = new Room(
                hotel,
                "101",
                roomType,
                new BigDecimal("5000.00")
        );
    }

    private Booking createBooking(
            LocalDate checkIn,
            LocalDate checkOut,
            BigDecimal totalAmount
    ) {
        return new Booking(
                hotel,
                room,
                guest,
                checkIn,
                checkOut,
                totalAmount
        );
    }

    private void mockRepositories(Booking booking) {

        when(
                bookingRepository
                        .findByCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                                LocalDate.of(2026, 11, 1),
                                LocalDate.of(2026, 10, 1),
                                BookingStatus.CANCELLED
                        )
        ).thenReturn(List.of(booking));

        when(hotelRepository.findAll())
                .thenReturn(List.of(hotel));

        when(guestRepository.findById(booking.getGuest().getId()))
                .thenReturn(Optional.of(guest));

        when(roomRepository.findById(booking.getRoom().getId()))
                .thenReturn(Optional.of(room));
    }

    @Test
    void shouldCalculateDashboardMetrics() {

        Booking booking = createBooking(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 13),
                new BigDecimal("15000.00")
        );

        mockRepositories(booking);

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        assertNotNull(response);

        assertEquals(
                new BigDecimal("15000.00"),
                response.getTotalRevenue()
        );

        assertEquals(
                0.97,
                response.getOccupancyRate()
        );

        assertEquals(
                new BigDecimal("5000.00"),
                response.getAdr()
        );

        assertEquals(
                new BigDecimal("48.39"),
                response.getRevpar()
        );

        assertEquals(
                1,
                response.getTotalBookings()
        );
    }

    @Test
    void shouldCalculateRevenueForOccupiedNightsOnly() {

        Booking booking = createBooking(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 13),
                new BigDecimal("15000.00")
        );

        mockRepositories(booking);

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        assertEquals(
                0,
                response.getRevenueTrend()
                        .get(8)
                        .getRevenue()
                        .compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                new BigDecimal("5000.00"),
                response.getRevenueTrend()
                        .get(9)
                        .getRevenue()
        );

        assertEquals(
                new BigDecimal("5000.00"),
                response.getRevenueTrend()
                        .get(10)
                        .getRevenue()
        );

        assertEquals(
                new BigDecimal("5000.00"),
                response.getRevenueTrend()
                        .get(11)
                        .getRevenue()
        );

        assertEquals(
                0,
                response.getRevenueTrend()
                        .get(12)
                        .getRevenue()
                        .compareTo(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldExcludeCancelledBookings() {

        when(
                bookingRepository
                        .findByCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                                LocalDate.of(2026, 11, 1),
                                LocalDate.of(2026, 10, 1),
                                BookingStatus.CANCELLED
                        )
        ).thenReturn(List.of());

        when(hotelRepository.findAll())
                .thenReturn(List.of(hotel));

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        assertEquals(
                0,
                response.getTotalRevenue().compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                0.0,
                response.getOccupancyRate()
        );

        assertEquals(
                0,
                response.getAdr().compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                0,
                response.getRevpar().compareTo(BigDecimal.ZERO)
        );

        assertEquals(
                0,
                response.getTotalBookings()
        );

        assertEquals(
                0,
                response.getRecentBookings().size()
        );
    }

    @Test
    void shouldCalculateOccupancyBasedOnRoomNights() {

        Booking booking = createBooking(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 13),
                new BigDecimal("15000.00")
        );

        mockRepositories(booking);

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        /*
         * 10 rooms × 31 days = 310 available room nights.
         *
         * 3 occupied nights / 310 available nights × 100
         * = 0.9677...%
         *
         * Rounded to 0.97%.
         */
        assertEquals(
                0.97,
                response.getOccupancyRate()
        );
    }

    @Test
    void shouldReturnRecentBookingsWithGuestAndRoomDetails() {

        Booking booking = createBooking(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 13),
                new BigDecimal("15000.00")
        );

        mockRepositories(booking);

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        List<RecentBookingResponse> recentBookings =
                response.getRecentBookings();

        assertNotNull(recentBookings);

        assertEquals(
                1,
                recentBookings.size()
        );

        RecentBookingResponse recentBooking =
                recentBookings.get(0);

        assertEquals(
                "John Smith",
                recentBooking.getGuestName()
        );

        assertEquals(
                "101",
                recentBooking.getRoomNumber()
        );

        assertEquals(
                new BigDecimal("15000.00"),
                recentBooking.getTotalAmount()
        );

        assertEquals(
                BookingStatus.CONFIRMED,
                recentBooking.getStatus()
        );
    }

    @Test
    void shouldGenerateThirtyOneDaysOfRevenueTrendForOctober() {

        when(
                bookingRepository
                        .findByCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                                LocalDate.of(2026, 11, 1),
                                LocalDate.of(2026, 10, 1),
                                BookingStatus.CANCELLED
                        )
        ).thenReturn(List.of());

        when(hotelRepository.findAll())
                .thenReturn(List.of(hotel));

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        assertEquals(
                31,
                response.getRevenueTrend().size()
        );

        assertEquals(
                "2026-10-01",
                response.getRevenueTrend()
                        .get(0)
                        .getDate()
        );

        assertEquals(
                "2026-10-31",
                response.getRevenueTrend()
                        .get(30)
                        .getDate()
        );
    }

    @Test
    void shouldGenerateThirtyOneDaysOfOccupancyTrendForOctober() {

        when(
                bookingRepository
                        .findByCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                                LocalDate.of(2026, 11, 1),
                                LocalDate.of(2026, 10, 1),
                                BookingStatus.CANCELLED
                        )
        ).thenReturn(List.of());

        when(hotelRepository.findAll())
                .thenReturn(List.of(hotel));

        DashboardSummaryResponse response =
                analyticsService.getDashboardSummary();

        assertEquals(
                31,
                response.getOccupancyTrend().size()
        );

        assertEquals(
                "2026-10-01",
                response.getOccupancyTrend()
                        .get(0)
                        .getDate()
        );

        assertEquals(
                "2026-10-31",
                response.getOccupancyTrend()
                        .get(30)
                        .getDate()
        );
    }
}