package com.hotelmanager.dtos.hotel;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateHotelDTO implements Serializable{
        private String name;
        private int starRating;
        private String address;
        private String phoneNumber;
        private String email;
        private String website;
        private String description;

    }
