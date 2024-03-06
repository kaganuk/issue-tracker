package com.kaganuk.issuetracker.model;

import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class PlannedStoryDto {
    protected Integer id;
    protected Integer estimation;
    protected Status status;
    protected Type type;
}
