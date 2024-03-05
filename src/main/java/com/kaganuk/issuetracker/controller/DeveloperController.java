package com.kaganuk.issuetracker.controller;

import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/developer")
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping
    public List<Developer> getDevelopers() {
        return this.developerService.getDevelopers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer createDeveloper(@RequestBody Developer developer) {
        return this.developerService.saveDeveloper(developer);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Developer updateDeveloper(@PathVariable Integer id, @RequestBody Developer developer) {
        return this.developerService.updateDeveloper(developer, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeveloper(@PathVariable Integer id) {
        this.developerService.deleteDeveloper(id);
    }


}
