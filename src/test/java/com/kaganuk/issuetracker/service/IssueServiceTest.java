package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.exception.NoSuchEntityException;
import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.model.IssueCreateDto;
import com.kaganuk.issuetracker.model.IssueDto;
import com.kaganuk.issuetracker.repository.IssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    private IssueService issueService;

    private Issue issue;
    private Developer developer;
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    @BeforeEach
    public void setUp() {
        developer = Developer.builder()
                .id(1)
                .name("Developer 1")
                .build();

        issue = Issue.builder()
                .id(1)
                .title("Sample Issue")
                .description("This is a sample issue")
                .type(Type.STORY)
                .status(Status.ESTIMATED)
                .estimation(5)
                .assignee(developer)
                .createdAt(timestamp)
                .build();

        issueService = new IssueService(issueRepository, new ModelMapper());
    }

    @Test
    public void testGetIssue() {
        when(issueRepository.findById(Mockito.any())).thenReturn(Optional.of(issue));

        IssueDto result = issueService.getIssue(1);

        IssueDto expectedIssueDto = IssueDto.builder().id(1).build();
        assertEquals(expectedIssueDto.getId(), result.getId());
    }

    @Test
    public void testGetIssueNonExisting() {
        assertThrows(NoSuchEntityException.class, () -> issueService.getIssue(2));
    }

    @Test
    public void testSaveIssue() {
        when(issueRepository.save(any())).thenReturn(issue);

        IssueCreateDto issueCreateDto = IssueCreateDto.builder()
                .id(1)
                .title("Sample Issue")
                .description("This is a sample issue")
                .type(Type.STORY)
                .status(Status.ESTIMATED)
                .estimation(5)
                .createdAt(timestamp)
                .build();

        IssueDto result = issueService.saveIssue(issueCreateDto);

        ArgumentCaptor<Issue> argumentCaptor = ArgumentCaptor.forClass(Issue.class);
        verify(issueRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getId(), issueCreateDto.getId());
        assertEquals(issueCreateDto.getId(), result.getId());
    }

    @Test
    public void testUpdateIssue() {
        IssueDto updatedIssueDto = IssueDto.builder()
                .id(1)
                .title("Updated Issue")
                .description("This is a sample issue")
                .type(Type.STORY)
                .status(Status.ESTIMATED)
                .estimation(5)
                .assigneeId(developer.getId())
                .createdAt(timestamp)
                .build();

        when(issueRepository.findById(Mockito.any())).thenReturn(Optional.of(issue));
        when(issueRepository.save(any())).thenReturn(issue);

        IssueDto result = issueService.updateIssue(updatedIssueDto, 1);

        ArgumentCaptor<Issue> issueArgumentInSaveMethod = ArgumentCaptor.forClass(Issue.class);
        verify(issueRepository, times(1)).save(issueArgumentInSaveMethod.capture());
        assertTrue(new ReflectionEquals(issueArgumentInSaveMethod.getValue()).matches(issue));
        assertTrue(new ReflectionEquals(result).matches(updatedIssueDto));
    }

    @Test
    public void testUpdateIssueNonExisting() {
        assertThrows(NoSuchEntityException.class, () -> issueService.getIssue(2));
    }

    @Test
    public void testDeleteIssue() {
        when(issueRepository.existsById(1)).thenReturn(true);

        issueService.deleteIssue(1);

        verify(issueRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteNonExistingIssue() {
        when(issueRepository.existsById(2)).thenReturn(false);

        assertThrows(NoSuchEntityException.class, () -> issueService.deleteIssue(2));

        verify(issueRepository, Mockito.never()).deleteById(2);
    }
}
