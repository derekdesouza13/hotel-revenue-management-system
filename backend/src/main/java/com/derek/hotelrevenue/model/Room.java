package com.derek.hotelrevenue.model;

import java.math.BigDecimal;

import com.derek.hotelrevenue.enums.RoomStatus;
import com.derek.hotelrevenue.enums.RoomType;

public class Room {

    private Long id;

    private Long hotelId;

    private String roomNumber;

    private RoomType roomType;

    private BigDecimal basePrice;

    private RoomStatus status;

    public Room() {
        this.status = RoomStatus.AVAILABLE;
    }

    public Room(
            Long id,
            Long hotelId,
            String roomNumber,
            RoomType roomType,
            BigDecimal basePrice
    ) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.status = RoomStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }
}