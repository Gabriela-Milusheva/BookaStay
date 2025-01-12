package com.hotelmanager.models;

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

import com.hotelmanager.enumerations.room.RoomTypes;

import lombok.Data;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    private int number;
    private int bedCapacity;
    private double pricePerNight;
    private RoomTypes roomType;

    private String description;
    private String floor;
    private String view;
    private double size;
    private String amenities;
    private int maxOccupants;

    @ManyToOne
    @JoinColumn(name = "hotelId")
    private Hotel hotel;
}
