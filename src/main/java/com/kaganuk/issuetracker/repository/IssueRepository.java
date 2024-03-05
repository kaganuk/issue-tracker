package com.kaganuk.issuetracker.repository;

import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Integer> {
    List<Issue> findIssuesByTypeOrderByEstimationDesc(Type type);
}
