package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Color;
import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.repositories.IPisteRepository;
import com.example.gestionstationskii.services.PisteServicesImpl;
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
public class PisteServicesImplMockTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste samplePiste;

    @BeforeEach
    void setUp() {
        samplePiste = new Piste();
        samplePiste.setNumPiste(1L);
        samplePiste.setNamePiste("Piste Verte");
        samplePiste.setColor(Color.GREEN);
        samplePiste.setLength(1200);
        samplePiste.setSlope(15);
    }

    @Test
    void testRetrieveAllPistes() {
        when(pisteRepository.findAll()).thenReturn(Arrays.asList(samplePiste));

        List<Piste> result = pisteServices.retrieveAllPistes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Piste Verte", result.get(0).getNamePiste());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        when(pisteRepository.save(samplePiste)).thenReturn(samplePiste);

        Piste created = pisteServices.addPiste(samplePiste);

        assertNotNull(created);
        assertEquals(1L, created.getNumPiste());
        verify(pisteRepository, times(1)).save(samplePiste);
    }

    @Test
    void testRemovePiste() {
        doNothing().when(pisteRepository).deleteById(1L);

        pisteServices.removePiste(1L);

        verify(pisteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRetrievePiste() {
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(samplePiste));

        Piste found = pisteServices.retrievePiste(1L);

        assertNotNull(found);
        assertEquals(1L, found.getNumPiste());
        verify(pisteRepository, times(1)).findById(1L);
    }
}
