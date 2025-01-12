package com.hotelmanager.dtos.hotel;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.hotelmanager.dtos.review.ReviewDto;
import com.hotelmanager.dtos.room.RoomDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HotelDTO implements Serializable{
private UUID id;
private String name;
private int starRating;
private String address;
private String phoneNumber;
private String email;
private String website;
private String description;
private List<RoomDto> rooms;
private List<ReviewDto> reviews;
}
