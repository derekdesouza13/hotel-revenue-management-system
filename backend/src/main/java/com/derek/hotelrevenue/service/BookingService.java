package com.derek.hotelrevenue.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.derek.hotelrevenue.enums.BookingStatus;
import com.derek.hotelrevenue.exception.ResourceNotFoundException;
import com.derek.hotelrevenue.model.Booking;
import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.BookingRepository;
import com.derek.hotelrevenue.repository.GuestRepository;
import com.derek.hotelrevenue.repository.HotelRepository;
import com.derek.hotelrevenue.repository.RoomRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public BookingService(
            BookingRepository bookingRepository,
            HotelRepository hotelRepository,
            RoomRepository roomRepository,
            GuestRepository guestRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id
                        ));
    }

    public List<Booking> getBookingsByHotel(Long hotelId) {
        return bookingRepository.findByHotelId(hotelId);
    }

    public List<Booking> getBookingsByRoom(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public Booking createBooking(
            Long hotelId,
            Long roomId,
            Long guestId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        validateBookingDates(checkIn, checkOut);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hotel not found with id: " + hotelId
                        ));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: " + roomId
                        ));

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with id: " + guestId
                        ));

        if (!room.getHotel().getId().equals(hotelId)) {
            throw new IllegalArgumentException(
                    "Room does not belong to the selected hotel"
            );
        }

        boolean roomAlreadyBooked =
                bookingRepository
                        .existsByRoomIdAndCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
                                roomId,
                                checkOut,
                                checkIn,
                                BookingStatus.CANCELLED
                        );

        if (roomAlreadyBooked) {
            throw new IllegalArgumentException(
                    "Room is already booked for the selected dates"
            );
        }

        long numberOfNights =
                checkIn.until(checkOut).getDays();

        BigDecimal totalAmount =
                room.getBasePrice()
                        .multiply(BigDecimal.valueOf(numberOfNights));

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

    public Booking cancelBooking(Long id) {

        Booking booking = getBookingById(id);

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {

        Booking booking = getBookingById(id);

        bookingRepository.delete(booking);
    }

    private void validateBookingDates(
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException(
                    "Check-in and check-out dates are required"
            );
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }
    }
}