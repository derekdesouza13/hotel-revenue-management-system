package com.derek.hotelrevenue.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.derek.hotelrevenue.dto.PricingRecommendationResponse;
import com.derek.hotelrevenue.enums.BookingStatus;
import com.derek.hotelrevenue.model.Booking;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.PricingRule;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.BookingRepository;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.PricingRuleRepository;
import com.derek.hotelrevenue.repository.RoomRepository;

@Service
public class PricingRecommendationService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final RoomRepository roomRepository;

    public PricingRecommendationService(
            BookingRepository bookingRepository,
            HotelRepository hotelRepository,
            PricingRuleRepository pricingRuleRepository,
            RoomRepository roomRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        this.roomRepository = roomRepository;
    }

    public BigDecimal calculateRecommendedPrice(
            BigDecimal currentPrice,
            BigDecimal adjustmentPercentage
    ) {

        if (currentPrice == null) {
            throw new IllegalArgumentException(
                    "Current price cannot be null"
            );
        }

        if (adjustmentPercentage == null) {
            throw new IllegalArgumentException(
                    "Adjustment percentage cannot be null"
            );
        }

        BigDecimal multiplier =
                BigDecimal.ONE.add(
                        adjustmentPercentage.divide(
                                BigDecimal.valueOf(100),
                                4,
                                RoundingMode.HALF_UP
                        )
                );

        return currentPrice
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public String determineRecommendation(
            BigDecimal adjustmentPercentage
    ) {

        if (adjustmentPercentage == null) {
            return "MAINTAIN";
        }

        int comparison =
                adjustmentPercentage.compareTo(BigDecimal.ZERO);

        if (comparison > 0) {
            return "INCREASE";
        }

        if (comparison < 0) {
            return "DECREASE";
        }

        return "MAINTAIN";
    }

    public String determineReason(
            BigDecimal occupancyRate,
            BigDecimal adjustmentPercentage
    ) {

        if (occupancyRate == null) {
            return "No occupancy data available";
        }

        if (adjustmentPercentage == null) {
            return "No pricing adjustment configured";
        }

        int comparison =
                adjustmentPercentage.compareTo(BigDecimal.ZERO);

        if (comparison > 0) {
            return "High occupancy detected";
        }

        if (comparison < 0) {
            return "Low occupancy detected";
        }

        return "Occupancy is within the configured pricing range";
    }

    public List<PricingRecommendationResponse>
    getRecommendationsForHotel(Long hotelId) {

        return getRecommendationsForHotel(
                hotelId,
                LocalDate.now()
        );
    }

    public List<PricingRecommendationResponse>
    getRecommendationsForHotel(
            Long hotelId,
            LocalDate date
    ) {

        Hotel hotel =
                hotelRepository.findById(hotelId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Hotel not found with id: " + hotelId
                                )
                        );

        List<Room> rooms =
                roomRepository.findByHotelId(hotelId);

        BigDecimal occupancyRate =
                calculateOccupancyForDate(
                        hotelId,
                        hotel.getTotalRooms(),
                        date
                );

        PricingRule pricingRule =
                pricingRuleRepository
                        .findFirstByHotelIdAndActiveTrueAndOccupancyThresholdLessThanEqualOrderByOccupancyThresholdDesc(
                                hotelId,
                                occupancyRate
                        )
                        .orElse(null);

        return rooms.stream()
                .map(room ->
                        buildRecommendation(
                                room,
                                occupancyRate,
                                pricingRule
                        )
                )
                .toList();
    }

    BigDecimal calculateOccupancyForDate(
            Long hotelId,
            int totalRooms,
            LocalDate date
    ) {

        if (totalRooms == 0) {
            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }

        List<Booking> activeBookings =
                bookingRepository
                        .findByHotelIdAndCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                                hotelId,
                                date.plusDays(1),
                                date,
                                BookingStatus.CANCELLED
                        );

        return BigDecimal.valueOf(activeBookings.size())
                .divide(
                        BigDecimal.valueOf(totalRooms),
                        4,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private PricingRecommendationResponse buildRecommendation(
            Room room,
            BigDecimal occupancyRate,
            PricingRule pricingRule
    ) {

        BigDecimal adjustmentPercentage =
                pricingRule != null
                        ? pricingRule.getAdjustmentPercentage()
                        : BigDecimal.ZERO;

        BigDecimal recommendedPrice =
                calculateRecommendedPrice(
                        room.getBasePrice(),
                        adjustmentPercentage
                );

        String recommendation =
                determineRecommendation(
                        adjustmentPercentage
                );

        String reason =
                determineReason(
                        occupancyRate,
                        adjustmentPercentage
                );

        return new PricingRecommendationResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getBasePrice(),
                occupancyRate,
                adjustmentPercentage,
                recommendedPrice,
                recommendation,
                reason
        );
    }
}