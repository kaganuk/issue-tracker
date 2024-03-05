package com.kaganuk.issuetracker.controller;

import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;

    @GetMapping("{id}")
    public Issue getIssue(@PathVariable Integer id) {
        return this.issueService.getIssue(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Issue createIssue(@RequestBody Issue issue) {
        return this.issueService.saveIssue(issue);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Issue updateIssue(@PathVariable Integer id, @RequestBody Issue issue) {
        return this.issueService.updateIssue(issue, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable Integer id) {
        this.issueService.deleteIssue(id);
    }
}
