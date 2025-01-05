package com.hotelmanager.dtos.hotel;

import com.hotelmanager.dtos.room.RoomDTO;

import java.util.List;

public record HotelDTO(String name, int starRating, List<RoomDTO> rooms) {}
