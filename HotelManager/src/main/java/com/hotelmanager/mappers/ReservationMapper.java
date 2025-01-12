package com.hotelmanager.mappers;

import com.hotelmanager.dtos.reservation.CreateReservationDTO;
import com.hotelmanager.dtos.reservation.ReservationDTO;
import com.hotelmanager.models.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {

    // Map CreateReservationDTO to Reservation entity
    @Mapping(target = "user.id", source = "dto.userId")
    @Mapping(target = "roomId", source = "dto.roomId")
    @Mapping(target = "hotelId", source = "dto.hotelId")
    Reservation toEntity(CreateReservationDTO dto);

    // Map Reservation entity to ReservationDTO
    @Mapping(target = "id", source = "reservation.id")
    @Mapping(target = "userId", source = "reservation.user.id")
    @Mapping(target = "roomId", source = "reservation.roomId")
    @Mapping(target = "hotelId", source = "reservation.hotelId")
    ReservationDTO toDto(Reservation reservation);
}
