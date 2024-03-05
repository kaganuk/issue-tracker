package com.kaganuk.issuetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDetails> defaultExceptionHandler(Exception exception){
        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessage("Internal server error")
                .exceptionName(exception.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NoSuchEntityException.class)
    public ResponseEntity<ApiErrorDetails> handleEntityNotFoundException(NoSuchEntityException ex) {

        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessage(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.NOT_FOUND);
    }
}
