package com.kaganuk.issuetracker.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDetails> defaultExceptionHandler(Exception exception){
        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessages(List.of("Internal server error"))
                .exceptionName(exception.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NoSuchEntityException.class)
    public ResponseEntity<ApiErrorDetails> handleEntityNotFoundException(NoSuchEntityException exception) {

        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessages(List.of(exception.getMessage()))
                .exceptionName(exception.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<String> errorMessages = new ArrayList<>();
        if (ex.getBindingResult().getFieldError() != null)
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                errorMessages.add(fieldError.getDefaultMessage());
            }


        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessages(errorMessages)
                .exceptionName(ex.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.BAD_REQUEST);
    }
}
