package com.hotelmanager.controllers;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelmanager.dtos.User.ChangeUserPasswordDto;
import com.hotelmanager.dtos.User.LoginResponse;
import com.hotelmanager.dtos.User.LoginUserDto;
import com.hotelmanager.dtos.User.RegisterUserDto;
import com.hotelmanager.dtos.request.RequestDto;
import com.hotelmanager.dtos.response.ResponseDto;
import com.hotelmanager.enumerations.User.UserMessages;
import com.hotelmanager.services.RequestAndResponseService;
import com.hotelmanager.services.UserService;
import com.hotelmanager.utility.LoggingUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final RequestAndResponseService requestAndResponseService;

    public AuthenticationController(UserService userService, RequestAndResponseService requestAndResponseService) {
        this.userService = userService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<String>> registerUser(@RequestBody RequestDto<RegisterUserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        ResponseDto<String> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            var registeredUser = this.userService.registerUser(request.getData());

            if (registeredUser == null) {
                response = new ResponseDto<>(UserMessages.REGISTER_FAILED.getMessage());
                this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            response = new ResponseDto<>(HttpStatus.OK, UserMessages.REGISTER_SUCCESS.getMessage());

            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<?>> loginUser(@RequestBody @Valid RequestDto<LoginUserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        ResponseDto<LoginResponse> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            LoginResponse loginResponse = this.userService.loginUser(request.getData());

            response = new ResponseDto<>(loginResponse, HttpStatus.OK, UserMessages.LOGIN_SUCCESS.getMessage());
            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseDto<?>> changePassword(@RequestBody @Valid RequestDto<ChangeUserPasswordDto> request, BindingResult bindingResult) throws JsonProcessingException {
        ResponseDto<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ResponseDto<>(errorMessage));
        }

        try {
            this.userService.updatePassword(request.getData());
            response = new ResponseDto<>(HttpStatus.OK, UserMessages.PASSWORD_CHANGED_SUCCESS.getMessage());
            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            this.requestAndResponseService.createRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
