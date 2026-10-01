package com.derek.hotelrevenue.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "hotels",
        indexes = {
                @Index(name = "idx_hotel_location", columnList = "location")
        }
)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(name = "total_rooms", nullable = false)
    private int totalRooms;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Hotel() {
        // Required by JPA
    }

    public Hotel(
            String name,
            String location,
            int totalRooms
    ) {
        this.name = name;
        this.location = location;
        this.totalRooms = totalRooms;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}