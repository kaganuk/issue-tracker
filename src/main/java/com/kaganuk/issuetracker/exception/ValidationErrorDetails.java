package com.kaganuk.issuetracker.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ValidationErrorDetails {
    private LocalDateTime currentTime;
    private List<String> validationErrors;
}
