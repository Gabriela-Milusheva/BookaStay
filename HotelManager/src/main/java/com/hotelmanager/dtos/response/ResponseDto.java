package com.hotelmanager.dtos.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> implements Serializable {
    private LocalDateTime date;
    private String errorDescription;
    private String responseId;
    private HttpStatus status;
    private String description;
    private T data;

    public ResponseDto() {
        this.date = LocalDateTime.now();
        this.responseId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS"));
        this.errorDescription = "";
        this.status = HttpStatus.OK;
        this.description = "";
        this.data = null;
    }

    public ResponseDto(T data, HttpStatus status, String description) {
        this();
        this.data = data;
        this.status = status;
        this.description = description;
        this.errorDescription = "";
    }

    public ResponseDto(HttpStatus status, String description) {
        this();
        this.data = null;
        this.status = status;
        this.description = description;
        this.errorDescription = "";
    }

    public ResponseDto(String errorDescription, HttpStatus status, String description) {
        this();
        this.errorDescription = errorDescription;
        this.status = status;
        this.description = description;
        this.data = null;
    }

    public ResponseDto(String errorDescription) {
        this();
        this.errorDescription = errorDescription;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.description = "";
        this.data = null;
    }
}
