package com.hotelmanager.dtos.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateRoomDto {

    private int number;
    private int bedCapacity;
    private double pricePerNight;
    private String roomType;
    private String description;
    private int floor;
    private String view;
    private double size;
    private String amenities;
    private int maxOccupants;
}
