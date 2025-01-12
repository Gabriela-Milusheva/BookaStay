package com.hotelmanager.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelmanager.models.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    Optional<Hotel> findById(UUID id);
    
    boolean existsByName(String name);

    void deleteByName(String name);

    Optional<Hotel> findByName(String name);
    
    @Query("SELECT h FROM Hotel h " +
           "WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:minRating IS NULL OR h.starRating >= :minRating) " +
           "AND (:address IS NULL OR LOWER(h.address) LIKE LOWER(CONCAT('%', :address, '%')))")
    List<Hotel> filterHotels(
        @Param("name") String name,
        @Param("minRating") Integer minRating,
        @Param("address") String address
    );
}
