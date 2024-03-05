package com.kaganuk.issuetracker.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ApiErrorDetails {
    private LocalDateTime currentTime;

    @JsonIgnore
    private String exceptionName;

    private String errorMessage;
}
