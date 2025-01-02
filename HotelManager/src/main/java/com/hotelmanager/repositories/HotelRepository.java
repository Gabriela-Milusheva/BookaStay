package com.hotelmanager.repositories;

import com.hotelmanager.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    boolean existsByName(String name);

    void deleteByName(String name);

    Optional<Hotel> findByName(String name);

    @Modifying
    @Query("UPDATE Hotel h SET h.starRating = :starRating WHERE h.name = :name")
    void updateStarRatingByName(String name, int starRating);
}
