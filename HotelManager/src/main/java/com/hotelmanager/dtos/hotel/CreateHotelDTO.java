package com.hotelmanager.dtos.hotel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateHotelDTO implements Serializable {

    private String name;
    private int starRating;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;

}
