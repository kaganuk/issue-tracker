package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.exception.NoSuchEntityException;
import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.model.IssueCreateDto;
import com.kaganuk.issuetracker.model.IssueDto;
import com.kaganuk.issuetracker.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final ModelMapper modelMapper;

    public IssueDto getIssue(Integer id) {
        Issue issue = getIssueOrThrowException(id);

        return modelMapper.map(issue, IssueDto.class);
    }

    public IssueDto saveIssue(IssueCreateDto issueCreateDto) {
        Issue issue = modelMapper.map(issueCreateDto, Issue.class);

        return modelMapper.map(issueRepository.save(issue), IssueDto.class);
    }

    public IssueDto updateIssue(IssueDto issueDto, Integer id) {
        Issue issue = getIssueOrThrowException(id);
        Issue issueRequest = modelMapper.map(issueDto, Issue.class);
        updateExistingIssue(issue, issueRequest);

        return modelMapper.map(issueRepository.save(issue), IssueDto.class);
    }

    public void deleteIssue(Integer id) {
        if (!issueRepository.existsById(id))
            throw new NoSuchEntityException(Developer.class.getSimpleName(), id);

        issueRepository.deleteById(id);
    }

    private static void updateExistingIssue(Issue issue, Issue issueRequest) {
        if (issueRequest.getTitle() != null) issue.setTitle(issueRequest.getTitle());
        if (issueRequest.getDescription() != null) issue.setDescription(issueRequest.getDescription());
        if (issueRequest.getType() != null) issue.setType(issueRequest.getType());
        if (issueRequest.getStatus() != null) issue.setStatus(issueRequest.getStatus());
        if (issueRequest.getPriority() != null) issue.setPriority(issueRequest.getPriority());
        if (issueRequest.getEstimation() != null) issue.setEstimation(issueRequest.getEstimation());
        if (issueRequest.getAssignee() != null) issue.setAssignee(issueRequest.getAssignee());
    }

    private Issue getIssueOrThrowException(Integer id) {
        Optional<Issue> issueFromDb = issueRepository.findById(id);

        if (issueFromDb.isEmpty())
            throw new NoSuchEntityException(this.getClass().getSimpleName(), id);

        return issueFromDb.get();
    }
}
