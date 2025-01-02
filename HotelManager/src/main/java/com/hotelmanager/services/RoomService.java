package com.hotelmanager.services;

import com.hotelmanager.dtos.room.RoomDTO;
import com.hotelmanager.dtos.room.UpdateRoomDTO;
import com.hotelmanager.mappers.RoomMapper;
import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Room;
import com.hotelmanager.repositories.HotelRepository;
import com.hotelmanager.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public boolean deleteRoomByHotelNameAndNumber(String hotelName, int roomNumber) {
        Hotel hotel = hotelRepository.findByName(hotelName)
                .orElseThrow(() -> new IllegalArgumentException("Hotel with name " + hotelName + " not found."));

        Room room = roomRepository.findByHotelAndNumber(hotel, roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room number " + roomNumber + " not found in hotel " + hotelName));

        roomRepository.delete(room);
        return true;
    }

    public RoomDTO getRoomByHotelNameAndNumber(String hotelName, int number) {
        return roomRepository.findByHotelNameAndNumber(hotelName, number)
                .map(roomMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Room not found for hotel " + hotelName + " with number " + number));
    }

    @Transactional
    public void updateRoomByHotelNameAndNumber(String hotelName, int number, UpdateRoomDTO updateRoomDTO) {
        Room room = roomRepository.findByHotelNameAndNumber(hotelName, number)
                .orElseThrow(() -> new IllegalArgumentException("Room not found for hotel " + hotelName + " with number " + number));

        room.setBedCapacity(updateRoomDTO.bedCapacity());
        room.setPricePerNight(updateRoomDTO.pricePerNight());

        roomRepository.save(room);
    }
}
