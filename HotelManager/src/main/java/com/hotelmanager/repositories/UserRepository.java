package com.hotelmanager.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanager.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID userId);  
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
}
