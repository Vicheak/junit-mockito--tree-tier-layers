package com.vicheak.unittestdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ResourceDuplicatedException.class})
    public ResponseEntity<?> handleResourceDuplicatedException(ResourceDuplicatedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

}
