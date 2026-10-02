package com.derek.hotelrevenue.dto;

import java.math.BigDecimal;

import com.derek.hotelrevenue.enums.RoomStatus;
import com.derek.hotelrevenue.enums.RoomType;

public class RoomResponse {

    private Long id;
    private Long hotelId;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal basePrice;
    private RoomStatus status;

    public RoomResponse() {
    }

    public RoomResponse(
            Long id,
            Long hotelId,
            String roomNumber,
            RoomType roomType,
            BigDecimal basePrice,
            RoomStatus status
    ) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public RoomStatus getStatus() {
        return status;
    }
}