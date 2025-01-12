package com.hotelmanager.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotelmanager.dtos.hotel.CreateHotelDTO;
import com.hotelmanager.dtos.hotel.HotelDTO;
import com.hotelmanager.dtos.hotel.UpdateHotelDto;
import com.hotelmanager.dtos.review.ReviewDto;
import com.hotelmanager.dtos.room.RoomDto;
import com.hotelmanager.enumerations.hotel.HotelMessages;
import com.hotelmanager.exception.hotel.HotelCustomException;
import com.hotelmanager.exception.hotel.HotelCustomException.HotelNotFoundException;
import com.hotelmanager.mappers.HotelMapper;
import com.hotelmanager.mappers.ReviewMapper;
import com.hotelmanager.mappers.RoomMapper;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Review;
import com.hotelmanager.repositories.HotelRepository;
import com.hotelmanager.repositories.ReviewRepository;
import com.hotelmanager.repositories.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
    }

    public HotelDTO getHotelById(UUID hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(String.format(
                HotelMessages.HOTEL_NOT_FOUND.getMessage(), hotelId)));
        return hotelMapper.toDto(hotel);
    }

    @Transactional
    public HotelDTO createHotel(CreateHotelDTO hotelDTO) {
        if (hotelRepository.existsByName(hotelDTO.getName())) {
            throw new HotelCustomException.HotelAlreadyExistsException(hotelDTO.getName());
        }

        try {
            Hotel hotel = hotelMapper.toEntity(hotelDTO);
            Hotel savedHotel = hotelRepository.save(hotel);
            return hotelMapper.toDto(savedHotel);
        } catch (Exception e) {
            throw new HotelCustomException.HotelCreationFailedException();
        }
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> filterHotels(String name, Integer minRating, String address) {
        List<Hotel> hotels = hotelRepository.filterHotels(name, minRating, address);
        return hotels.stream().map(hotelMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteHotelById(UUID hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(String.format(
                HotelMessages.HOTEL_NOT_FOUND.getMessage(), hotelId)));
        hotelRepository.delete(hotel);
    }

    public List<RoomDto> getRoomsByHotelId(UUID hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(String.format(
                HotelMessages.HOTEL_NOT_FOUND.getMessage(), hotelId)));

        return roomRepository.findRoomsByHotelId(hotel.getId()).stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByHotelId(UUID hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(String.format(
                HotelMessages.HOTEL_NOT_FOUND.getMessage(), hotelId)));
        return reviewRepository.findReviewsByHotelId(hotel.getId()).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto addReviewToHotel(UUID hotelId, ReviewDto reviewDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(String.format(
                HotelMessages.HOTEL_NOT_FOUND.getMessage(), hotelId)));
        try {
            Review review = reviewMapper.toEntity(reviewDto);
            reviewRepository.save(review);
            return reviewMapper.toDto(review);
        } catch (Exception e) {
            throw new HotelCustomException.HotelCreationFailedException();
        }
    }

    @Transactional
    public HotelDTO updateHotel(UUID hotelId, UpdateHotelDto updateHotelDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(String.format(
                HotelMessages.HOTEL_NOT_FOUND.getMessage(), hotelId)));

        if (updateHotelDto.getName() != null) {
            hotel.setName(updateHotelDto.getName());
        }
        if (updateHotelDto.getAddress() != null) {
            hotel.setAddress(updateHotelDto.getAddress());
        }
        if (updateHotelDto.getPhoneNumber() != null) {
            hotel.setPhoneNumber(updateHotelDto.getPhoneNumber());
        }
        if (updateHotelDto.getEmail() != null) {
            hotel.setEmail(updateHotelDto.getEmail());
        }
        if (updateHotelDto.getWebsite() != null) {
            hotel.setWebsite(updateHotelDto.getWebsite());
        }
        if (updateHotelDto.getDescription() != null) {
            hotel.setDescription(updateHotelDto.getDescription());
        }

        try {
            Hotel updatedHotel = hotelRepository.save(hotel);
            return hotelMapper.toDto(updatedHotel);
        } catch (Exception e) {
            throw new HotelCustomException.HotelUpdateFailedException();
        }
    }
}
