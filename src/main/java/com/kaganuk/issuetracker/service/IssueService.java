package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IssueService {
    private final IssueRepository issueRepository;

    public Issue getIssue(Integer id) {
        return issueRepository.findById(id).orElseThrow();
    }

    public Issue saveIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public Issue updateIssue(Issue issue, Integer id) {
        // implement
        return issueRepository.save(issue);
    }

    public void deleteIssue(Integer id) {
        issueRepository.deleteById(id);
    }
}
