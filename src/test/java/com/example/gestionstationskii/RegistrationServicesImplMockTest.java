package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IRegistrationRepository;
import com.example.gestionstationskii.repositories.ISkierRepository;
import com.example.gestionstationskii.services.RegistrationServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServicesImplMockTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Registration sampleRegistration;
    private Skier sampleSkier;
    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        sampleRegistration = new Registration();
        sampleRegistration.setNumRegistration(1L);
        sampleRegistration.setNumWeek(5);

        sampleSkier = new Skier();
        sampleSkier.setNumSkier(10L);
        sampleSkier.setFirstName("Sophie");
        sampleSkier.setDateOfBirth(LocalDate.of(2000, 5, 20));

        sampleCourse = new Course();
        sampleCourse.setNumCourse(20L);
        sampleCourse.setTypeCourse(TypeCourse.INDIVIDUAL);
        sampleCourse.setSupport(Support.SKI);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        when(skierRepository.findById(10L)).thenReturn(Optional.of(sampleSkier));
        when(registrationRepository.save(sampleRegistration)).thenReturn(sampleRegistration);

        Registration saved = registrationServices.addRegistrationAndAssignToSkier(sampleRegistration, 10L);

        assertNotNull(saved);
        assertEquals(sampleSkier, saved.getSkier());
        verify(skierRepository, times(1)).findById(10L);
        verify(registrationRepository, times(1)).save(sampleRegistration);
    }

    @Test
    void testAssignRegistrationToCourse() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(sampleRegistration));
        when(courseRepository.findById(20L)).thenReturn(Optional.of(sampleCourse));
        when(registrationRepository.save(sampleRegistration)).thenReturn(sampleRegistration);

        Registration updated = registrationServices.assignRegistrationToCourse(1L, 20L);

        assertNotNull(updated);
        assertEquals(sampleCourse, updated.getCourse());
        verify(registrationRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(20L);
        verify(registrationRepository, times(1)).save(sampleRegistration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse() {
        when(skierRepository.findById(10L)).thenReturn(Optional.of(sampleSkier));
        when(courseRepository.findById(20L)).thenReturn(Optional.of(sampleCourse));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(5, 10L, 20L)).thenReturn(0L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(sampleRegistration);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(sampleRegistration, 10L, 20L);

        assertNotNull(result);
        assertEquals(sampleSkier, result.getSkier());
        assertEquals(sampleCourse, result.getCourse());
        verify(registrationRepository, times(1)).save(sampleRegistration);
    }

    @Test
    void testNumWeeksCourseOfInstructorBySupport() {
        List<Integer> expectedWeeks = Arrays.asList(1, 2, 3);
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(1L, Support.SKI)).thenReturn(expectedWeeks);

        List<Integer> weeks = registrationServices.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        assertNotNull(weeks);
        assertEquals(3, weeks.size());
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
    }
}
