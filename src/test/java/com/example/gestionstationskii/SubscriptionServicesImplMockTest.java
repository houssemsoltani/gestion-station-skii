package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Skier;
import com.example.gestionstationskii.entities.Subscription;
import com.example.gestionstationskii.entities.TypeSubscription;
import com.example.gestionstationskii.repositories.ISkierRepository;
import com.example.gestionstationskii.repositories.ISubscriptionRepository;
import com.example.gestionstationskii.services.SubscriptionServicesImpl;
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
public class SubscriptionServicesImplMockTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription sampleSubscription;

    @BeforeEach
    void setUp() {
        sampleSubscription = new Subscription();
        sampleSubscription.setNumSub(1L);
        sampleSubscription.setTypeSub(TypeSubscription.ANNUAL);
        sampleSubscription.setStartDate(LocalDate.of(2025, 1, 1));
        sampleSubscription.setPrice(500.0f);
    }

    @Test
    void testAddSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(sampleSubscription);

        Subscription saved = subscriptionServices.addSubscription(sampleSubscription);

        assertNotNull(saved);
        assertEquals(LocalDate.of(2026, 1, 1), saved.getEndDate());
        verify(subscriptionRepository, times(1)).save(sampleSubscription);
    }

    @Test
    void testUpdateSubscription() {
        sampleSubscription.setPrice(600.0f);
        when(subscriptionRepository.save(sampleSubscription)).thenReturn(sampleSubscription);

        Subscription updated = subscriptionServices.updateSubscription(sampleSubscription);

        assertNotNull(updated);
        assertEquals(600.0f, updated.getPrice());
        verify(subscriptionRepository, times(1)).save(sampleSubscription);
    }

    @Test
    void testRetrieveSubscriptionById() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(sampleSubscription));

        Subscription found = subscriptionServices.retrieveSubscriptionById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getNumSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSubscriptionByType() {
        Set<Subscription> set = new HashSet<>(Collections.singletonList(sampleSubscription));
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL)).thenReturn(set);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL);
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(start, end))
                .thenReturn(Collections.singletonList(sampleSubscription));

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(start, end);
    }

    @Test
    void testRetrieveSubscriptions() {
        sampleSubscription.setEndDate(LocalDate.of(2026, 1, 1));
        when(subscriptionRepository.findDistinctOrderByEndDateAsc())
                .thenReturn(Collections.singletonList(sampleSubscription));

        Skier skier = new Skier();
        skier.setFirstName("Paul");
        skier.setLastName("Bernard");
        when(skierRepository.findBySubscription(sampleSubscription)).thenReturn(skier);

        assertDoesNotThrow(() -> subscriptionServices.retrieveSubscriptions());

        verify(subscriptionRepository, times(1)).findDistinctOrderByEndDateAsc();
        verify(skierRepository, times(1)).findBySubscription(sampleSubscription);
    }

    @Test
    void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(600.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(1200.0f);

        assertDoesNotThrow(() -> subscriptionServices.showMonthlyRecurringRevenue());

        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }
}
