package com.kaganuk.issuetracker.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class IssueDto extends IssueCreateDto {
    private Integer assigneeId;
}
