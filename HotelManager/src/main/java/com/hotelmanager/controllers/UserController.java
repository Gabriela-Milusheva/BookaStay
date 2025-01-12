package com.hotelmanager.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelmanager.dtos.User.UserDto;
import com.hotelmanager.dtos.response.ResponseDto;
import com.hotelmanager.enumerations.User.UserMessages;
import com.hotelmanager.exception.User.UserCustomException;
import com.hotelmanager.services.RequestAndResponseService;
import com.hotelmanager.services.UserService;
import com.hotelmanager.utility.LoggingUtils;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RequestAndResponseService requestAndResponseService;

    public UserController(UserService userService, RequestAndResponseService requestAndResponseService) {
        this.userService = userService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @GetMapping("/get-data/{id}")
    public ResponseEntity<ResponseDto<?>> getUserData(@PathVariable UUID id) {
        ResponseDto<?> response;

        if (id == null || id.toString().isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(UserMessages.INVALID_ID.getMessage()));
        }

        try {
            var data = this.userService.getUser(id);
            response = new ResponseDto<>(data, HttpStatus.OK, UserMessages.USER_FETCH_SUCCESS.getMessage());
            this.requestAndResponseService.createRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (UserCustomException e) {
            response = new ResponseDto<>(e.getMessage());
            this.requestAndResponseService.createRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            this.requestAndResponseService.createRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ResponseDto<UserDto>> getUserByEmail(
            @PathVariable(name = "email") String email) throws JsonProcessingException {

        ResponseDto<UserDto> response;

        try {
            var userDto = this.userService.findByEmail(email);
            response = new ResponseDto<>(userDto, HttpStatus.OK, UserMessages.USER_FETCH_SUCCESS.getMessage());

            return ResponseEntity.ok(response);

        } catch (UserCustomException e) {
            response = new ResponseDto<>(e.getMessage());

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response = new ResponseDto<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
