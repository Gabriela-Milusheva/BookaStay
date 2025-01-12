package com.hotelmanager.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanager.models.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findReviewsByHotelId(UUID hotelId);
}
