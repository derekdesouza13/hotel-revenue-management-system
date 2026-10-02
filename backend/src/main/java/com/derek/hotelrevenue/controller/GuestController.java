package com.derek.hotelrevenue.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.derek.hotelrevenue.dto.GuestRequest;
import com.derek.hotelrevenue.dto.GuestResponse;
import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.service.GuestService;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public ResponseEntity<List<GuestResponse>> getAllGuests() {

        List<GuestResponse> response = guestService.getAllGuests()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuestById(
            @PathVariable Long id) {

        Guest guest = guestService.getGuestById(id);

        return ResponseEntity.ok(toResponse(guest));
    }

    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(
            @RequestBody GuestRequest request) {

        Guest guest = new Guest(
                request.getName(),
                request.getEmail(),
                request.getPhone()
        );

        Guest savedGuest = guestService.createGuest(guest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(savedGuest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(
            @PathVariable Long id,
            @RequestBody GuestRequest request) {

        Guest updatedGuest = new Guest(
                request.getName(),
                request.getEmail(),
                request.getPhone()
        );

        Guest guest = guestService.updateGuest(id, updatedGuest);

        return ResponseEntity.ok(toResponse(guest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(
            @PathVariable Long id) {

        guestService.deleteGuest(id);

        return ResponseEntity.noContent().build();
    }

    private GuestResponse toResponse(Guest guest) {

        return new GuestResponse(
                guest.getId(),
                guest.getName(),
                guest.getEmail(),
                guest.getPhone()
        );
    }
}