package com.derek.hotelrevenue.dto;

public class HotelResponse {

    private Long id;
    private String name;
    private String location;
    private Integer totalRooms;

    public HotelResponse() {
    }

    public HotelResponse(
            Long id,
            String name,
            String location,
            Integer totalRooms
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalRooms = totalRooms;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Integer getTotalRooms() {
        return totalRooms;
    }
}