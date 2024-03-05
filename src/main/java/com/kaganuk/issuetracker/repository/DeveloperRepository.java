package com.kaganuk.issuetracker.repository;

import com.kaganuk.issuetracker.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Integer> {
}
