package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.exception.NoDevelopersFoundException;
import com.kaganuk.issuetracker.exception.NoIssuesFoundException;
import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.model.PlannedStoryResponseDto;
import com.kaganuk.issuetracker.repository.DeveloperRepository;
import com.kaganuk.issuetracker.repository.IssueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

	@Mock
	private IssueRepository issueRepository;

	@Mock
	private DeveloperRepository developerRepository;

	private PlanService planService;

	private final List<Developer> developers = new ArrayList<>();
	private final List<Issue> issues = new ArrayList<>();

	@BeforeEach
	public void setUp(){
		planService = new PlanService(developerRepository, issueRepository, new ModelMapper());
		this.createIssues();
	}
	@Test
	void testPlanIssuesNoDevelopersFoundExceptionThrown() {
		when(developerRepository.count()).thenReturn(0L);
		assertThrows(NoDevelopersFoundException.class, planService::planIssues);
	}

	@Test
	void testPlanIssuesNoIssuesFoundExceptionThrown() {
		when(developerRepository.count()).thenReturn(1L);
		when(issueRepository.findIssuesByTypeAndStatusOrderByEstimationDesc(
				Type.STORY, Status.ESTIMATED)).thenReturn(Collections.emptyList());

		assertThrows(NoIssuesFoundException.class, planService::planIssues);
	}

	@Test
	public void testPlanIssues(){
		when(developerRepository.count()).thenReturn(3L);
		when(issueRepository.findIssuesByTypeAndStatusOrderByEstimationDesc(any(Type.class), any(Status.class))).thenReturn(issues);

		Map<String, List<PlannedStoryResponseDto>> weeklyMappedIssues = planService.planIssues();

        Assertions.assertTrue(weeklyMappedIssues.containsKey("Week 1"));
        Assertions.assertTrue(weeklyMappedIssues.containsKey("Week 2"));
        Assertions.assertTrue(weeklyMappedIssues.containsKey("Week 3"));
        Assertions.assertFalse(weeklyMappedIssues.containsKey("Week 4"));

		List<Integer> expectedTotalEstimations = List.of(30, 30 , 2);
		int week = 0;
		for (List<PlannedStoryResponseDto> issuesOfWeek : weeklyMappedIssues.values()) {
			int totalEstimationOfWeek = 0;
			for (PlannedStoryResponseDto issue : issuesOfWeek) {
				totalEstimationOfWeek += issue.getEstimation();
			}
            Assertions.assertEquals(totalEstimationOfWeek, (int) expectedTotalEstimations.get(week));
			week++;
		}
	}


	private void createIssues() {
		List<Integer> estimations = List.of(10, 9, 8, 7, 5, 5, 5, 4, 2, 2, 2, 2, 1);
		for (int i = 0; i < estimations.size(); i++) {
			issues.add(Issue.builder()
					.id(i)
					.title("Issue Title " + i)
					.description("Issue Description " + i)
					.type(Type.STORY)
					.status(Status.ESTIMATED)
					.estimation(estimations.get(i)).build());
		}
	}
}
