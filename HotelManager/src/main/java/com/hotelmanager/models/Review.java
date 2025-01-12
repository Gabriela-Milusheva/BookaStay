package com.hotelmanager.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String reviewTitle;
    private String reviewDesc;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "hotelId", nullable = false)
    private Hotel hotel;

    /*@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;*/

    private LocalDateTime createdAt;
}
