package com.hotelmanager.models;

import java.util.UUID;

import com.hotelmanager.enumerations.room.RoomTypes;

import jakarta.persistence.Column;
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
