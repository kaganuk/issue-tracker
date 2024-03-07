package com.kaganuk.issuetracker.validation;

import com.kaganuk.issuetracker.enums.issue.Priority;
import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.model.IssueCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IssueValidatorTest {

    private IssueValidator issueValidator;

    @BeforeEach
    void setUp() {
        issueValidator = new IssueValidator();
    }

    @Test
    void supportsWhenValidClassShouldReturnTrue() {
        assertTrue(issueValidator.supports(IssueCreateDto.class));
    }

    @Test
    void supportsWhenInvalidClassShouldReturnFalse() {
        assertFalse(issueValidator.supports(String.class));
    }

    @Test
    void validateWhenTypeIsNullShouldRejectTypeField() {
        IssueCreateDto request = new IssueCreateDto();
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("type"));
        assertEquals("type.null", errors.getFieldError("type").getCode());
    }

    @Test
    void validateWhenTypeIsBugAndEstimationNotNullShouldRejectEstimationField() {
        IssueCreateDto request = new IssueCreateDto();
        request.setType(Type.BUG);
        request.setEstimation(5); // Not allowed for bugs
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("estimation"));
        assertEquals("estimation.not_null", errors.getFieldError("estimation").getCode());
    }

    @Test
    void validateWhenTypeIsBugAndPriorityIsNullShouldRejectPriorityField() {
        IssueCreateDto request = new IssueCreateDto();
        request.setType(Type.BUG);
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("priority"));
        assertEquals("priority.null", errors.getFieldError("priority").getCode());
    }

    @Test
    void validateWhenTypeIsBugAndStatusIsInvalidShouldRejectStatusField() {
        IssueCreateDto request = new IssueCreateDto();
        request.setType(Type.BUG);
        request.setStatus(Status.ESTIMATED); // Invalid status for bugs
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("status"));
        assertEquals("status.invalid", errors.getFieldError("status").getCode());
    }

    @Test
    void validateWhenTypeIsStoryAndStatusIsEstimatedAndEstimationIsNullShouldRejectEstimationField() {
        IssueCreateDto request = new IssueCreateDto();
        request.setType(Type.STORY);
        request.setStatus(Status.ESTIMATED);
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("estimation"));
        assertEquals("estimation.null", errors.getFieldError("estimation").getCode());
    }

    @Test
    void validateWhenTypeIsStoryAndEstimationIsOutOfRangeShouldRejectEstimationField() {
        IssueCreateDto request = new IssueCreateDto();
        request.setType(Type.STORY);
        request.setEstimation(15); // Invalid estimation range
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("estimation"));
        assertEquals("estimation.invalid", errors.getFieldError("estimation").getCode());
    }

    @Test
    void validateWhenTypeIsStoryAndPriorityIsNotNullShouldRejectPriorityField() {
        IssueCreateDto request = new IssueCreateDto();
        request.setType(Type.STORY);
        request.setPriority(Priority.CRITICAL); // Priority not allowed for stories
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("priority"));
        assertEquals("priority.not_null", errors.getFieldError("priority").getCode());
    }

    @Test
    void validateWhenTypeIsStoryAndStatusIsInvalidShouldRejectStatusField() {
        IssueCreateDto request = new IssueCreateDto();
        request.setType(Type.STORY);
        request.setStatus(Status.VERIFIED); // Invalid status for stories
        Errors errors = new BeanPropertyBindingResult(request, "request");
        issueValidator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("status"));
        assertEquals("status.invalid", errors.getFieldError("status").getCode());
    }
}
