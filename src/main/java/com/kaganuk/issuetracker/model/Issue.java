package com.kaganuk.issuetracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.enums.issue.Priority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Issue {
    public static final Integer AVG_STORY_POINTS_PER_DEVELOPER = 10;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String description;

    @Enumerated
    private Type type;

    @Enumerated
    private Status status;

    @Enumerated
    private Priority priority;

    @Column
    private Integer estimation;

    @ManyToOne
    private Developer assignee;

    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp createdAt;
}
