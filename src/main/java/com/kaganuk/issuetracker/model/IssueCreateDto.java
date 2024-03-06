package com.kaganuk.issuetracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaganuk.issuetracker.enums.issue.Priority;
import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class IssueCreateDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Integer id;
    protected String title;
    protected String description;
    protected Type type;
    protected Status status;
    protected Priority priority;
    protected Integer estimation;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp createdAt;
}
