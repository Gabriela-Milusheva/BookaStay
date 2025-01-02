package com.hotelmanager.mappers;

import com.hotelmanager.dtos.room.CreateRoomDTO;
import com.hotelmanager.dtos.room.RoomDTO;
import com.hotelmanager.models.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {
    @Mapping(target = "hotel", ignore = true)
    Room toEntity(CreateRoomDTO dto);

    RoomDTO toDto(Room room);
}
