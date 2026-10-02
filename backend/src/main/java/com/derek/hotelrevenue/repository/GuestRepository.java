package com.derek.hotelrevenue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.derek.hotelrevenue.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}