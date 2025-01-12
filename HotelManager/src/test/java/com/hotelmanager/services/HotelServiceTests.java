package com.hotelmanager.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hotelmanager.dtos.hotel.CreateHotelDTO;
import com.hotelmanager.dtos.hotel.HotelDTO;
import com.hotelmanager.dtos.hotel.UpdateHotelDto;
import com.hotelmanager.dtos.review.ReviewDto;
import com.hotelmanager.dtos.room.RoomDto;
import com.hotelmanager.exception.hotel.HotelCustomException;
import com.hotelmanager.mappers.HotelMapper;
import com.hotelmanager.mappers.ReviewMapper;
import com.hotelmanager.mappers.RoomMapper;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Review;
import com.hotelmanager.models.Room;
import com.hotelmanager.repositories.HotelRepository;
import com.hotelmanager.repositories.ReviewRepository;
import com.hotelmanager.repositories.RoomRepository;

public class HotelServiceTests {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private HotelService hotelService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    private Hotel mockHotel(UUID hotelId, String name) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setName(name);
        return hotel;
    }

    private Room mockRoom(int number, int bedCapacity, double pricePerNight) {
        Room room = new Room();
        room.setNumber(number);
        room.setBedCapacity(bedCapacity);
        room.setPricePerNight(pricePerNight);
        return room;
    }

    private Review mockReview(Hotel hotel, String title, String comment, int rating) {
        Review review = new Review();
        review.setHotel(hotel);
        review.setReviewTitle(title);
        review.setReviewDesc(comment);
        review.setRating(rating);
        return review;
    }

    @Test
    void createHotel_ShouldReturnCreatedHotel_WhenNameIsUnique() {
        // Arrange
        CreateHotelDTO createHotelDTO = new CreateHotelDTO();
        createHotelDTO.setName("Grand Plaza");
        createHotelDTO.setStarRating(5);
        
        Hotel mockHotel = new Hotel();
        mockHotel.setName("Grand Plaza");
        mockHotel.setStarRating(5);
        
        HotelDTO expectedDto = new HotelDTO();
        expectedDto.setName("Grand Plaza");
        expectedDto.setStarRating(5);
        expectedDto.setRooms(new ArrayList<>());
        
        // Mock repository calls
        when(hotelRepository.existsByName("Grand Plaza")).thenReturn(false);
        when(hotelMapper.toEntity(createHotelDTO)).thenReturn(mockHotel);
        when(hotelRepository.save(mockHotel)).thenReturn(mockHotel);
        when(hotelMapper.toDto(mockHotel)).thenReturn(expectedDto);
        
        // Act
        HotelDTO result = hotelService.createHotel(createHotelDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals("Grand Plaza", result.getName());
        assertEquals(5, result.getStarRating());
        assertEquals(0, result.getRooms().size()); 
        verify(hotelRepository).existsByName("Grand Plaza");
        verify(hotelMapper).toEntity(createHotelDTO);
        verify(hotelRepository).save(mockHotel); 
        verify(hotelMapper).toDto(mockHotel);
    }
    
    @Test
    void createHotel_ShouldThrowHotelCustomException_WhenNameIsNotUnique() {
        // Arrange
        CreateHotelDTO createHotelDTO = new CreateHotelDTO();
        createHotelDTO.setName("Grand Plaza");

        when(hotelRepository.existsByName("Grand Plaza")).thenReturn(true);

        // Act & Assert
        HotelCustomException exception = assertThrows(HotelCustomException.class, () -> {
            hotelService.createHotel(createHotelDTO);
        });
        assertEquals("Hotel with name 'Grand Plaza' already exists", exception.getMessage());

    }

    @Test
    void getHotelById_ShouldReturnHotel_WhenHotelExists() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        Hotel mockHotel = mockHotel(hotelId, "Grand Plaza");
        HotelDTO expectedDto = new HotelDTO();
        expectedDto.setName("Grand Plaza");
        expectedDto.setStarRating(5);
        expectedDto.setRooms(new ArrayList<>());

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));
        when(hotelMapper.toDto(mockHotel)).thenReturn(expectedDto);

        // Act
        HotelDTO result = hotelService.getHotelById(hotelId);

        // Assert
        assertNotNull(result);
        assertEquals("Grand Plaza", result.getName());
        verify(hotelRepository).findById(hotelId);
        verify(hotelMapper).toDto(mockHotel);
    }

    @Test
    void getHotelById_ShouldThrowHotelNotFoundException_WhenHotelDoesNotExist() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        HotelCustomException.HotelNotFoundException exception = assertThrows(HotelCustomException.HotelNotFoundException.class, () -> {
            hotelService.getHotelById(hotelId);
        });
        assertEquals(String.format("Hotel with ID '%s' not found", hotelId.toString()), exception.getMessage());
        verify(hotelRepository).findById(hotelId);
    }

    @Test
    void deleteHotelById_ShouldDeleteHotel_WhenHotelExists() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        Hotel mockHotel = mockHotel(hotelId, "Grand Plaza");

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));

        // Act
        hotelService.deleteHotelById(hotelId);

        // Assert
        verify(hotelRepository).findById(hotelId);
        verify(hotelRepository).delete(mockHotel);
    }

    @Test
    void deleteHotelById_ShouldThrowHotelNotFoundException_WhenHotelDoesNotExist() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        HotelCustomException.HotelNotFoundException exception = assertThrows(HotelCustomException.HotelNotFoundException.class, () -> {
            hotelService.deleteHotelById(hotelId);
        });
        assertEquals(String.format("Hotel with ID '%s' not found", hotelId.toString()), exception.getMessage());
        verify(hotelRepository).findById(hotelId);
        verify(hotelRepository, never()).delete(any());
    }

    @Test
    void getRoomsByHotelId_ShouldReturnRoomDTOs_WhenHotelExists() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        Hotel mockHotel = mockHotel(hotelId, "Grand Plaza");
    
        Room mockRoom1 = mockRoom(101, 2, 150.0);
        Room mockRoom2 = mockRoom(102, 3, 200.0);
    
        RoomDto roomDTO1 = new RoomDto(UUID.randomUUID(), 101, 2, 150.0, "Single", "Ocean view", 1, "Seaside", 20.0, "Wi-Fi, TV", 2, hotelId);
        RoomDto roomDTO2 = new RoomDto(UUID.randomUUID(), 102, 3, 200.0, "Double", "Mountain view", 2, "Forest", 25.0, "Wi-Fi, TV", 3, hotelId);
    
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));
        when(roomRepository.findRoomsByHotelId(hotelId)).thenReturn(Arrays.asList(mockRoom1, mockRoom2));
        when(roomMapper.toDto(mockRoom1)).thenReturn(roomDTO1);
        when(roomMapper.toDto(mockRoom2)).thenReturn(roomDTO2);
    
        // Act
        List<RoomDto> result = hotelService.getRoomsByHotelId(hotelId);
    
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(roomDTO1, result.get(0));
        assertEquals(roomDTO2, result.get(1));
    
        verify(hotelRepository).findById(hotelId);
        verify(roomRepository).findRoomsByHotelId(hotelId);
        verify(roomMapper).toDto(mockRoom1);
        verify(roomMapper).toDto(mockRoom2);
    }

    @Test
    void addReviewToHotel_ShouldReturnReview_WhenHotelExists() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        Hotel mockHotel = mockHotel(hotelId, "Grand Plaza");

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewTitle("Excellent!");
        reviewDto.setRating(5);

        Review mockReview = mockReview(mockHotel, "10/10", "Excellent!", 5);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));
        when(reviewMapper.toEntity(reviewDto)).thenReturn(mockReview);
        when(reviewRepository.save(mockReview)).thenReturn(mockReview);
        when(reviewMapper.toDto(mockReview)).thenReturn(reviewDto);

        // Act
        ReviewDto result = hotelService.addReviewToHotel(hotelId, reviewDto);

        // Assert
        assertNotNull(result);
        assertEquals("Excellent!", result.getReviewTitle());
        assertEquals(5, result.getRating());
        verify(hotelRepository).findById(hotelId);
        verify(reviewMapper).toEntity(reviewDto);
        verify(reviewRepository).save(mockReview);
        verify(reviewMapper).toDto(mockReview);
    }

    @Test
    void updateHotel_ShouldReturnUpdatedHotel_WhenHotelExists() {
        // Arrange
        UUID hotelId = UUID.randomUUID();
        Hotel existingHotel = new Hotel();
        existingHotel.setId(hotelId);
        existingHotel.setName("Old Hotel Name");
    
        Hotel updatedHotel = new Hotel();
        updatedHotel.setId(hotelId);
        updatedHotel.setName("Updated Hotel Name");
    
        UpdateHotelDto updateHotelDto = new UpdateHotelDto();
        updateHotelDto.setName("Updated Hotel Name");
    
        HotelDTO updatedHotelDto = new HotelDTO();
        updatedHotelDto.setId(hotelId);
        updatedHotelDto.setName("Updated Hotel Name");
    
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(existingHotel));
        when(hotelRepository.save(existingHotel)).thenReturn(updatedHotel);
        when(hotelMapper.toDto(updatedHotel)).thenReturn(updatedHotelDto);
    
        // Act
        HotelDTO result = hotelService.updateHotel(hotelId, updateHotelDto);
    
        // Assert
        assertNotNull(result); 
        assertEquals("Updated Hotel Name", result.getName());
    
        verify(hotelRepository).findById(hotelId);
        verify(hotelRepository).save(existingHotel);
        verify(hotelMapper).toDto(updatedHotel);
    }
    
}
