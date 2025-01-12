package com.hotelmanager.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotelmanager.dtos.room.CreateRoomDto;
import com.hotelmanager.dtos.room.RoomDto;
import com.hotelmanager.dtos.room.UpdateRoomDto;
import com.hotelmanager.enumerations.hotel.HotelMessages;
import com.hotelmanager.enumerations.room.RoomMessages;
import com.hotelmanager.exception.hotel.HotelCustomException.HotelNotFoundException;
import com.hotelmanager.exception.room.RoomCustomException.RoomAlreadyExistsException;
import com.hotelmanager.exception.room.RoomCustomException.RoomNotFoundException;
import com.hotelmanager.mappers.RoomMapper;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Room;
import com.hotelmanager.repositories.HotelRepository;
import com.hotelmanager.repositories.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomDto createRoom(UUID hotelId, CreateRoomDto createRoomDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(String.format(
                        HotelMessages.HOTEL_NOT_FOUND.getMessage(),  hotelId)));

        boolean roomExists = roomRepository.existsByHotelIdAndNumber(hotelId, createRoomDto.getNumber());
        if (roomExists) {
            throw new RoomAlreadyExistsException(String.format(
                    RoomMessages.ROOM_ALREADY_EXISTS.getMessage(), createRoomDto.getNumber(), hotelId));
        }

        Room room = roomMapper.toEntity(createRoomDto);
        room.setHotel(hotel);

        Room savedRoom = roomRepository.save(room);

        return roomMapper.toDto(savedRoom);
    }

    @Transactional
    public boolean deleteRoomByRoomId(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(String.format(
                        RoomMessages.ROOM_NOT_FOUND.getMessage(),  roomId)));

        roomRepository.delete(room);
        return true;
    }

    public RoomDto getRoomByRoomId(UUID roomId) {
        return roomRepository.findById(roomId)
                .map(roomMapper::toDto)
                .orElseThrow(() -> new RoomNotFoundException(String.format(
                        RoomMessages.ROOM_NOT_FOUND.getMessage(), roomId)));
    }

    @Transactional
    public RoomDto updateRoomByRoomId(UUID roomId, UpdateRoomDto updateRoomDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(String.format(
                        RoomMessages.ROOM_NOT_FOUND.getMessage(), roomId)));

        room.setBedCapacity(updateRoomDto.getBedCapacity());
        room.setPricePerNight(updateRoomDto.getPricePerNight());

        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toDto(updatedRoom);
    }
}