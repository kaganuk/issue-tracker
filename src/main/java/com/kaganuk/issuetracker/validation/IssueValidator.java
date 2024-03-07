package com.kaganuk.issuetracker.validation;

import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.model.IssueCreateDto;
import com.kaganuk.issuetracker.model.IssueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IssueValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return IssueCreateDto.class.isAssignableFrom(clazz) || IssueDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        IssueCreateDto request = (IssueCreateDto) target;

        if (request.getType() == null){
            String field = "type";
            errors.rejectValue(field, field + ".null", field + " cannot be null.");
            return;
        }

        validateIssue(errors, request);
    }

    private static void validateIssue(Errors errors, IssueCreateDto request) {
        switch (request.getType()) {
            case STORY:
                validateStory(errors, request);
                break;
            case BUG:
                validateBug(errors, request);
                break;
        }
    }

    private static void validateBug(Errors errors, IssueCreateDto request) {
        if (request.getEstimation() != null) {
            String field = "estimation";
            errors.rejectValue(field, field + ".not_null", field + " for type bug cannot be filled.");
        }
        if (request.getPriority() == null) {
            String field = "priority";
            errors.rejectValue(field, field + ".null", field +" for type bug cannot be null.");
        }
        List<Status> availableStatusesForBugs = List.of(Status.NEW, Status.VERIFIED, Status.RESOLVED);
        if (request.getStatus() != null && !availableStatusesForBugs.contains(request.getStatus())) {
            String field = "status";
            errors.rejectValue(field, field + ".invalid" , field + " for type bug cannot be filled with the given value. Supported statuses for a bug: " + availableStatusesForBugs);
        }
    }

    private static void validateStory(Errors errors, IssueCreateDto request) {
        if (request.getStatus() == Status.ESTIMATED && request.getEstimation() == null) {
            String field = "estimation";
            errors.rejectValue(field, field + ".null", field + " for type story with status ESTIMATED cannot be null.");
        }
        if (request.getEstimation() != null && (request.getEstimation() <= 0 || request.getEstimation() > Issue.AVG_STORY_POINTS_PER_DEVELOPER)) {
            String field = "estimation";
            errors.rejectValue(field, field + ".invalid", field + " should be between 0 - " + (Issue.AVG_STORY_POINTS_PER_DEVELOPER + 1));
        }
        if (request.getPriority() != null) {
            String field = "priority";
            errors.rejectValue(field, field + ".not_null", field + " for type story cannot be filled.");
        }
        List<Status> availableStatusesForStories = List.of(Status.NEW, Status.ESTIMATED, Status.COMPLETED);
        if (request.getStatus() != null && !availableStatusesForStories.contains(request.getStatus())) {
            String field = "status";
            errors.rejectValue(field, field + ".invalid", field + " for type story cannot be filled with the given value. Supported statuses for a story: " + availableStatusesForStories);
        }
    }
}
