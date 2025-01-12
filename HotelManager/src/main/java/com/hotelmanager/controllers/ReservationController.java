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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelmanager.dtos.request.RequestDto;
import com.hotelmanager.dtos.reservation.CreateReservationDTO;
import com.hotelmanager.dtos.reservation.ReservationDTO;
import com.hotelmanager.dtos.response.ResponseDto;
import com.hotelmanager.enumerations.reservation.ReservationMessages;
import com.hotelmanager.exception.reservation.ReservationCustomException;
import com.hotelmanager.services.RequestAndResponseService;
import com.hotelmanager.services.ReservationService;
import com.hotelmanager.utility.LoggingUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RequestAndResponseService requestAndResponseService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<String>> createReservation(
            @RequestBody @Valid RequestDto<CreateReservationDTO> request,
            BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            ReservationDTO createdReservation = reservationService.createReservation(request.getData());

            ResponseDto<String> response = new ResponseDto<>(HttpStatus.CREATED, ReservationMessages.RESERVATION_SUCCESS.getMessage());
            requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (ReservationCustomException e) {
            ResponseDto<String> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto<String> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteReservation(@PathVariable UUID id) throws JsonProcessingException {
        RequestDto<UUID> request = new RequestDto<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(id);

        try {
            reservationService.deleteReservationById(id);
            ResponseDto<Void> response = new ResponseDto<>(null, HttpStatus.NO_CONTENT, ReservationMessages.RESERVATION_DELETE_SUCCESS.getMessage());
            requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (ReservationCustomException e) {
            ResponseDto<Void> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto<Void> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto<ReservationDTO>> updateReservation(
            @PathVariable UUID id,
            @RequestBody @Valid ReservationDTO reservationDTO,
            BindingResult bindingResult) throws JsonProcessingException {

        RequestDto<ReservationDTO> request = new RequestDto<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(reservationDTO);

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
            ResponseDto<ReservationDTO> response = new ResponseDto<>(updatedReservation, HttpStatus.OK, ReservationMessages.RESERVATION_UPDATE_SUCCESS.getMessage());
            requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (ReservationCustomException e) {
            ResponseDto<ReservationDTO> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto<ReservationDTO> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ReservationDTO>> getReservationById(@PathVariable UUID id) throws JsonProcessingException {
        RequestDto<UUID> request = new RequestDto<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(id);

        try {
            ReservationDTO reservation = reservationService.getReservationById(id);
            ResponseDto<ReservationDTO> response = new ResponseDto<>(reservation, HttpStatus.OK, "Reservation fetched successfully");
            requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (ReservationCustomException e) {
            ResponseDto<ReservationDTO> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto<ReservationDTO> errorResponse = new ResponseDto<>(e.getMessage());
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
