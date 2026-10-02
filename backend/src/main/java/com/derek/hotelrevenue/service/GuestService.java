package com.derek.hotelrevenue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.derek.hotelrevenue.exception.ResourceNotFoundException;
import com.derek.hotelrevenue.model.Guest;
import com.derek.hotelrevenue.repository.GuestRepository;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

   public Guest getGuestById(Long id) {
    return guestRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Guest not found with id: " + id)
            );
}

    public Guest createGuest(Guest guest) {

        if (guestRepository.existsByEmailIgnoreCase(guest.getEmail())) {
            throw new IllegalArgumentException(
                    "Guest already exists with email: " + guest.getEmail()
            );
        }

        return guestRepository.save(guest);
    }

    public Guest updateGuest(Long id, Guest updatedGuest) {

        Guest existingGuest = getGuestById(id);

        existingGuest.setName(updatedGuest.getName());
        existingGuest.setName(updatedGuest.getName());
        existingGuest.setEmail(updatedGuest.getEmail());
        existingGuest.setPhone(updatedGuest.getPhone());

        return guestRepository.save(existingGuest);
    }

    public void deleteGuest(Long id) {

        Guest guest = getGuestById(id);

        guestRepository.delete(guest);
    }
}