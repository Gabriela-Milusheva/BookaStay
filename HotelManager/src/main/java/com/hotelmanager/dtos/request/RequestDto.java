package com.hotelmanager.dtos.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class RequestDto<T> implements Serializable {
    private String requestId;
    private LocalDateTime timestamp;

    @JsonProperty("data")
    private T data;
}
