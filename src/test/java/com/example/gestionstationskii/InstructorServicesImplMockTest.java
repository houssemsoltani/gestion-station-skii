package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IInstructorRepository;
import com.example.gestionstationskii.services.InstructorServicesImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServicesImplMockTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor sampleInstructor;

    @BeforeEach
    void setUp() {
        sampleInstructor = new Instructor();
        sampleInstructor.setNumInstructor(1L);
        sampleInstructor.setFirstName("Jean");
        sampleInstructor.setLastName("Dupont");
        sampleInstructor.setDateOfHire(LocalDate.of(2020, 1, 15));
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(sampleInstructor)).thenReturn(sampleInstructor);

        Instructor saved = instructorServices.addInstructor(sampleInstructor);

        assertNotNull(saved);
        assertEquals("Jean", saved.getFirstName());
        verify(instructorRepository, times(1)).save(sampleInstructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(sampleInstructor));

        List<Instructor> instructors = instructorServices.retrieveAllInstructors();

        assertNotNull(instructors);
        assertEquals(1, instructors.size());
        assertEquals("Dupont", instructors.get(0).getLastName());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        sampleInstructor.setLastName("Martin");
        when(instructorRepository.save(sampleInstructor)).thenReturn(sampleInstructor);

        Instructor updated = instructorServices.updateInstructor(sampleInstructor);

        assertNotNull(updated);
        assertEquals("Martin", updated.getLastName());
        verify(instructorRepository, times(1)).save(sampleInstructor);
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(sampleInstructor));

        Instructor found = instructorServices.retrieveInstructor(1L);

        assertNotNull(found);
        assertEquals(1L, found.getNumInstructor());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        Course course = new Course();
        course.setNumCourse(10L);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Instructor result = instructorServices.addInstructorAndAssignToCourse(sampleInstructor, 10L);

        assertNotNull(result);
        assertNotNull(result.getCourses());
        assertTrue(result.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(10L);
        verify(instructorRepository, times(1)).save(sampleInstructor);
    }
}
