package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.*;
import com.example.gestionstationskii.services.SkierServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkierServicesImplMockTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    private Skier sampleSkier;
    private Subscription sampleSubscription;

    @BeforeEach
    void setUp() {
        sampleSubscription = new Subscription();
        sampleSubscription.setNumSub(1L);
        sampleSubscription.setTypeSub(TypeSubscription.ANNUAL);
        sampleSubscription.setStartDate(LocalDate.of(2025, 1, 1));

        sampleSkier = new Skier();
        sampleSkier.setNumSkier(1L);
        sampleSkier.setFirstName("Alice");
        sampleSkier.setLastName("Durand");
        sampleSkier.setDateOfBirth(LocalDate.of(1995, 3, 15));
        sampleSkier.setCity("Grenoble");
        sampleSkier.setSubscription(sampleSubscription);
    }

    @Test
    void testRetrieveAllSkiers() {
        when(skierRepository.findAll()).thenReturn(Arrays.asList(sampleSkier));

        List<Skier> result = skierServices.retrieveAllSkiers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        verify(skierRepository, times(1)).findAll();
    }

    @Test
    void testAddSkier() {
        when(skierRepository.save(any(Skier.class))).thenReturn(sampleSkier);

        Skier saved = skierServices.addSkier(sampleSkier);

        assertNotNull(saved);
        assertEquals(LocalDate.of(2026, 1, 1), saved.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(sampleSkier);
    }

    @Test
    void testAssignSkierToSubscription() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(sampleSkier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(sampleSubscription));
        when(skierRepository.save(sampleSkier)).thenReturn(sampleSkier);

        Skier result = skierServices.assignSkierToSubscription(1L, 1L);

        assertNotNull(result);
        assertEquals(sampleSubscription, result.getSubscription());
        verify(skierRepository, times(1)).findById(1L);
        verify(subscriptionRepository, times(1)).findById(1L);
        verify(skierRepository, times(1)).save(sampleSkier);
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        Course course = new Course();
        course.setNumCourse(5L);

        Registration reg = new Registration();
        Set<Registration> registrations = new HashSet<>();
        registrations.add(reg);
        sampleSkier.setRegistrations(registrations);

        when(skierRepository.save(sampleSkier)).thenReturn(sampleSkier);
        when(courseRepository.getById(5L)).thenReturn(course);
        when(registrationRepository.save(any(Registration.class))).thenReturn(reg);

        Skier result = skierServices.addSkierAndAssignToCourse(sampleSkier, 5L);

        assertNotNull(result);
        verify(courseRepository, times(1)).getById(5L);
        verify(registrationRepository, times(1)).save(reg);
    }

    @Test
    void testRemoveSkier() {
        doNothing().when(skierRepository).deleteById(1L);

        skierServices.removeSkier(1L);

        verify(skierRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrieveSkier() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(sampleSkier));

        Skier found = skierServices.retrieveSkier(1L);

        assertNotNull(found);
        assertEquals(1L, found.getNumSkier());
        verify(skierRepository, times(1)).findById(1L);
    }

    @Test
    void testAssignSkierToPiste() {
        Piste piste = new Piste();
        piste.setNumPiste(10L);
        piste.setNamePiste("Piste Noire");

        when(skierRepository.findById(1L)).thenReturn(Optional.of(sampleSkier));
        when(pisteRepository.findById(10L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(sampleSkier)).thenReturn(sampleSkier);

        Skier result = skierServices.assignSkierToPiste(1L, 10L);

        assertNotNull(result);
        assertNotNull(result.getPistes());
        assertTrue(result.getPistes().contains(piste));
        verify(skierRepository, times(1)).findById(1L);
        verify(pisteRepository, times(1)).findById(10L);
        verify(skierRepository, times(1)).save(sampleSkier);
    }
}
