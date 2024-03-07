package com.kaganuk.issuetracker.service;

import com.kaganuk.issuetracker.exception.NoSuchEntityException;
import com.kaganuk.issuetracker.model.Developer;
import com.kaganuk.issuetracker.model.Issue;
import com.kaganuk.issuetracker.repository.DeveloperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    @BeforeEach
    void setUp() {}

    @Test
    void getDevelopersSuccess() {
        Developer dev1 = Developer.builder().id(1).name("John Doe").build();
        Developer dev2 = Developer.builder().id(1).name("George").build();
        when(developerRepository.findAll()).thenReturn(Arrays.asList(dev1, dev2));

        List<Developer> developers = developerService.getDevelopers();

        assertEquals(2, developers.size());
        verify(developerRepository, times(1)).findAll();
    }

    @Test
    void saveDeveloperSuccess() {
        Developer dev = Developer.builder().id(1).name("John Doe").build();
        when(developerRepository.save(any(Developer.class))).thenReturn(dev);

        Developer savedDeveloper = developerService.saveDeveloper(dev);

        ArgumentCaptor<Developer> developerArgumentInSaveMethod = ArgumentCaptor.forClass(Developer.class);
        verify(developerRepository, times(1)).save(developerArgumentInSaveMethod.capture());
        assertTrue(new ReflectionEquals(developerArgumentInSaveMethod.getValue()).matches(dev));
        assertTrue(new ReflectionEquals(savedDeveloper).matches(dev));
    }

    @Test
    void updateDeveloperSuccess() {
        Developer existingDeveloper = Developer.builder().id(1).name("John Doe").build();
        when(developerRepository.findById(1)).thenReturn(Optional.of(existingDeveloper));
        when(developerRepository.save(any(Developer.class))).thenReturn(existingDeveloper);
        Developer newDeveloperDetails = Developer.builder().name("New Name").build();

        Developer updatedDeveloper = developerService.updateDeveloper(newDeveloperDetails, 1);

        ArgumentCaptor<Developer> developerArgumentInSaveMethod = ArgumentCaptor.forClass(Developer.class);
        verify(developerRepository, times(1)).save(developerArgumentInSaveMethod.capture());
        assertTrue(new ReflectionEquals(developerArgumentInSaveMethod.getValue()).matches(existingDeveloper));
        assertEquals("New Name", updatedDeveloper.getName());
    }

    @Test
    void updateDeveloperNotFound() {
        when(developerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchEntityException.class, () -> developerService.updateDeveloper(new Developer(), 1));
    }

    @Test
    void deleteDeveloperSuccess() {
        when(developerRepository.existsById(1)).thenReturn(true);

        developerService.deleteDeveloper(1);

        verify(developerRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteDeveloperNotFound() {
        when(developerRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchEntityException.class, () -> developerService.deleteDeveloper(1));
    }
}