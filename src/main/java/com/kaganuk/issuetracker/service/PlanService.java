package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.enums.issue.Type;
import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.repository.DeveloperRepository;
import com.kaganuk.issuetracker.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
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

    public Map<String, List<Issue>> planIssues() {
        List<Developer> developers = this.developerRepository.findAll();
        List<Issue> issues = this.issueRepository.findIssuesByTypeOrderByEstimationDesc(Type.STORY);

        return assignDevelopersToIssuesWithBestFitAlgorithm(issues, developers);
    }

    private Map<String, List<Issue>>  assignDevelopersToIssuesWithBestFitAlgorithm(List<Issue> issues, List<Developer> developers) {
        int weeksRequired = 0, currentIndexForEstimationStorage = 0;
        int storyPointsPerWeek = Issue.AVG_STORY_POINTS_PER_DEVELOPER * developers.size();
        Map<String, List<Issue>> weeklyGroupedIssues = new HashMap<>();
        int[] estimationStorage = new int[issues.size()];

        for (Issue issue : issues) {
            BestWeek bestWeek = getBestWeek(issue, storyPointsPerWeek, weeksRequired, estimationStorage);

            if (noExistingWeekAvailable(bestWeek.minimumSpaceLeft(), storyPointsPerWeek)) {
                createNewWeekForEstimation(issue, estimationStorage, weeksRequired, storyPointsPerWeek);
                currentIndexForEstimationStorage = weeksRequired;
                weeksRequired++;
            } else {
                removeEstimationFromBestWeekExisting(issue.getEstimation(), estimationStorage, bestWeek.index());
                currentIndexForEstimationStorage = bestWeek.index();
            }

            addIssueToWeeklyGroupedIssues(issue, currentIndexForEstimationStorage, weeklyGroupedIssues);
        }

        return weeklyGroupedIssues;
    }

    private static boolean noExistingWeekAvailable(int minimumSpaceLeftForBestWeek, int storyPointsPerWeek) {
        return minimumSpaceLeftForBestWeek == storyPointsPerWeek + 1;
    }

    private static BestWeek getBestWeek(Issue issue, int storyPointsPerWeek, int weeksRequired, int[] estimationStorage) {
        int  minimumSpaceLeftForBestWeek = storyPointsPerWeek + 1, indexOfBestWeek = 0;
        for (int week = 0; week < weeksRequired; week++) {
            int spaceLeftForTheWeek = estimationStorage[week] - issue.getEstimation();
            boolean isSpaceAvailableForTheWeek = estimationStorage[week] >= issue.getEstimation();
            if (isSpaceAvailableForTheWeek && spaceLeftForTheWeek < minimumSpaceLeftForBestWeek) {
                indexOfBestWeek = week;
                minimumSpaceLeftForBestWeek = spaceLeftForTheWeek;
            }
        }
        return new BestWeek(minimumSpaceLeftForBestWeek, indexOfBestWeek);
    }

    private record BestWeek(int minimumSpaceLeft, int index) {
    }

    private static void addIssueToWeeklyGroupedIssues(Issue issue, int index, Map<String, List<Issue>> weeklyGroupedIssues) {
        String week = "Week " + (index + 1);
        weeklyGroupedIssues.computeIfAbsent(week, k -> new ArrayList<>());
        weeklyGroupedIssues.get(week).add(issue);
    }

    private static void removeEstimationFromBestWeekExisting(int estimation, int[] storage, int indexOfBestWeek) {
        storage[indexOfBestWeek] -= estimation;
    }

    private static void createNewWeekForEstimation(Issue issue, int[] storage, int weeksRequired, int storyPointsPerWeek) {
        storage[weeksRequired] = storyPointsPerWeek - issue.getEstimation();
    }
}
