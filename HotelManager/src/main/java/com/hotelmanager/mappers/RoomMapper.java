package com.hotelmanager.mappers;

import com.hotelmanager.dtos.room.CreateRoomDto;
import com.hotelmanager.dtos.room.RoomDto;
import com.hotelmanager.enumerations.room.RoomTypes;
import com.hotelmanager.models.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    // Map Room entity back to RoomDTO
    @Mapping(target = "number", source = "room.number")
    @Mapping(target = "bedCapacity", source = "room.bedCapacity")
    @Mapping(target = "pricePerNight", source = "room.pricePerNight")
    @Mapping(target = "roomType", source = "room.roomType")
    @Mapping(target = "description", source = "room.description")
    @Mapping(target = "floor", source = "room.floor")
    @Mapping(target = "view", source = "room.view")
    @Mapping(target = "size", source = "room.size")
    @Mapping(target = "amenities", source = "room.amenities")
    @Mapping(target = "maxOccupants", source = "room.maxOccupants")
    @Mapping(target = "hotelId", source = "room.hotel.id")
    RoomDto toDto(Room room);

    // Map RoomDTO to Room entity
    @Mapping(target = "number", source = "dto.number")
    @Mapping(target = "bedCapacity", source = "dto.bedCapacity")
    @Mapping(target = "pricePerNight", source = "dto.pricePerNight")
    @Mapping(target = "roomType", source = "dto.roomType")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "floor", source = "dto.floor")
    @Mapping(target = "view", source = "dto.view")
    @Mapping(target = "size", source = "dto.size")
    @Mapping(target = "amenities", source = "dto.amenities")
    @Mapping(target = "maxOccupants", source = "dto.maxOccupants")
    @Mapping(target = "hotel.id", source = "dto.hotelId")
    Room toEntity(CreateRoomDto dto);
}
