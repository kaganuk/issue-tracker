package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.repository.DeveloperRepository;
import com.kaganuk.issuetracker.repository.IssueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTests {

	@Mock
	private IssueRepository issueRepository;

	@Mock
	private DeveloperRepository developerRepository;

	@InjectMocks
	private PlanService planService;

	private final Random rand = new Random();
	private final List<Developer> developers = new ArrayList<>();
	private final List<Issue> issues = new ArrayList<>();

	@BeforeEach
	public void setup(){
		this.createDevelopers();
		this.createIssues();
	}

	@Test
	public void givenStoriesAndDevelopers_whenPlanIssues_thenReturnWeeklyPlannedIssues(){
		when(developerRepository.findAll()).thenReturn(developers);
		when(issueRepository.findIssuesByTypeOrderByEstimationDesc(any(Type.class))).thenReturn(issues);

		Map<String, List<Issue>> weeklyMappedIssues = planService.planIssues();

        Assertions.assertTrue(weeklyMappedIssues.containsKey("Week 1"));
        Assertions.assertTrue(weeklyMappedIssues.containsKey("Week 2"));
        Assertions.assertTrue(weeklyMappedIssues.containsKey("Week 3"));
        Assertions.assertFalse(weeklyMappedIssues.containsKey("Week 4"));

		List<Integer> expectedTotalEstimations = List.of(30, 30 , 2);
		int week = 0;
		for (List<Issue> issuesOfWeek : weeklyMappedIssues.values()) {
			int totalEstimationOfWeek = 0;
			for (Issue issue : issuesOfWeek) {
				totalEstimationOfWeek += issue.getEstimation();
			}
			Assertions.assertTrue(totalEstimationOfWeek <= expectedTotalEstimations.get(week));
			week++;
		}
	}

	private void createDevelopers() {
		for (int i = 0; i < 3; i++) {
			developers.add(
					Developer.builder().id(i).name("Developer " + (i+1)).build()
			);
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
