package com.hotelmanager.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import com.hotelmanager.dtos.User.UserDto;


@Getter
@Setter
@AllArgsConstructor
public class LoginResponse implements Serializable  {
    private UserDto user;

    public LoginResponse() {

    }
}
