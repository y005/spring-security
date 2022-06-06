package com.example.springsecurity.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtRestControllerAdvice {
    @ExceptionHandler
    public String error(Exception e) {
        return e.getMessage();
    }
}
