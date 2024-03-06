package com.kaganuk.issuetracker.controller;

import com.kaganuk.issuetracker.model.IssueCreateDto;
import com.kaganuk.issuetracker.model.IssueDto;
import com.kaganuk.issuetracker.service.IssueService;
import com.kaganuk.issuetracker.validation.IssueValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;
    private final IssueValidator issueValidator;

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) {
        binder.addValidators(issueValidator);
    }

    @GetMapping("{id}")
    public IssueDto getIssue(@PathVariable Integer id) {
        return this.issueService.getIssue(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueDto createIssue(@RequestBody @Valid IssueCreateDto issueDto) {
        return this.issueService.saveIssue(issueDto);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public IssueDto updateIssue(@PathVariable Integer id, @RequestBody @Valid IssueDto issueDto) {
        return this.issueService.updateIssue(issueDto, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable Integer id) {
        this.issueService.deleteIssue(id);
    }
}
