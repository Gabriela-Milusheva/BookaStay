package com.hotelmanager.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanager.models.Room;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByIdAndHotelId(UUID roomId, UUID hotelId);
    List<Room> findRoomsByHotelId(UUID hotelId);
    boolean existsByHotelIdAndNumber(UUID hotelId, int number);
}
