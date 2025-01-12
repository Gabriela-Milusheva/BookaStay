package com.hotelmanager.services;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hotelmanager.dtos.room.CreateRoomDto;
import com.hotelmanager.dtos.room.RoomDto;
import com.hotelmanager.dtos.room.UpdateRoomDto;
import com.hotelmanager.enumerations.room.RoomTypes;
import com.hotelmanager.exception.room.RoomCustomException.RoomNotFoundException;
import com.hotelmanager.mappers.RoomMapper;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Room;
import com.hotelmanager.repositories.HotelRepository;
import com.hotelmanager.repositories.RoomRepository;

public class RoomServiceTests {

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
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void createRoom_ShouldCreateRoom_WhenHotelExists() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        Hotel mockHotel = new Hotel();
        mockHotel.setId(hotelId);
        CreateRoomDto createRoomDto = new CreateRoomDto(
                101, 2, 150.0, "Double", "Ocean View Room", 3, "Ocean", 25.5,
                "Free Wi-Fi, Mini Bar", 4, hotelId
        );

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));
        when(roomRepository.existsByHotelIdAndNumber(hotelId, createRoomDto.getNumber())).thenReturn(false);

        Room mockRoom = new Room();
        when(roomMapper.toEntity(createRoomDto)).thenReturn(mockRoom);
        when(roomRepository.save(mockRoom)).thenReturn(mockRoom);
        when(roomMapper.toDto(mockRoom)).thenReturn(new RoomDto(
                mockRoom.getId(), createRoomDto.getNumber(), createRoomDto.getBedCapacity(),
                createRoomDto.getPricePerNight(), "Double", createRoomDto.getDescription(),
                createRoomDto.getFloor(), createRoomDto.getView(), createRoomDto.getSize(),
                createRoomDto.getAmenities(), createRoomDto.getMaxOccupants(), createRoomDto.getHotelId()
        ));

        // Act
        RoomDto result = roomService.createRoom(hotelId, createRoomDto);

        // Assert
        assertNotNull(result);
        assertEquals(101, result.getNumber());
        assertEquals(2, result.getBedCapacity());
        assertEquals(150.0, result.getPricePerNight());
        assertEquals("Double", result.getRoomType());
        assertEquals("Ocean View Room", result.getDescription());

        verify(hotelRepository).findById(hotelId);
        verify(roomRepository).existsByHotelIdAndNumber(hotelId, createRoomDto.getNumber());
        verify(roomRepository).save(mockRoom);
        verify(roomMapper).toDto(mockRoom);
    }

    @Test
    void deleteRoomByRoomId_ShouldDeleteRoom_WhenRoomExists() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        Room mockRoom = new Room();
        mockRoom.setId(roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));

        // Act
        boolean result = roomService.deleteRoomByRoomId(roomId);

        // Assert
        assertTrue(result);
        verify(roomRepository).findById(roomId);
        verify(roomRepository).delete(mockRoom);
    }

    @Test
    void deleteRoomByRoomId_ShouldThrowException_WhenRoomDoesNotExist() {
        // Arrange
        UUID roomId = UUID.randomUUID();

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            roomService.deleteRoomByRoomId(roomId);
        });

        assertEquals(String.format("Room with ID %s not found.", roomId), exception.getMessage());

        verify(roomRepository).findById(roomId);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void getRoomByRoomId_ShouldReturnRoomDTO_WhenRoomExists() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        Room mockRoom = new Room();
        mockRoom.setId(roomId);
        mockRoom.setNumber(101);
        mockRoom.setBedCapacity(2);
        mockRoom.setPricePerNight(150.0);
        mockRoom.setRoomType("Double");
        mockRoom.setDescription("Ocean View Room");
        UUID hotelId = UUID.randomUUID();

        RoomDto expectedDto = new RoomDto(
                roomId, 101, 2, 150.0, "Double", "Ocean View Room", 3, "Ocean",
                25.5, "Free Wi-Fi, Mini Bar", 4, hotelId
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));
        when(roomMapper.toDto(mockRoom)).thenReturn(expectedDto);

        // Act
        RoomDto result = roomService.getRoomByRoomId(roomId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(roomRepository).findById(roomId);
        verify(roomMapper).toDto(mockRoom);
    }

    @Test
    void getRoomByRoomId_ShouldThrowException_WhenRoomDoesNotExist() {
        // Arrange
        UUID roomId = UUID.randomUUID();

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            roomService.getRoomByRoomId(roomId);
        });

        assertEquals(String.format("Room with ID %s not found.", roomId), exception.getMessage());

        verify(roomRepository).findById(roomId);
        verifyNoInteractions(roomMapper);
    }

    @Test
    void updateRoomByRoomId_ShouldUpdateRoom_WhenRoomExists() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        Room mockRoom = new Room();
        mockRoom.setId(roomId);
        mockRoom.setNumber(101);
        mockRoom.setBedCapacity(2);
        mockRoom.setPricePerNight(150.0);
        mockRoom.setRoomType("Double");
        mockRoom.setDescription("Ocean View Room");

        UpdateRoomDto updateRoomDto = new UpdateRoomDto(
                101, 3, 200.0, "Single", "Mountain View Room", 5, "Mountain", 30.0,
                "Free Wi-Fi, Coffee Machine", 5
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(mockRoom));

        // Act
        roomService.updateRoomByRoomId(roomId, updateRoomDto);

        // Assert
        assertEquals(3, mockRoom.getBedCapacity());
        assertEquals(200.0, mockRoom.getPricePerNight());
        assertEquals("Double", mockRoom.getRoomType());
        assertEquals("Ocean View Room", mockRoom.getDescription());

        verify(roomRepository).findById(roomId);
        verify(roomRepository).save(mockRoom);
    }

    @Test
    void updateRoomByRoomId_ShouldThrowException_WhenRoomDoesNotExist() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        UpdateRoomDto updateRoomDto = new UpdateRoomDto(
                101, 3, 200.0, "Single", "Mountain View Room", 5, "Mountain", 30.0,
                "Free Wi-Fi, Coffee Machine", 5
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            roomService.updateRoomByRoomId(roomId, updateRoomDto);
        });

        assertEquals(String.format("Room with ID %s not found.", roomId), exception.getMessage());

        verify(roomRepository).findById(roomId);
        verifyNoMoreInteractions(roomRepository);
    }
}
