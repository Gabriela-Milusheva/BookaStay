package com.hotelmanager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanager.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
