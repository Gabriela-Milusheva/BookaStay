package com.hotelmanager.mappers;

import com.hotelmanager.dtos.hotel.CreateHotelDTO;
import com.hotelmanager.dtos.hotel.HotelDTO;
import com.hotelmanager.models.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelMapper {
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "starRating", source = "dto.starRating")
    Hotel toEntity(CreateHotelDTO dto);

    @Mapping(target = "name", source = "hotel.name")
    @Mapping(target = "starRating", source = "hotel.starRating")
    HotelDTO toDto(Hotel hotel);
}
