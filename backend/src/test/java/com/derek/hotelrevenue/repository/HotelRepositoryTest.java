package com.derek.hotelrevenue.repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.derek.hotelrevenue.model.Hotel;

@SpringBootTest
@ActiveProfiles("test")
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindHotel() {

        Hotel hotel = new Hotel(
                "Test Pune Hotel",
                "Pune",
                100
        );

        Hotel savedHotel = hotelRepository.save(hotel);

        assertNotNull(savedHotel.getId());

        Hotel foundHotel =
                hotelRepository
                        .findById(savedHotel.getId())
                        .orElseThrow();

        assertEquals(
                "Test Pune Hotel",
                foundHotel.getName()
        );
    }

    @Test
    void shouldFindHotelsByLocation() {

        hotelRepository.save(
                new Hotel(
                        "Pune Hotel",
                        "Pune",
                        100
                )
        );

        hotelRepository.save(
                new Hotel(
                        "Mumbai Hotel",
                        "Mumbai",
                        150
                )
        );

        List<Hotel> hotels =
                hotelRepository
                        .findByLocationContainingIgnoreCase("pune");

        assertEquals(1, hotels.size());

        assertEquals(
                "Pune Hotel",
                hotels.get(0).getName()
        );
    }
}