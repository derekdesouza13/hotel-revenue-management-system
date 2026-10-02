package com.derek.hotelrevenue.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.derek.hotelrevenue.enums.BookingStatus;
import com.derek.hotelrevenue.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByHotelId(Long hotelId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByRoomIdAndStatusNot(
            Long roomId,
            BookingStatus status
    );

    boolean existsByRoomIdAndCheckInLessThanAndCheckOutGreaterThanAndStatusNot(
            Long roomId,
            LocalDate checkOut,
            LocalDate checkIn,
            BookingStatus status
    );
}