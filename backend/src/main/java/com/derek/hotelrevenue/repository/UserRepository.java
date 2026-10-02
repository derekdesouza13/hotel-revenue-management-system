package com.derek.hotelrevenue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.derek.hotelrevenue.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);
}