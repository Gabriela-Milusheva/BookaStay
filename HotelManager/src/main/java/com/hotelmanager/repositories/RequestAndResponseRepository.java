package com.hotelmanager.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelmanager.models.RequestAndResponse;

public interface RequestAndResponseRepository extends JpaRepository<RequestAndResponse, UUID> {
    Optional<RequestAndResponse> findByRequestId(String requestId);
    Optional<RequestAndResponse> findByResponseId(String responseId);
}
