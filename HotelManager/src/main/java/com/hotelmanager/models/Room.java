package com.hotelmanager.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;
    private int number;
    private int bedCapacity;
    private double pricePerNight;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
