package com.hotelmanager.controllers;

import com.hotelmanager.dtos.hotel.CreateHotelDTO;
import com.hotelmanager.dtos.hotel.HotelDTO;
import com.hotelmanager.dtos.hotel.UpdateStarRatingDTO;
import com.hotelmanager.dtos.room.CreateRoomDTO;
import com.hotelmanager.dtos.room.RoomDTO;
import com.hotelmanager.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        List<HotelDTO> hotelsDto = hotelService.getAllHotels();
        return ResponseEntity.ok(hotelsDto);
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<HotelDTO>> getHotelsByName(@PathVariable String name) {
        List<HotelDTO> hotelsDto = hotelService.getHotelsByName(name);
        if (!hotelsDto.isEmpty()) {
            return ResponseEntity.ok(hotelsDto);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createHotel(@RequestBody CreateHotelDTO hotelDTO) {
        try {
            HotelDTO createdHotel = hotelService.createHotel(hotelDTO);
            return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> deleteHotelByName(@PathVariable String name) {
        if (hotelService.deleteHotelByName(name)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add-rooms/{name}")
    public ResponseEntity<?> addRoomsToHotel(@PathVariable String name, @RequestBody CreateRoomDTO roomDTO) {
        try {
            hotelService.addRoomsToHotel(name, roomDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{name}/rooms")
    public ResponseEntity<List<RoomDTO>> getRoomsByHotelName(@PathVariable String name) {
        try {
            List<RoomDTO> roomsDto = hotelService.getRoomsByHotelName(name);
            return ResponseEntity.ok(roomsDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("update-rating/{name}")
    public ResponseEntity<Void> updateStarRating(@PathVariable String name, @RequestBody UpdateStarRatingDTO starRatingDTO) {
        try {
            hotelService.updateStarRatingByName(name, starRatingDTO.starRating());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
