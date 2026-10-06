package com.derek.hotelrevenue.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.derek.hotelrevenue.enums.RoomStatus;
import com.derek.hotelrevenue.enums.RoomType;
import com.derek.hotelrevenue.exception.ResourceNotFoundException;
import com.derek.hotelrevenue.model.Hotel;
import com.derek.hotelrevenue.model.Room;
import com.derek.hotelrevenue.repository.RoomRepository;

class RoomServiceTest {

    private RoomRepository roomRepository;
    private RoomService roomService;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {

        roomRepository = mock(RoomRepository.class);

        roomService = new RoomService(
                roomRepository
        );

        hotel = new Hotel(
                "Test Hotel",
                "Pune",
                10
        );

        room = new Room(
                hotel,
                "101",
                RoomType.values()[0],
                new BigDecimal("5000.00")
        );
    }

    @Test
    void shouldUpdateRoomPrice() {

        when(
                roomRepository.findById(1L)
        ).thenReturn(Optional.of(room));

        when(
                roomRepository.save(room)
        ).thenReturn(room);

        Room updatedRoom =
                roomService.updateRoomPrice(
                        1L,
                        new BigDecimal("5750.00")
                );

        assertEquals(
                new BigDecimal("5750.00"),
                updatedRoom.getBasePrice()
        );

        verify(
                roomRepository
        ).save(room);
    }

    @Test
    void shouldRejectNullPrice() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        roomService.updateRoomPrice(
                                1L,
                                null
                        )
        );
    }

    @Test
    void shouldRejectZeroPrice() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        roomService.updateRoomPrice(
                                1L,
                                BigDecimal.ZERO
                        )
        );
    }

    @Test
    void shouldRejectNegativePrice() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        roomService.updateRoomPrice(
                                1L,
                                new BigDecimal("-100.00")
                        )
        );
    }

    @Test
    void shouldRejectUnknownRoom() {

        when(
                roomRepository.findById(999L)
        ).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () ->
                        roomService.updateRoomPrice(
                                999L,
                                new BigDecimal("5750.00")
                        )
        );
    }

    @Test
    void shouldUpdateRoomStatus() {

        when(
                roomRepository.findById(1L)
        ).thenReturn(Optional.of(room));

        when(
                roomRepository.save(room)
        ).thenReturn(room);

        Room updatedRoom =
                roomService.updateRoomStatus(
                        1L,
                        RoomStatus.AVAILABLE
                );

        assertEquals(
                RoomStatus.AVAILABLE,
                updatedRoom.getStatus()
        );

        verify(
                roomRepository
        ).save(room);
    }
}