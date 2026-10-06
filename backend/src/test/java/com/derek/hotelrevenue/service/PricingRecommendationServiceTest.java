package com.derek.hotelrevenue.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.derek.hotelrevenue.dto.PricingRecommendationResponse;
import com.derek.hotelrevenue.enums.BookingStatus;
import com.derek.hotelrevenue.enums.RoomType;
import com.derek.hotelrevenue.model.Booking;
import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.PricingRule;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.BookingRepository;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.PricingRuleRepository;
import com.derek.hotelrevenue.repository.RoomRepository;

class PricingRecommendationServiceTest {
    
    private BookingRepository bookingRepository;
    private HotelRepository hotelRepository;
    private PricingRuleRepository pricingRuleRepository;
    private RoomRepository roomRepository;

    private PricingRecommendationService pricingRecommendationService;

    private Hotel hotel;
    private Guest guest;
    private Room room;
private final LocalDate today = LocalDate.of(2026, 10, 6);
    @BeforeEach
    void setUp() {

        bookingRepository =
                mock(BookingRepository.class);

        hotelRepository =
                mock(HotelRepository.class);

        pricingRuleRepository =
                mock(PricingRuleRepository.class);

        roomRepository =
                mock(RoomRepository.class);

        pricingRecommendationService =
                new PricingRecommendationService(
                        bookingRepository,
                        hotelRepository,
                        pricingRuleRepository,
                        roomRepository
                );

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
                RoomType.values()[0],
                new BigDecimal("5000.00")
        );
    }

    @Test
    void shouldIncreasePriceByTenPercent() {

        BigDecimal result =
                pricingRecommendationService.calculateRecommendedPrice(
                        new BigDecimal("5000.00"),
                        new BigDecimal("10.00")
                );

        assertEquals(
                new BigDecimal("5500.00"),
                result
        );
    }

    @Test
    void shouldDecreasePriceByTenPercent() {

        BigDecimal result =
                pricingRecommendationService.calculateRecommendedPrice(
                        new BigDecimal("5000.00"),
                        new BigDecimal("-10.00")
                );

        assertEquals(
                new BigDecimal("4500.00"),
                result
        );
    }

    @Test
    void shouldMaintainPriceWhenAdjustmentIsZero() {

        BigDecimal result =
                pricingRecommendationService.calculateRecommendedPrice(
                        new BigDecimal("5000.00"),
                        BigDecimal.ZERO
                );

        assertEquals(
                new BigDecimal("5000.00"),
                result
        );
    }

    @Test
    void shouldDetermineIncreaseRecommendation() {

        assertEquals(
                "INCREASE",
                pricingRecommendationService.determineRecommendation(
                        new BigDecimal("10.00")
                )
        );
    }

    @Test
    void shouldDetermineDecreaseRecommendation() {

        assertEquals(
                "DECREASE",
                pricingRecommendationService.determineRecommendation(
                        new BigDecimal("-10.00")
                )
        );
    }

    @Test
    void shouldDetermineMaintainRecommendation() {

        assertEquals(
                "MAINTAIN",
                pricingRecommendationService.determineRecommendation(
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldDetermineHighOccupancyReason() {

        assertEquals(
                "High occupancy detected",
                pricingRecommendationService.determineReason(
                        new BigDecimal("82.00"),
                        new BigDecimal("10.00")
                )
        );
    }

    @Test
    void shouldDetermineLowOccupancyReason() {

        assertEquals(
                "Low occupancy detected",
                pricingRecommendationService.determineReason(
                        new BigDecimal("30.00"),
                        new BigDecimal("-10.00")
                )
        );
    }

    @Test
    void shouldRejectNullCurrentPrice() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        pricingRecommendationService
                                .calculateRecommendedPrice(
                                        null,
                                        new BigDecimal("10.00")
                                )
        );
    }

    @Test
    void shouldRejectNullAdjustment() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        pricingRecommendationService
                                .calculateRecommendedPrice(
                                        new BigDecimal("5000.00"),
                                        null
                                )
        );
    }

    @Test
