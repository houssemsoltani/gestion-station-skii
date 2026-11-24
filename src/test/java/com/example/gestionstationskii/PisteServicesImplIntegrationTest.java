package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Color;
import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.services.IPisteServices;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PisteServicesImplIntegrationTest {

    @Autowired
    private IPisteServices pisteServices;

    private static Long createdPisteId;

    @Test
    @Order(1)
    void testAddPiste() {
        Piste piste = new Piste();
        piste.setNamePiste("Piste Rouge Test");
        piste.setColor(Color.RED);
        piste.setLength(2500);
        piste.setSlope(30);

        Piste saved = pisteServices.addPiste(piste);
        assertNotNull(saved);
        assertNotNull(saved.getNumPiste());
        assertEquals("Piste Rouge Test", saved.getNamePiste());
        createdPisteId = saved.getNumPiste();
    }

    @Test
    @Order(2)
    void testRetrievePiste() {
        assertNotNull(createdPisteId);
        Piste found = pisteServices.retrievePiste(createdPisteId);
        assertNotNull(found);
        assertEquals("Piste Rouge Test", found.getNamePiste());
        assertEquals(Color.RED, found.getColor());
    }

    @Test
    @Order(3)
    void testRetrieveAllPistes() {
        List<Piste> pistes = pisteServices.retrieveAllPistes();
        assertNotNull(pistes);
        assertFalse(pistes.isEmpty());
    }

    @Test
    @Order(4)
    void testRemovePiste() {
        assertNotNull(createdPisteId);
        pisteServices.removePiste(createdPisteId);
        Piste deleted = pisteServices.retrievePiste(createdPisteId);
        assertNull(deleted);
    }
}
