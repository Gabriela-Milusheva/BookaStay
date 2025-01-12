package com.hotelmanager.mappers;

import com.hotelmanager.dtos.hotel.CreateHotelDTO;
import com.hotelmanager.dtos.hotel.HotelDTO;
import com.hotelmanager.models.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelMapper {

    // Map CreateHotelDTO to Hotel entity
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "starRating", source = "dto.starRating")
    @Mapping(target = "address", source = "dto.address")
    @Mapping(target = "phoneNumber", source = "dto.phoneNumber")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "website", source = "dto.website")
    @Mapping(target = "description", source = "dto.description")
    //@Mapping(target = "rooms", source = "dto.rooms")
    //@Mapping(target = "reviews", source = "dto.reviews")
    Hotel toEntity(CreateHotelDTO dto);

    // Map Hotel entity back to HotelDTO
    @Mapping(target = "name", source = "hotel.name")
    @Mapping(target = "starRating", source = "hotel.starRating")
    @Mapping(target = "address", source = "hotel.address")
    @Mapping(target = "phoneNumber", source = "hotel.phoneNumber")
    @Mapping(target = "email", source = "hotel.email")
    @Mapping(target = "website", source = "hotel.website")
    @Mapping(target = "description", source = "hotel.description")
    @Mapping(target = "rooms", source = "hotel.rooms")
    @Mapping(target = "reviews", source = "hotel.reviews")
    HotelDTO toDto(Hotel hotel);
}
