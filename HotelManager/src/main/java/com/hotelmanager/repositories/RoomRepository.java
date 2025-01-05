package com.hotelmanager.repositories;

import com.hotelmanager.models.Hotel;
import com.hotelmanager.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByHotel(Hotel hotel);

    Optional<Room> findByHotelAndNumber(Hotel hotel, int number);

    Optional<Room> findByHotelNameAndNumber(String hotelName, int number);
}
