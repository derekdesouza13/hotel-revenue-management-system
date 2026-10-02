package com.derek.hotelrevenue.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.derek.hotelrevenue.dto.BookingRequest;
import com.derek.hotelrevenue.dto.BookingResponse;
import com.derek.hotelrevenue.model.Booking;
import com.derek.hotelrevenue.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {

        List<BookingResponse> response =
                bookingService.getAllBookings()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long id) {

        Booking booking = bookingService.getBookingById(id);

        return ResponseEntity.ok(toResponse(booking));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByHotel(
            @PathVariable Long hotelId) {

        List<BookingResponse> response =
                bookingService.getBookingsByHotel(hotelId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByRoom(
            @PathVariable Long roomId) {

        List<BookingResponse> response =
                bookingService.getBookingsByRoom(roomId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest request) {

        Booking booking = bookingService.createBooking(
                request.getHotelId(),
                request.getRoomId(),
                request.getGuestId(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(booking));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id) {

        Booking booking = bookingService.cancelBooking(id);

        return ResponseEntity.ok(toResponse(booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.noContent().build();
    }

    private BookingResponse toResponse(Booking booking) {

        return new BookingResponse(
                booking.getId(),
                booking.getHotel().getId(),
                booking.getRoom().getId(),
                booking.getGuest().getId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getTotalAmount(),
                booking.getNumberOfNights()
        );
    }
}