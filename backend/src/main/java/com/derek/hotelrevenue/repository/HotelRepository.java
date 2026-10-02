package com.derek.hotelrevenue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.derek.hotelrevenue.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByLocationContainingIgnoreCase(String location);

    boolean existsByNameIgnoreCase(String name);
}