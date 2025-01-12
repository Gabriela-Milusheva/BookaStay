package com.hotelmanager.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RequestsAndResponses")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestAndResponse {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;

    @Column(nullable = false)
    @NotEmpty(message = "Controller name cannot be empty")
    private String controllerName;

    @Column(nullable = false)
    @NotEmpty(message = "Method name cannot be empty")
    private String methodName;

    @Column(nullable = false)
    @NotEmpty(message = "Request ID cannot be empty")
    private String requestId;

    @Column(nullable = false, length = 2500)
    @NotEmpty(message = "Request data cannot be empty")
    private String requestData;

    @Column(nullable = false)
    @NotEmpty(message = "Response ID cannot be empty")
    private String responseId;

    @Column(nullable = true, length = 2500)
    private String responseData;

    @Column(nullable = true)
    private String errorMessage;
}
