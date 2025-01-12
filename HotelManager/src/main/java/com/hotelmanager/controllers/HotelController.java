package com.hotelmanager.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelmanager.dtos.hotel.CreateHotelDTO;
import com.hotelmanager.dtos.hotel.HotelDTO;
import com.hotelmanager.dtos.hotel.UpdateHotelDto;
import com.hotelmanager.dtos.request.RequestDto;
import com.hotelmanager.dtos.response.ResponseDto;
import com.hotelmanager.dtos.review.ReviewDto;
import com.hotelmanager.enumerations.hotel.HotelMessages;
import com.hotelmanager.exception.hotel.HotelCustomException;
import com.hotelmanager.services.HotelService;
import com.hotelmanager.services.RequestAndResponseService;
import com.hotelmanager.utility.LoggingUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final RequestAndResponseService requestAndResponseService;

    @GetMapping("/all")
    public ResponseEntity<ResponseDto<?>> getAllHotels() {
        ResponseDto<?> response;

        try {
            List<HotelDTO> hotels = hotelService.getAllHotels();
            response = new ResponseDto<>(hotels, HttpStatus.OK, HotelMessages.HOTELS_FETCH_SUCCESS.getMessage());
            return ResponseEntity.ok(response);
        } catch (HotelCustomException e) {
            response = new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.CUSTOM_ERROR.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response = new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
            return ResponseEntity.internalServerError().body(response); 
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<String>> createHotel(
            @RequestBody @Valid RequestDto<CreateHotelDTO> request,
            BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            CreateHotelDTO createHotelDTO = request.getData();
            hotelService.createHotel(createHotelDTO);

            ResponseDto<String> response = new ResponseDto<>();
            response.setResponseId(request.getRequestId());
            response.setDate(request.getTimestamp());
            response.setData(HotelMessages.HOTEL_CREATED_SUCCESS.getMessage());
            response.setStatus(HttpStatus.CREATED);
            response.setDescription(HotelMessages.HOTEL_CREATED_SUCCESS.getMessage());

            requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (HotelCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.CUSTOM_ERROR.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later."));
        }
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ResponseDto<?>> getHotelById(@PathVariable UUID hotelId) {
        try {
            var hotel = hotelService.getHotelById(hotelId);
            return ResponseEntity.ok(new ResponseDto<>(hotel, HttpStatus.OK, HotelMessages.HOTEL_FETCH_SUCCESS.getMessage()));
        } catch (HotelCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.CUSTOM_ERROR.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
        }
    }

    @DeleteMapping("/delete/{hotelId}")
    public ResponseEntity<ResponseDto<Void>> deleteHotel(@PathVariable UUID hotelId) throws JsonProcessingException {
        try {
            hotelService.deleteHotelById(hotelId);

            ResponseDto<Void> response = new ResponseDto<>(null, HttpStatus.NO_CONTENT, HotelMessages.HOTEL_DELETED_SUCCESS.getMessage());

            RequestDto<UUID> request = new RequestDto<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(hotelId);

            requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (HotelCustomException e) {
            ResponseDto<Void> errorResponse = new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.CUSTOM_ERROR.getMessage());
            RequestDto<UUID> request = new RequestDto<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(hotelId);
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto<Void> errorResponse = new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
            RequestDto<UUID> request = new RequestDto<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(hotelId);
            requestAndResponseService.createRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<ResponseDto<List<?>>> getRoomsByHotelId(@PathVariable UUID hotelId) {
        try {
            var rooms = hotelService.getRoomsByHotelId(hotelId);
            return ResponseEntity.ok(new ResponseDto<>(rooms, HttpStatus.OK, HotelMessages.ROOMS_FETCH_SUCCESS.getMessage()));
        } catch (HotelCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.CUSTOM_ERROR.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
        }
    }

    @GetMapping("/{hotelId}/reviews")
    public ResponseEntity<ResponseDto<List<?>>> getReviewsByHotelId(@PathVariable UUID hotelId) {
        try {
            var reviews = hotelService.getReviewsByHotelId(hotelId);
            return ResponseEntity.ok(new ResponseDto<>(reviews, HttpStatus.OK, HotelMessages.REVIEWS_FETCH_SUCCESS.getMessage()));
        } catch (HotelCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.CUSTOM_ERROR.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
        }
    }

    @PostMapping("/{hotelId}/reviews")
    public ResponseEntity<ResponseDto<ReviewDto>> addReview(
            @PathVariable UUID hotelId, 
            @RequestBody @Valid RequestDto<ReviewDto> request,
            BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            var createdReview = hotelService.addReviewToHotel(hotelId, request.getData());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto<>(createdReview, HttpStatus.CREATED, HotelMessages.REVIEW_ADDED_SUCCESS.getMessage()));
        } catch (HotelCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.CUSTOM_ERROR.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
        }
    }

    @PutMapping("/update/{hotelId}")
    public ResponseEntity<ResponseDto<HotelDTO>> updateHotel(
            @PathVariable UUID hotelId,
            @RequestBody @Valid RequestDto<UpdateHotelDto> request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError() != null ? bindingResult.getFieldError().getDefaultMessage() : "Validation error";
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage, HttpStatus.BAD_REQUEST, null));
        }

        try {
            UpdateHotelDto updateHotelDto = request.getData();
            HotelDTO updatedHotel = hotelService.updateHotel(hotelId, updateHotelDto);

            ResponseDto<HotelDTO> response = new ResponseDto<>();
            response.setData(updatedHotel);
            response.setStatus(HttpStatus.OK);
            response.setDescription(HotelMessages.HOTEL_UPDATE_SUCCESS.getMessage());

            return ResponseEntity.ok(response);
        } catch (HotelCustomException e) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, HotelMessages.HOTEL_UPDATE_FAILED.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseDto<List<HotelDTO>>> filterHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) String address) {
    
        try {
            List<HotelDTO> filteredHotels = hotelService.filterHotels(name, minRating, address);
            ResponseDto<List<HotelDTO>> response = new ResponseDto<>(filteredHotels, HttpStatus.OK, "Filtered hotels fetched successfully");
            return ResponseEntity.ok(response);
        } catch (HotelCustomException e) {
            ResponseDto<List<HotelDTO>> errorResponse = new ResponseDto<>(e.getMessage(), HttpStatus.BAD_REQUEST, "Error fetching hotels");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto<List<HotelDTO>> errorResponse = new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}