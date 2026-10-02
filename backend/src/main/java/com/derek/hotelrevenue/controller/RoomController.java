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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.derek.hotelrevenue.dto.RoomRequest;
import com.derek.hotelrevenue.dto.RoomResponse;
import com.derek.hotelrevenue.enums.RoomStatus;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.service.HotelService;
import com.derek.hotelrevenue.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HotelService hotelService;

    public RoomController(
            RoomService roomService,
            HotelService hotelService
    ) {
        this.roomService = roomService;
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {

        List<RoomResponse> response = roomService.getAllRooms()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(
            @PathVariable Long id) {

        Room room = roomService.getRoomById(id);

        return ResponseEntity.ok(toResponse(room));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByHotel(
            @PathVariable Long hotelId) {

        List<RoomResponse> response = roomService
                .getRoomsByHotel(hotelId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @PathVariable Long hotelId) {

        List<RoomResponse> response = roomService
                .getAvailableRooms(hotelId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @RequestBody RoomRequest request) {

        Hotel hotel = hotelService.getHotelById(request.getHotelId());

        Room room = new Room(
                hotel,
                request.getRoomNumber(),
                request.getRoomType(),
                request.getBasePrice()
        );

        Room savedRoom = roomService.createRoom(room);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedRoom));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RoomResponse> updateRoomStatus(
            @PathVariable Long id,
            @RequestParam RoomStatus status) {

        Room room = roomService.updateRoomStatus(id, status);

        return ResponseEntity.ok(toResponse(room));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id) {

        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }

    private RoomResponse toResponse(Room room) {

        return new RoomResponse(
                room.getId(),
                room.getHotel().getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getBasePrice(),
                room.getStatus()
        );
    }
}