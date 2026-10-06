package com.derek.hotelrevenue.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.derek.hotelrevenue.enums.RoomStatus;
import com.derek.hotelrevenue.exception.ResourceNotFoundException;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: " + id
                        )
                );
    }

    public List<Room> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    public List<Room> getAvailableRooms(Long hotelId) {
        return roomRepository.findByHotelIdAndStatus(
                hotelId,
                RoomStatus.AVAILABLE
        );
    }

    public Room createRoom(Room room) {

        if (roomRepository.existsByHotelIdAndRoomNumber(
                room.getHotel().getId(),
                room.getRoomNumber())) {

            throw new IllegalArgumentException(
                    "Room number already exists in this hotel"
            );
        }

        return roomRepository.save(room);
    }

    public Room updateRoomStatus(
            Long id,
            RoomStatus status
    ) {

        Room room = getRoomById(id);

        room.setStatus(status);

        return roomRepository.save(room);
    }

    public Room updateRoomPrice(
            Long roomId,
            BigDecimal newPrice
    ) {

        if (newPrice == null) {
            throw new IllegalArgumentException(
                    "New price cannot be null"
            );
        }

        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "New price must be greater than zero"
            );
        }

        Room room = getRoomById(roomId);

        room.setBasePrice(newPrice);

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = getRoomById(id);

        roomRepository.delete(room);
    }
}