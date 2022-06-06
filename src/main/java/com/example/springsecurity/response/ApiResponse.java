package com.example.springsecurity.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponse<T> {
    T result;
    LocalDateTime time;

    public ApiResponse(T result) {
        this.result = result;
        this.time = LocalDateTime.now();
    }
}
