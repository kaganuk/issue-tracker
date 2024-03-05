package com.kaganuk.issuetracker.controller;

import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/plan")
public class PlanController {


    private final PlanService planService;

    @GetMapping
    public Map<String, List<Issue>> getPlan() {
        return this.planService.planIssues();
    }
}
