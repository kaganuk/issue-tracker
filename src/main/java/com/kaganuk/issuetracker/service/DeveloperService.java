package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.exception.NoSuchEntityException;
import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeveloperService {
    private final DeveloperRepository developerRepository;

    public List<Developer> getDevelopers() {
        return developerRepository.findAll();
    }

    public Developer saveDeveloper(Developer developer) {
        return developerRepository.save(developer);
    }

    public Developer updateDeveloper(Developer developer, Integer id) {
        Optional<Developer> devFromDb = developerRepository.findById(id);
        if (devFromDb.isEmpty())
            throw new NoSuchEntityException(Developer.class.getSimpleName(), id);

        devFromDb.get().setName(developer.getName());

        return developerRepository.save(devFromDb.get());
    }

    public void deleteDeveloper(Integer id) {
        if (!developerRepository.existsById(id))
            throw new NoSuchEntityException(Developer.class.getSimpleName(), id);

        developerRepository.deleteById(id);
    }
}
