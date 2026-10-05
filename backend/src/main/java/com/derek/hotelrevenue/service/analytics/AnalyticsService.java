package com.derek.hotelrevenue.service.analytics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.derek.hotelrevenue.dto.analytics.DashboardSummaryResponse;
import com.derek.hotelrevenue.dto.analytics.RecentBookingResponse;
import com.derek.hotelrevenue.enums.BookingStatus;
import com.derek.hotelrevenue.model.Booking;
import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.BookingRepository;
import com.derek.hotelrevenue.repository.GuestRepository;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.RoomRepository;

@Service
public class AnalyticsService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public AnalyticsService(
            BookingRepository bookingRepository,
            HotelRepository hotelRepository,
            GuestRepository guestRepository,
            RoomRepository roomRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    public DashboardSummaryResponse getDashboardSummary() {

        YearMonth currentMonth = YearMonth.now();

        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        List<Booking> bookings =
                bookingRepository
                        .findByCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                                endDate.plusDays(1),
                                startDate,
                                BookingStatus.CANCELLED
                        );

        BigDecimal totalRevenue = calculateTotalRevenue(
                bookings,
                startDate,
                endDate
        );

        long totalBookings = bookings.size();

        long totalAvailableRoomNights =
                calculateAvailableRoomNights(
                        startDate,
                        endDate
                );

        long occupiedRoomNights =
                calculateOccupiedRoomNights(
                        bookings,
                        startDate,
                        endDate
                );

        double occupancyRate =
                calculateOccupancyRate(
                        occupiedRoomNights,
                        totalAvailableRoomNights
                );

        BigDecimal adr =
                calculateAdr(
                        totalRevenue,
                        occupiedRoomNights
                );

        BigDecimal revpar =
                calculateRevpar(
                        totalRevenue,
                        totalAvailableRoomNights
                );

        List<DashboardSummaryResponse.RevenueTrendPoint> revenueTrend =
                calculateRevenueTrend(
                        bookings,
                        startDate,
                        endDate
                );

        List<DashboardSummaryResponse.OccupancyTrendPoint> occupancyTrend =
                calculateOccupancyTrend(
                        bookings,
                        startDate,
                        endDate
                );

        List<RecentBookingResponse> recentBookings =
                calculateRecentBookings(bookings);

        return new DashboardSummaryResponse(
                totalRevenue,
                occupancyRate,
                adr,
                revpar,
                totalBookings,
                startDate,
                endDate,
                revenueTrend,
                occupancyTrend,
                recentBookings
        );
    }

    private BigDecimal calculateTotalRevenue(
            List<Booking> bookings,
            LocalDate startDate,
            LocalDate endDate
    ) {
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Booking booking : bookings) {

            LocalDate stayStart =
                    booking.getCheckIn().isAfter(startDate)
                            ? booking.getCheckIn()
                            : startDate;

            LocalDate stayEnd =
                    booking.getCheckOut().isBefore(endDate.plusDays(1))
                            ? booking.getCheckOut()
                            : endDate.plusDays(1);

            long overlappingNights =
                    ChronoUnit.DAYS.between(
                            stayStart,
                            stayEnd
                    );

            long totalBookingNights =
                    booking.getNumberOfNights();

            if (totalBookingNights <= 0 || overlappingNights <= 0) {
                continue;
            }

            BigDecimal nightlyRevenue =
                    booking.getTotalAmount().divide(
                            BigDecimal.valueOf(totalBookingNights),
                            2,
                            RoundingMode.HALF_UP
                    );

            totalRevenue = totalRevenue.add(
                    nightlyRevenue.multiply(
                            BigDecimal.valueOf(overlappingNights)
                    )
            );
        }

        return totalRevenue.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    private long calculateAvailableRoomNights(
            LocalDate startDate,
            LocalDate endDate
    ) {
        long numberOfDays =
                ChronoUnit.DAYS.between(
                        startDate,
                        endDate.plusDays(1)
                );

        long totalRooms = 0;

        for (Hotel hotel : hotelRepository.findAll()) {
            totalRooms += hotel.getTotalRooms();
        }

        return totalRooms * numberOfDays;
    }

    private long calculateOccupiedRoomNights(
            List<Booking> bookings,
            LocalDate startDate,
            LocalDate endDate
    ) {
        long occupiedRoomNights = 0;

        for (Booking booking : bookings) {

            LocalDate stayStart =
                    booking.getCheckIn().isAfter(startDate)
                            ? booking.getCheckIn()
                            : startDate;

            LocalDate stayEnd =
                    booking.getCheckOut().isBefore(endDate.plusDays(1))
                            ? booking.getCheckOut()
                            : endDate.plusDays(1);

            long nights =
                    ChronoUnit.DAYS.between(
                            stayStart,
                            stayEnd
                    );

            if (nights > 0) {
                occupiedRoomNights += nights;
            }
        }

        return occupiedRoomNights;
    }

    private double calculateOccupancyRate(
            long occupiedRoomNights,
            long availableRoomNights
    ) {
        if (availableRoomNights == 0) {
            return 0.0;
        }

        return round(
                (double) occupiedRoomNights
                        / availableRoomNights
                        * 100
        );
    }

    private BigDecimal calculateAdr(
            BigDecimal totalRevenue,
            long occupiedRoomNights
    ) {
        if (occupiedRoomNights == 0) {
            return BigDecimal.ZERO;
        }

        return totalRevenue.divide(
                BigDecimal.valueOf(occupiedRoomNights),
                2,
                RoundingMode.HALF_UP
        );
    }

    private BigDecimal calculateRevpar(
            BigDecimal totalRevenue,
            long availableRoomNights
    ) {
        if (availableRoomNights == 0) {
            return BigDecimal.ZERO;
        }

        return totalRevenue.divide(
                BigDecimal.valueOf(availableRoomNights),
                2,
                RoundingMode.HALF_UP
        );
    }

    private List<DashboardSummaryResponse.RevenueTrendPoint>
    calculateRevenueTrend(
            List<Booking> bookings,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<DashboardSummaryResponse.RevenueTrendPoint> trend =
                new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            BigDecimal dailyRevenue = BigDecimal.ZERO;

            for (Booking booking : bookings) {

                if (!booking.getCheckIn().isAfter(currentDate)
                        && booking.getCheckOut().isAfter(currentDate)) {

                    long totalNights =
                            booking.getNumberOfNights();

                    if (totalNights > 0) {

                        BigDecimal nightlyRevenue =
                                booking.getTotalAmount().divide(
                                        BigDecimal.valueOf(totalNights),
                                        2,
                                        RoundingMode.HALF_UP
                                );

                        dailyRevenue =
                                dailyRevenue.add(nightlyRevenue);
                    }
                }
            }

            trend.add(
                    new DashboardSummaryResponse.RevenueTrendPoint(
                            currentDate.toString(),
                            dailyRevenue.setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            )
                    )
            );

            currentDate = currentDate.plusDays(1);
        }

        return trend;
    }

    private List<DashboardSummaryResponse.OccupancyTrendPoint>
    calculateOccupancyTrend(
            List<Booking> bookings,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<DashboardSummaryResponse.OccupancyTrendPoint> trend =
                new ArrayList<>();

        long totalRooms = 0;

        for (Hotel hotel : hotelRepository.findAll()) {
            totalRooms += hotel.getTotalRooms();
        }

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            long occupiedRooms = 0;

            for (Booking booking : bookings) {

                if (!booking.getCheckIn().isAfter(currentDate)
                        && booking.getCheckOut().isAfter(currentDate)) {

                    occupiedRooms++;
                }
            }

            double occupancyRate = 0.0;

            if (totalRooms > 0) {
                occupancyRate =
                        round(
                                (double) occupiedRooms
                                        / totalRooms
                                        * 100
                        );
            }

            trend.add(
                    new DashboardSummaryResponse.OccupancyTrendPoint(
                            currentDate.toString(),
                            occupancyRate
                    )
            );

            currentDate = currentDate.plusDays(1);
        }

        return trend;
    }

    private List<RecentBookingResponse> calculateRecentBookings(
            List<Booking> bookings
    ) {
        List<RecentBookingResponse> recentBookings =
                new ArrayList<>();

        bookings.stream()
                .sorted(
                        (a, b) ->
                                b.getBookingDate()
                                        .compareTo(
                                                a.getBookingDate()
                                        )
                )
                .limit(5)
                .forEach(booking -> {

                    Guest guest =
                            guestRepository
                                    .findById(
                                            booking.getGuest().getId()
                                    )
                                    .orElse(null);

                    Room room =
                            roomRepository
                                    .findById(
                                            booking.getRoom().getId()
                                    )
                                    .orElse(null);

                    String guestName =
                            guest != null
                                    ? guest.getName()
                                    : "Unknown Guest";

                    String roomNumber =
                            room != null
                                    ? room.getRoomNumber()
                                    : "Unknown Room";

                    recentBookings.add(
                            new RecentBookingResponse(
                                    booking.getId(),
                                    guestName,
                                    roomNumber,
                                    booking.getCheckIn(),
                                    booking.getCheckOut(),
                                    booking.getBookingDate(),
                                    booking.getStatus(),
                                    booking.getTotalAmount()
                            )
                    );
                });

        return recentBookings;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}