package com.hotelmanager.services;


import com.hotelmanager.dtos.room.RoomDTO;
import com.hotelmanager.dtos.room.UpdateRoomDTO;
import com.hotelmanager.mappers.RoomMapper;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Room;
import com.hotelmanager.repositories.HotelRepository;
import com.hotelmanager.repositories.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomMapper roomMapper;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        // Initializes mocks and injects them into the test class
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void deleteRoomByHotelNameAndNumber_ShouldDeleteRoom_WhenHotelAndRoomExist() {
        // Arrange
        String hotelName = "Grand Plaza";
        int roomNumber = 101;

        Hotel mockHotel = new Hotel();
        mockHotel.setName(hotelName);

        Room mockRoom = new Room();
        mockRoom.setNumber(roomNumber);

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.of(mockHotel));
        when(roomRepository.findByHotelAndNumber(mockHotel, roomNumber)).thenReturn(Optional.of(mockRoom));

        // Act
        boolean result = roomService.deleteRoomByHotelNameAndNumber(hotelName, roomNumber);

        // Assert
        assertTrue(result);
        verify(hotelRepository).findByName(hotelName);
        verify(roomRepository).findByHotelAndNumber(mockHotel, roomNumber);
        verify(roomRepository).delete(mockRoom);
    }

    @Test
    void deleteRoomByHotelNameAndNumber_ShouldThrowException_WhenHotelDoesNotExist() {
        // Arrange
        String hotelName = "Nonexistent Hotel";
        int roomNumber = 101;

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.deleteRoomByHotelNameAndNumber(hotelName, roomNumber);
        });

        assertEquals("Hotel with name Nonexistent Hotel not found.", exception.getMessage());

        verify(hotelRepository).findByName(hotelName);
        verifyNoInteractions(roomRepository);
    }

    @Test
    void deleteRoomByHotelNameAndNumber_ShouldThrowException_WhenRoomDoesNotExist() {
        // Arrange
        String hotelName = "Grand Plaza";
        int roomNumber = 101;

        Hotel mockHotel = new Hotel();
        mockHotel.setName(hotelName);

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.of(mockHotel));
        when(roomRepository.findByHotelAndNumber(mockHotel, roomNumber)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.deleteRoomByHotelNameAndNumber(hotelName, roomNumber);
        });

        assertEquals("Room number 101 not found in hotel Grand Plaza", exception.getMessage());

        verify(hotelRepository).findByName(hotelName);
        verify(roomRepository).findByHotelAndNumber(mockHotel, roomNumber);
    }

    @Test
    void getRoomByHotelNameAndNumber_ShouldReturnRoomDTO_WhenRoomExists() {
        // Arrange
        String hotelName = "Grand Plaza";
        int roomNumber = 101;

        Room mockRoom = new Room();
        mockRoom.setNumber(roomNumber);
        mockRoom.setBedCapacity(2);
        mockRoom.setPricePerNight(150.0);

        RoomDTO expectedDto = new RoomDTO(roomNumber, 2, 150.0);

        when(roomRepository.findByHotelNameAndNumber(hotelName, roomNumber)).thenReturn(Optional.of(mockRoom));
        when(roomMapper.toDto(mockRoom)).thenReturn(expectedDto);

        // Act
        RoomDTO result = roomService.getRoomByHotelNameAndNumber(hotelName, roomNumber);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(roomRepository).findByHotelNameAndNumber(hotelName, roomNumber);
        verify(roomMapper).toDto(mockRoom);
    }

    @Test
    void getRoomByHotelNameAndNumber_ShouldThrowException_WhenRoomDoesNotExist() {
        // Arrange
        String hotelName = "Grand Plaza";
        int roomNumber = 101;

        when(roomRepository.findByHotelNameAndNumber(hotelName, roomNumber)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.getRoomByHotelNameAndNumber(hotelName, roomNumber);
        });

        assertEquals("Room not found for hotel Grand Plaza with number 101", exception.getMessage());

        verify(roomRepository).findByHotelNameAndNumber(hotelName, roomNumber);
    }

    @Test
    void updateRoomByHotelNameAndNumber_ShouldUpdateRoom_WhenRoomExists() {
        // Arrange
        String hotelName = "Grand Plaza";
        int roomNumber = 101;

        Room mockRoom = new Room();
        mockRoom.setNumber(roomNumber);
        mockRoom.setBedCapacity(2);
        mockRoom.setPricePerNight(150.0);

        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO(3, 200.0);

        when(roomRepository.findByHotelNameAndNumber(hotelName, roomNumber)).thenReturn(Optional.of(mockRoom));

        // Act
        roomService.updateRoomByHotelNameAndNumber(hotelName, roomNumber, updateRoomDTO);

        // Assert
        assertEquals(3, mockRoom.getBedCapacity());
        assertEquals(200.0, mockRoom.getPricePerNight());

        verify(roomRepository).findByHotelNameAndNumber(hotelName, roomNumber);
        verify(roomRepository).save(mockRoom);
    }

    @Test
    void updateRoomByHotelNameAndNumber_ShouldThrowException_WhenRoomDoesNotExist() {
        // Arrange
        String hotelName = "Grand Plaza";
        int roomNumber = 101;

        UpdateRoomDTO updateRoomDTO = new UpdateRoomDTO(3, 200.0);

        when(roomRepository.findByHotelNameAndNumber(hotelName, roomNumber)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            roomService.updateRoomByHotelNameAndNumber(hotelName, roomNumber, updateRoomDTO);
        });

        assertEquals("Room not found for hotel Grand Plaza with number 101", exception.getMessage());

        verify(roomRepository).findByHotelNameAndNumber(hotelName, roomNumber);
    }
}

