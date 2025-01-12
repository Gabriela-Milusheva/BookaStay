package com.hotelmanager.controllers;

import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelmanager.dtos.request.RequestDto;
import com.hotelmanager.dtos.response.ResponseDto;
import com.hotelmanager.dtos.room.CreateRoomDto;
import com.hotelmanager.dtos.room.RoomDto;
import com.hotelmanager.dtos.room.UpdateRoomDto;
import com.hotelmanager.enumerations.room.RoomMessages;
import com.hotelmanager.exception.room.RoomCustomException;
import com.hotelmanager.services.RequestAndResponseService;
import com.hotelmanager.services.RoomService;
import com.hotelmanager.utility.LoggingUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final RequestAndResponseService requestAndResponseService;

    @PostMapping("/create/{hotelId}")
    public ResponseEntity<ResponseDto<RoomDto>> createRoom(
            @PathVariable UUID hotelId,
            @RequestBody @Valid RequestDto<CreateRoomDto> request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            CreateRoomDto createRoomDto = request.getData();

            RoomDto createdRoom = roomService.createRoom(hotelId, createRoomDto);

            ResponseDto<RoomDto> response = new ResponseDto<>();
            response.setResponseId(request.getRequestId());
            response.setDate(request.getTimestamp());
            response.setData(createdRoom);
            response.setStatus(HttpStatus.CREATED);
            response.setDescription(RoomMessages.ROOM_CREATED_SUCCESS.getMessage());

            requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RoomCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, RoomMessages.ROOM_CREATION_FAILED.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, RoomMessages.CUSTOM_ERROR.getMessage()));
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDto<RoomDto>> getRoomByHotelIdAndRoomId(
            @PathVariable UUID roomId) {
        try {
            RoomDto roomDto = roomService.getRoomByRoomId(roomId);
            return ResponseEntity.ok(new ResponseDto<>(roomDto, HttpStatus.OK, RoomMessages.ROOM_FETCH_SUCCESS.getMessage()));
        } catch (RoomCustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(e.getMessage(), HttpStatus.NOT_FOUND, RoomMessages.ROOM_NOT_FOUND.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, RoomMessages.CUSTOM_ERROR.getMessage()));
        }
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<ResponseDto<Void>> updateRoom(
            @PathVariable UUID roomId,
            @RequestBody @Valid RequestDto<UpdateRoomDto> request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError() != null ? bindingResult.getFieldError().getDefaultMessage() : "Validation error";
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage, HttpStatus.BAD_REQUEST, null));
        }

        try {
            roomService.updateRoomByRoomId(roomId, request.getData());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseDto<>(null, HttpStatus.NO_CONTENT, RoomMessages.ROOM_UPDATE_SUCCESS.getMessage()));
        } catch (RoomCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, RoomMessages.ROOM_UPDATE_FAILED.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, RoomMessages.CUSTOM_ERROR.getMessage()));
        }
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<ResponseDto<Void>> deleteRoom(
            @PathVariable UUID roomId) {
        try {
            roomService.deleteRoomByRoomId(roomId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseDto<>(null, HttpStatus.NO_CONTENT, RoomMessages.ROOM_DELETED_SUCCESS.getMessage()));
        } catch (RoomCustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(e.getMessage(), HttpStatus.NOT_FOUND, RoomMessages.ROOM_DELETED_FAILD.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, RoomMessages.CUSTOM_ERROR.getMessage()));
        }
    }
}
