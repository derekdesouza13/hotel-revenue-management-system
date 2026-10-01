package com.derek.hotelrevenue.model;

import java.time.LocalDateTime;

public class Hotel {

    private Long id;

    private String name;

    private String location;

    private int totalRooms;

    private LocalDateTime createdAt;

    public Hotel() {
        this.createdAt = LocalDateTime.now();
    }

    public Hotel(
            Long id,
            String name,
            String location,
            int totalRooms
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalRooms = totalRooms;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}