package com.hotelmanager.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) // Connect review to the user who created it
    private User user;

    private LocalDateTime createdAt;
}
