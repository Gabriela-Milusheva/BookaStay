package com.hotelmanager.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotelmanager.models.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("SELECT COUNT(r) > 0 FROM Reservation r "
            + "WHERE r.roomId = :roomId "
            + "AND ((r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate))")
    boolean existsByRoomIdAndDateRange(UUID roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate);
}
