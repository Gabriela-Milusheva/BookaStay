package com.hotelmanager.controllers;

import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelmanager.dtos.User.UpdateUserRequest;
import com.hotelmanager.dtos.request.RequestDto;
import com.hotelmanager.dtos.response.ResponseDto;
import com.hotelmanager.enumerations.User.UserMessages;
import com.hotelmanager.services.RequestAndResponseService;
import com.hotelmanager.services.UserService;
import com.hotelmanager.utility.LoggingUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RequestAndResponseService requestAndResponseService;

    public AdminController(UserService userService, RequestAndResponseService requestAndResponseService) {
        this.userService = userService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable UUID id) {
        ResponseDto response = null;
        try {
            if (id == null || id.toString().isEmpty()) {
                response = new ResponseDto(HttpStatus.BAD_REQUEST, "Invalid Id");
                this.requestAndResponseService.createRequestAndResponse(id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            var data = this.userService.deleteUser(id);

            if (data == null) {
                response = new ResponseDto(HttpStatus.BAD_REQUEST, UserMessages.USER_NOT_FOUND.getMessage());
                this.requestAndResponseService.createRequestAndResponse(id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            response = new ResponseDto(HttpStatus.OK, UserMessages.USER_DELETED.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            var errorResponse = new ResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e.getStackTrace().toString());
            this.requestAndResponseService.createRequestAndResponse(id, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<ResponseDto<?>> updateUser(@PathVariable UUID id, @RequestBody @Valid RequestDto<UpdateUserRequest> request, BindingResult bindingResult) throws JsonProcessingException {
        ResponseDto<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            var data = this.userService.updateUser(id, request.getData());
            response = new ResponseDto<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("get-users")
    public ResponseEntity<ResponseDto<?>> getAllUsers() {
        ResponseDto<?> response;
        String requestId = UUID.randomUUID().toString();

        try {
            var data = this.userService.getUsers();
            response = new ResponseDto<>(data, HttpStatus.OK, UserMessages.USERS_FETCH_SUCCESS.getMessage());
            this.requestAndResponseService.createRequestAndResponse(requestId, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            this.requestAndResponseService.createRequestAndResponse(requestId, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
