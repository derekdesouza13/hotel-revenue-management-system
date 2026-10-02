package com.derek.hotelrevenue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.derek.hotelrevenue.exception.ResourceNotFoundException;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.repository.HotelRepository;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
    }

    public List<Hotel> searchHotelsByLocation(String location) {
        return hotelRepository.findByLocationContainingIgnoreCase(location);
    }

    public Hotel createHotel(Hotel hotel) {
        if (hotelRepository.existsByNameIgnoreCase(hotel.getName())) {
            throw new IllegalArgumentException(
        "Hotel already exists with name: " + hotel.getName());
        }

        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Hotel existingHotel = getHotelById(id);

        existingHotel.setName(updatedHotel.getName());
        existingHotel.setLocation(updatedHotel.getLocation());
        existingHotel.setTotalRooms(updatedHotel.getTotalRooms());

        return hotelRepository.save(existingHotel);
    }

    public void deleteHotel(Long id) {
        Hotel hotel = getHotelById(id);
        hotelRepository.delete(hotel);
    }
}