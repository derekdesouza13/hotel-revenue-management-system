package com.derek.hotelrevenue.controller;

import com.derek.hotelrevenue.dto.HotelRequest;
import com.derek.hotelrevenue.dto.HotelResponse;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {

        List<HotelResponse> response = hotelService.getAllHotels()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(
            @PathVariable Long id) {

        Hotel hotel = hotelService.getHotelById(id);

        return ResponseEntity.ok(toResponse(hotel));
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelResponse>> searchHotels(
            @RequestParam String location) {

        List<HotelResponse> response =
                hotelService.searchHotelsByLocation(location)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(
            @RequestBody HotelRequest request) {

        Hotel hotel = new Hotel(
                request.getName(),
                request.getLocation(),
                request.getTotalRooms()
        );

        Hotel savedHotel = hotelService.createHotel(hotel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedHotel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(
            @PathVariable Long id,
            @RequestBody HotelRequest request) {

        Hotel updatedHotel = new Hotel(
                request.getName(),
                request.getLocation(),
                request.getTotalRooms()
        );

        Hotel hotel = hotelService.updateHotel(id, updatedHotel);

        return ResponseEntity.ok(toResponse(hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(
            @PathVariable Long id) {

        hotelService.deleteHotel(id);

        return ResponseEntity.noContent().build();
    }

    private HotelResponse toResponse(Hotel hotel) {

        return new HotelResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getLocation(),
                hotel.getTotalRooms()
        );
    }
}