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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<HotelDTO> getHotelsByName(String name) {
        return hotelRepository.findByName(name).stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public HotelDTO createHotel(CreateHotelDTO hotelDTO) {
        if (hotelRepository.existsByName(hotelDTO.name())) {
            throw new IllegalArgumentException("Hotel with name " + hotelDTO.name() + " already exists.");
        }

        Hotel hotel = hotelMapper.toEntity(hotelDTO);
        Hotel savedHotel = hotelRepository.save(hotel);

        return hotelMapper.toDto(savedHotel);
    }

    @Transactional
    public void updateStarRatingByName(String name, int starRating) {
        if (!hotelRepository.existsByName(name)) {
            throw new IllegalArgumentException("Hotel with name " + name + " not found.");
        }
        hotelRepository.updateStarRatingByName(name, starRating);
    }

    @Transactional
    public boolean deleteHotelByName(String name) {
        return hotelRepository.findByName(name).map(hotel -> {
            hotelRepository.delete(hotel);
            return true;
        }).orElse(false);
    }

    @Transactional
    public void addRoomsToHotel(String hotelName, CreateRoomDTO roomDTO) {
        Hotel hotel = hotelRepository.findByName(hotelName)
                .orElseThrow(() -> new IllegalArgumentException("Hotel with name " + hotelName + " not found."));

        Set<Integer> existingRoomNumbers = roomRepository.findByHotel(hotel).stream()
                .map(Room::getNumber)
                .collect(Collectors.toSet());

        List<Integer> newRoomNumbers = RoomNumberUtil.findNextAvailableRoomNumbers(
                existingRoomNumbers, roomDTO.number(), roomDTO.quantity());

        for (int number : newRoomNumbers) {
            Room room = roomMapper.toEntity(roomDTO);
            room.setNumber(number);
            room.setHotel(hotel);
            roomRepository.save(room);
        }
    }

    public List<RoomDTO> getRoomsByHotelName(String hotelName) {
        Hotel hotel = hotelRepository.findByName(hotelName)
                .orElseThrow(() -> new IllegalArgumentException("Hotel with name " + hotelName + " not found."));
        return roomRepository.findByHotel(hotel).stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }
}
