package com.derek.hotelrevenue.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.derek.hotelrevenue.enums.BookingStatus;

public class RecentBookingResponse {

    private final Long id;
    private final String guestName;
    private final String roomNumber;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final LocalDateTime bookingDate;
    private final BookingStatus status;
    private final BigDecimal totalAmount;

    public RecentBookingResponse(
            Long id,
            String guestName,
            String roomNumber,
            LocalDate checkIn,
            LocalDate checkOut,
            LocalDateTime bookingDate,
            BookingStatus status,
            BigDecimal totalAmount
    ) {
        this.id = id;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}