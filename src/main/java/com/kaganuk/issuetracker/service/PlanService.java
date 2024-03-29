package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.enums.issue.Status;
import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.exception.NoDevelopersFoundException;
import com.kaganuk.issuetracker.exception.NoIssuesFoundException;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.model.PlannedStoryResponseDto;
import com.kaganuk.issuetracker.repository.DeveloperRepository;
import com.kaganuk.issuetracker.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PlanService {

    private final DeveloperRepository developerRepository;
    private final IssueRepository issueRepository;
    private final ModelMapper modelMapper;
    private Long developerSize = 0L;

    public Map<String, List<PlannedStoryResponseDto>> planIssues() {
        developerSize = this.developerRepository.count();
        if (developerSize == 0L)
            throw new NoDevelopersFoundException();

        List<Issue> issues = this.issueRepository.
                findIssuesByTypeAndStatusOrderByEstimationDesc(Type.STORY, Status.ESTIMATED);
        if (issues.isEmpty())
            throw new NoIssuesFoundException();

        return assignDevelopersToIssuesWithFirstFitDecreasingAlgorithm(issues);
    }

    private Map<String, List<PlannedStoryResponseDto>> assignDevelopersToIssuesWithFirstFitDecreasingAlgorithm(
            List<Issue> issues) {
        int amountOfWeeks = 0;
        Map<String, List<PlannedStoryResponseDto>> weeklyGroupedIssues = new HashMap<>();
        int[] estimationStorage = new int[issues.size()];

        for (Issue issue : issues) {
            int week = ifPossibleAddIssueToFirstWeekFitting(issue, amountOfWeeks, estimationStorage, weeklyGroupedIssues);

            amountOfWeeks = createNewWeekIfThereIsNoSpaceAvailable(
                    issue, week, amountOfWeeks, estimationStorage, weeklyGroupedIssues);
        }

        return weeklyGroupedIssues;
    }

    private int createNewWeekIfThereIsNoSpaceAvailable(Issue issue,
                                                       int week,
                                                       int amountOfWeeks,
                                                       int[] estimationStorage,
                                                       Map<String, List<PlannedStoryResponseDto>> weeklyGroupedIssues) {
        if (week == amountOfWeeks) {
            createNewWeekForEstimation(issue, estimationStorage, amountOfWeeks);
            addIssueToWeeklyGroupedIssues(issue, amountOfWeeks, weeklyGroupedIssues);
            amountOfWeeks++;
        }
        return amountOfWeeks;
    }

    private int ifPossibleAddIssueToFirstWeekFitting(Issue issue,
                                                     int amountOfWeeks,
                                                     int[] estimationStorage,
                                                     Map<String, List<PlannedStoryResponseDto>> weeklyGroupedIssues) {
        int week;
        for (week = 0; week < amountOfWeeks; week++) {
            boolean isSpaceAvailableForTheWeek = estimationStorage[week] >= issue.getEstimation();
            if (isSpaceAvailableForTheWeek) {
                removeEstimationFromFirstWeekPossible(issue.getEstimation(), estimationStorage, week);
                addIssueToWeeklyGroupedIssues(issue, week, weeklyGroupedIssues);
                break;
            }
        }
        return week;
    }

    private void addIssueToWeeklyGroupedIssues(
            Issue issue, int index, Map<String, List<PlannedStoryResponseDto>> weeklyGroupedIssues) {
        String week = "Week " + (index + 1);
        weeklyGroupedIssues.computeIfAbsent(week, k -> new ArrayList<>());
        PlannedStoryResponseDto plannedStoryResponseDto = this.modelMapper.map(issue, PlannedStoryResponseDto.class);
        weeklyGroupedIssues.get(week).add(plannedStoryResponseDto);
    }

    private static void removeEstimationFromFirstWeekPossible(int estimation, int[] storage, int index) {
        storage[index] -= estimation;
    }

    private void createNewWeekForEstimation(Issue issue, int[] storage, int index) {
        int storyPointsPerWeek = Issue.AVG_STORY_POINTS_PER_DEVELOPER * developerSize.intValue();
        storage[index] = storyPointsPerWeek - issue.getEstimation();
    }
}
