package com.hotelmanager.controllers;

import com.hotelmanager.dtos.room.RoomDTO;
import com.hotelmanager.dtos.room.UpdateRoomDTO;
import com.hotelmanager.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @DeleteMapping("/delete/{hotelName}/{roomNumber}")
    public ResponseEntity<Void> deleteRoomByHotelNameAndNumber(@PathVariable String hotelName, @PathVariable int roomNumber) {
        try {
            roomService.deleteRoomByHotelNameAndNumber(hotelName, roomNumber);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{hotelName}/{number}")
    public ResponseEntity<RoomDTO> getRoomByHotelNameAndNumber(@PathVariable String hotelName, @PathVariable int number) {
        try {
            RoomDTO roomDto = roomService.getRoomByHotelNameAndNumber(hotelName, number);
            return ResponseEntity.ok(roomDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{hotelName}/{number}")
    public ResponseEntity<Void> updateRoomByHotelNameAndNumber(@PathVariable String hotelName, @PathVariable int number,
                                                               @RequestBody UpdateRoomDTO updateRoomDTO) {
        try {
            roomService.updateRoomByHotelNameAndNumber(hotelName, number, updateRoomDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
