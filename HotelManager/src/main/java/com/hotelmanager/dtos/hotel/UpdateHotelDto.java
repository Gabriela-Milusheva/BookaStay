package com.hotelmanager.dtos.hotel;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateHotelDto implements Serializable {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
}