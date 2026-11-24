package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.entities.TypeCourse;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.services.CourseServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServicesImplMockTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        sampleCourse = new Course();
        sampleCourse.setNumCourse(1L);
        sampleCourse.setLevel(2);
        sampleCourse.setTypeCourse(TypeCourse.INDIVIDUAL);
        sampleCourse.setSupport(Support.SKI);
        sampleCourse.setPrice(150.0f);
        sampleCourse.setTimeSlot(60);
    }

    @Test
    void testRetrieveAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(sampleCourse));

        List<Course> courses = courseServices.retrieveAllCourses();

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(TypeCourse.INDIVIDUAL, courses.get(0).getTypeCourse());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddCourse() {
        when(courseRepository.save(sampleCourse)).thenReturn(sampleCourse);

        Course saved = courseServices.addCourse(sampleCourse);

        assertNotNull(saved);
        assertEquals(1L, saved.getNumCourse());
        verify(courseRepository, times(1)).save(sampleCourse);
    }

    @Test
    void testUpdateCourse() {
        sampleCourse.setPrice(180.0f);
        when(courseRepository.save(sampleCourse)).thenReturn(sampleCourse);

        Course updated = courseServices.updateCourse(sampleCourse);

        assertNotNull(updated);
        assertEquals(180.0f, updated.getPrice());
        verify(courseRepository, times(1)).save(sampleCourse);
    }

    @Test
    void testRetrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));

        Course retrieved = courseServices.retrieveCourse(1L);

        assertNotNull(retrieved);
        assertEquals(1L, retrieved.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }
}