void shouldCalculateHotelOccupancyAndRecommendHigherPrice() {

    Booking booking1 =
            new Booking(
                    hotel,
                    room,
                    guest,
                    today,
                    today.plusDays(2),
                    new BigDecimal("10000.00")
            );

    Booking booking2 =
            new Booking(
                    hotel,
                    room,
                    guest,
                    today,
                    today.plusDays(2),
                    new BigDecimal("10000.00")
            );

    when(hotelRepository.findById(1L))
            .thenReturn(Optional.of(hotel));

    when(roomRepository.findByHotelId(1L))
            .thenReturn(List.of(room));

    when(
            bookingRepository
                    .findByHotelIdAndCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                            1L,
                            today.plusDays(1),
                            today,
                            BookingStatus.CANCELLED
                    )
    ).thenReturn(List.of(booking1, booking2));

    PricingRule pricingRule =
            new PricingRule(
                    hotel,
                    "High Occupancy",
                    new BigDecimal("20.00"),
                    new BigDecimal("10.00")
            );

    when(
            pricingRuleRepository
                    .findFirstByHotelIdAndActiveTrueAndOccupancyThresholdLessThanEqualOrderByOccupancyThresholdDesc(
                            1L,
                            new BigDecimal("20.00")
                    )
    ).thenReturn(Optional.of(pricingRule));

    List<PricingRecommendationResponse> recommendations =
            pricingRecommendationService
.getRecommendationsForHotel(
        1L,
        today
);

    assertEquals(
            1,
            recommendations.size()
    );

    PricingRecommendationResponse recommendation =
            recommendations.get(0);

    assertEquals(
            "101",
            recommendation.getRoomNumber()
    );

    assertEquals(
            new BigDecimal("20.00"),
            recommendation.getOccupancyRate()
    );

    assertEquals(
            new BigDecimal("10.00"),
            recommendation.getAdjustmentPercentage()
    );

    assertEquals(
            new BigDecimal("5500.00"),
            recommendation.getRecommendedPrice()
    );

    assertEquals(
            "INCREASE",
            recommendation.getRecommendation()
    );

    assertEquals(
            "High occupancy detected",
            recommendation.getReason()
    );
}

   @Test
void shouldMaintainPriceWhenNoPricingRuleApplies() {

    when(hotelRepository.findById(1L))
            .thenReturn(Optional.of(hotel));

    when(roomRepository.findByHotelId(1L))
            .thenReturn(List.of(room));

    when(
            bookingRepository
                    .findByHotelIdAndCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                            1L,
                            today.plusDays(1),
                            today,
                            BookingStatus.CANCELLED
                    )
    ).thenReturn(List.of());

    when(
            pricingRuleRepository
                    .findFirstByHotelIdAndActiveTrueAndOccupancyThresholdLessThanEqualOrderByOccupancyThresholdDesc(
                            1L,
                            BigDecimal.ZERO.setScale(2)
                    )
    ).thenReturn(Optional.empty());

    List<PricingRecommendationResponse> recommendations =
            pricingRecommendationService
.getRecommendationsForHotel(
        1L,
        today
);

    assertEquals(
            1,
            recommendations.size()
    );

    PricingRecommendationResponse recommendation =
            recommendations.get(0);

    assertEquals(
            new BigDecimal("0.00"),
            recommendation.getOccupancyRate()
    );

    assertEquals(
            new BigDecimal("0"),
            recommendation.getAdjustmentPercentage()
    );

    assertEquals(
            new BigDecimal("5000.00"),
            recommendation.getRecommendedPrice()
    );

    assertEquals(
            "MAINTAIN",
            recommendation.getRecommendation()
    );
}

    @Test
    void shouldRejectUnknownHotel() {

        when(hotelRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        pricingRecommendationService
                                .getRecommendationsForHotel(999L)
        );
    }
}