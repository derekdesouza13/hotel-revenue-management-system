package com.derek.hotelrevenue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.derek.hotelrevenue.enums.RoomStatus;
import com.derek.hotelrevenue.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    List<Room> findByStatus(RoomStatus status);

    List<Room> findByHotelIdAndStatus(
            Long hotelId,
            RoomStatus status
    );

    boolean existsByHotelIdAndRoomNumber(
            Long hotelId,
            String roomNumber
    );
}