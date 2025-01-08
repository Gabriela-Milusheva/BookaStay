package com.hotelmanager.services;

import com.hotelmanager.dtos.hotel.CreateHotelDTO;
import com.hotelmanager.dtos.hotel.HotelDTO;
import com.hotelmanager.dtos.room.CreateRoomDTO;
import com.hotelmanager.dtos.room.RoomDTO;
import com.hotelmanager.mappers.HotelMapper;
import com.hotelmanager.mappers.RoomMapper;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Room;
import com.hotelmanager.repositories.HotelRepository;
import com.hotelmanager.repositories.RoomRepository;
import com.hotelmanager.utils.RoomNumberUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private HotelService hotelService;

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
    void createHotel_ShouldReturnCreatedHotel_WhenNameIsUnique() {
        // Arrange
        CreateHotelDTO createHotelDTO = new CreateHotelDTO("Grand Plaza", 5);

        Hotel mockHotel = new Hotel();
        mockHotel.setName("Grand Plaza");
        mockHotel.setStarRating(5);

        UUID hotelId = UUID.randomUUID();
        HotelDTO expectedDto = new HotelDTO("Grand Plaza", 5, new ArrayList<>() {
        });

        when(hotelRepository.existsByName("Grand Plaza")).thenReturn(false);
        when(hotelMapper.toEntity(createHotelDTO)).thenReturn(mockHotel);
        when(hotelRepository.save(mockHotel)).thenReturn(mockHotel);
        when(hotelMapper.toDto(mockHotel)).thenReturn(expectedDto);

        // Act
        HotelDTO result = hotelService.createHotel(createHotelDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Grand Plaza", result.name());
        assertEquals(5, result.starRating());
        verify(hotelRepository).existsByName("Grand Plaza");
        verify(hotelMapper).toEntity(createHotelDTO);
        verify(hotelRepository).save(mockHotel);
        verify(hotelMapper).toDto(mockHotel);
    }

    @Test
    void createHotel_ShouldThrowException_WhenNameAlreadyExists() {
        // Arrange
        CreateHotelDTO createHotelDTO = new CreateHotelDTO("Grand Plaza", 5);

        when(hotelRepository.existsByName("Grand Plaza")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.createHotel(createHotelDTO);
        });

        assertEquals("Hotel with name Grand Plaza already exists.", exception.getMessage());

        //We don't waste time and resources by mapping
        verify(hotelMapper, never()).toEntity(any());

        //Make sure the database is untouched
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void updateStarRatingByName_ShouldUpdateStarRating_WhenHotelExists() {
        // Arrange
        String hotelName = "Grand Plaza";
        int newStarRating = 4;

        when(hotelRepository.existsByName(hotelName)).thenReturn(true);

        // Act
        hotelService.updateStarRatingByName(hotelName, newStarRating);

        // Assert
        verify(hotelRepository).existsByName(hotelName);
        verify(hotelRepository).updateStarRatingByName(hotelName, newStarRating);
    }

    @Test
    void updateStarRatingByName_ShouldThrowException_WhenHotelDoesNotExist() {
        // Arrange
        String hotelName = "Nonexistent Hotel";
        int newStarRating = 3;

        when(hotelRepository.existsByName(hotelName)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.updateStarRatingByName(hotelName, newStarRating);
        });

        assertEquals("Hotel with name Nonexistent Hotel not found.", exception.getMessage());

        verify(hotelRepository, never()).updateStarRatingByName(anyString(), anyInt());
    }

    @Test
    void deleteHotelByName_ShouldReturnTrue_WhenHotelExists() {
        // Arrange
        String hotelName = "Grand Plaza";
        Hotel mockHotel = new Hotel();
        mockHotel.setName(hotelName);

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.of(mockHotel));

        // Act
        boolean result = hotelService.deleteHotelByName(hotelName);

        // Assert
        assertTrue(result);
        verify(hotelRepository).findByName(hotelName);
        verify(hotelRepository).delete(mockHotel);
    }

    @Test
    void deleteHotelByName_ShouldReturnFalse_WhenHotelDoesNotExist() {
        // Arrange
        String hotelName = "Nonexistent Hotel";

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.empty());

        // Act
        boolean result = hotelService.deleteHotelByName(hotelName);

        // Assert
        assertFalse(result);
        verify(hotelRepository).findByName(hotelName);
        verify(hotelRepository, never()).delete(any());
    }

    @Test
    void addRoomsToHotel_ShouldAddRooms_WhenHotelExists() {
        // Arrange
        String hotelName = "Grand Plaza";
        CreateRoomDTO roomDTO = new CreateRoomDTO(101, 2, 150.0, 3);

        Hotel mockHotel = new Hotel();
        mockHotel.setName(hotelName);

        Set<Integer> existingRoomNumbers = Set.of(101, 102);
        List<Integer> newRoomNumbers = List.of(103, 104, 105);
        ArrayList<Room> rooms = new ArrayList<>();

        Room roomOne = new Room();
        roomOne.setNumber(101);
        rooms.add(roomOne);

        Room roomTwo = new Room();
        roomTwo.setNumber(102);
        rooms.add(roomTwo);

        Room mockRoom = new Room();

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.of(mockHotel));
        when(roomRepository.findByHotel(mockHotel)).thenReturn(rooms);
        mockStatic(RoomNumberUtil.class);
        when(RoomNumberUtil.findNextAvailableRoomNumbers(existingRoomNumbers, 101, 3)).thenReturn(newRoomNumbers);
        when(roomMapper.toEntity(roomDTO)).thenReturn(mockRoom);

        // Act
        hotelService.addRoomsToHotel(hotelName, roomDTO);

        // Assert
        verify(hotelRepository).findByName(hotelName);
        verify(roomRepository).findByHotel(mockHotel);
        verify(roomRepository, times(3)).save(any(Room.class));
    }

    @Test
    void getRoomsByHotelName_ShouldReturnRoomDTOs_WhenHotelExists() {
        // Arrange
        String hotelName = "Grand Plaza";

        Hotel mockHotel = new Hotel();
        mockHotel.setName(hotelName);

        Room mockRoom1 = new Room();
        mockRoom1.setNumber(101);
        mockRoom1.setBedCapacity(2);
        mockRoom1.setPricePerNight(150.0);

        Room mockRoom2 = new Room();
        mockRoom2.setNumber(102);
        mockRoom2.setBedCapacity(3);
        mockRoom2.setPricePerNight(200.0);

        RoomDTO roomDTO1 = new RoomDTO(101, 2, 150.0);
        RoomDTO roomDTO2 = new RoomDTO(102, 3, 200.0);

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.of(mockHotel));
        when(roomRepository.findByHotel(mockHotel)).thenReturn(List.of(mockRoom1, mockRoom2));
        when(roomMapper.toDto(mockRoom1)).thenReturn(roomDTO1);
        when(roomMapper.toDto(mockRoom2)).thenReturn(roomDTO2);

        // Act
        List<RoomDTO> result = hotelService.getRoomsByHotelName(hotelName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(roomDTO1, result.get(0));
        assertEquals(roomDTO2, result.get(1));

        verify(hotelRepository).findByName(hotelName);
        verify(roomRepository).findByHotel(mockHotel);
        verify(roomMapper).toDto(mockRoom1);
        verify(roomMapper).toDto(mockRoom2);
    }

    @Test
    void getRoomsByHotelName_ShouldThrowException_WhenHotelDoesNotExist() {
        // Arrange
        String hotelName = "Nonexistent Hotel";

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.getRoomsByHotelName(hotelName);
        });

        assertEquals("Hotel with name Nonexistent Hotel not found.", exception.getMessage());

        verify(hotelRepository).findByName(hotelName);
    }
}