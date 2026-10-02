package com.derek.hotelrevenue.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.derek.hotelrevenue.enums.BookingStatus;

public class BookingResponse {

    private Long id;
    private Long hotelId;
    private Long roomId;
    private Long guestId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private long numberOfNights;

    public BookingResponse() {
    }

    public BookingResponse(
            Long id,
            Long hotelId,
            Long roomId,
            Long guestId,
            LocalDate checkIn,
            LocalDate checkOut,
            LocalDateTime bookingDate,
            BookingStatus status,
            BigDecimal totalAmount,
            long numberOfNights
    ) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.guestId = guestId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.numberOfNights = numberOfNights;
    }

    public Long getId() {
        return id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getGuestId() {
        return guestId;
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

    public long getNumberOfNights() {
        return numberOfNights;
    }
}