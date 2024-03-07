package com.kaganuk.issuetracker.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDetails> defaultExceptionHandler(Exception exception){
        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessage("Internal server error")
                .exceptionName(exception.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        logApiErrorDetails(apiErrorDetails);

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NoSuchEntityException.class)
    public ResponseEntity<ApiErrorDetails> handleEntityNotFoundException(NoSuchEntityException exception) {

        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessage(exception.getMessage())
                .exceptionName(exception.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        logApiErrorDetails(apiErrorDetails);

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


        ValidationErrorDetails validationErrorDetails = ValidationErrorDetails.builder()
                .validationErrors(errorMessages)
                .currentTime(LocalDateTime.now())
                .build();

        log.error("validationErrors: {} currentTime: {}",
                String.join(", ", validationErrorDetails.getValidationErrors()),
                validationErrorDetails.getCurrentTime());

        return new ResponseEntity<>(validationErrorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Cannot add, update or delete a parent/child row: a foreign key constraint fails";
        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessage(errorMessage)
                .exceptionName(ex.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        logApiErrorDetails(apiErrorDetails);

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiErrorDetails> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException ex) {

        ApiErrorDetails apiErrorDetails = ApiErrorDetails.builder()
                .errorMessage("Foreign key constraint has failed")
                .exceptionName(ex.getClass().getSimpleName())
                .currentTime(LocalDateTime.now())
                .build();

        logApiErrorDetails(apiErrorDetails);

        return new ResponseEntity<>(apiErrorDetails, HttpStatus.BAD_REQUEST);
    }

    private static void logApiErrorDetails(ApiErrorDetails apiErrorDetails) {
        log.error("errorMessage: {} exceptionName: {} currentTime: {}",
                apiErrorDetails.getErrorMessage(),
                apiErrorDetails.getExceptionName(),
                apiErrorDetails.getCurrentTime());
    }
}
