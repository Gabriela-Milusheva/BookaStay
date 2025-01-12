package com.hotelmanager.dtos.reservation;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationDTO {

    private UUID id;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private UUID roomId;
    private UUID hotelId;
    private UUID userId;
}
