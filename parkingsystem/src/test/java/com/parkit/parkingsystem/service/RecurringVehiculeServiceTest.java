package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.RecurringVehicule;
import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;

import com.parkit.parkingsystem.service.RecurringVehiculeService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.time.Instant.now;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A vehicle leaves parking. Price is calculating. Want to check if discount available.
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RecurringVehiculeServiceTest {
    private static RecurringVehiculeService recurringVehiculeService;
    private static RecurringVehicule recurringVehicule;
    private static String recurrentVehRegNumber = "ABCDEFG";
    private static String notRecurrentVehRegNumber = "MPLOKIJ";

    @Mock
    private static IRecurringVehiculeDAO recurringVehiculeDAO;

    @BeforeAll
    private static void setUp() {

    }

    @BeforeEach
    private void setUpPerTest() {
        //GIVEN
        recurringVehicule = new RecurringVehicule(recurrentVehRegNumber, now());
        when(recurringVehiculeDAO.getRecurringVehicule(recurrentVehRegNumber)).thenReturn(recurringVehicule);
        when(recurringVehiculeDAO.getRecurringVehicule(notRecurrentVehRegNumber)).thenReturn(null);
        //when(recurringVehiculeDAO.getListOfRecurrentVehicule()).thenReturn(listRecurringVehicule);
        when(recurringVehiculeDAO.addRecurrentVehicule(recurringVehicule)).thenReturn(new RecurringVehicule("ABCDEF", now()));
        when(recurringVehiculeDAO.updateRecurrentVehicule(recurringVehicule)).thenReturn(1);
        recurringVehiculeService = new RecurringVehiculeService(recurringVehiculeDAO);
    }


    @DisplayName("Check Recurring Vehicule")
    @Order(1)
    @Test
    public void checkRecurringVehicule(){

        //WHEN
        //give vehiculeRegNumber to a class service which return Object if present or null if not
        RecurringVehicule vehiculeIsRecurring = recurringVehiculeService.checkRecurringVehicule(recurrentVehRegNumber);
        RecurringVehicule vehiculeNotRecurring = recurringVehiculeService.checkRecurringVehicule(notRecurrentVehRegNumber);

        //THEN
        assertNull(vehiculeNotRecurring,
                "Vehicule is not recurring: checkRecurringVehicule should return null");
        assertNotNull(vehiculeIsRecurring,
                "Vehicule is recurring: checkRecurringVehicule method should return an object");
        assertEquals(recurringVehicule, vehiculeIsRecurring,
                "Vehicule is recurring: checkRecurringVehicule method should return a recurring vehicle");
        verify(recurringVehiculeDAO, Mockito.times(2)).getRecurringVehicule(any(String.class));
    }

    @DisplayName("Add Recurring Vehicule")
    @Order(2)
    @Test
    public void addRecurringVehicule(){

        //WHEN
        RecurringVehicule vehiculeAdded = recurringVehiculeService.addRecurringVehicule(recurringVehicule);

        //THEN
        assertNotNull(vehiculeAdded, "Vehicule is not recorded in DBB (Stub)");
        verify(recurringVehiculeDAO, Mockito.times(1))
                .addRecurrentVehicule(any(RecurringVehicule.class));
    }

    @DisplayName("Update Recurring Vehicule")
    @Order(3)
    @Test
    public void updateRecurringVehicule(){

        //WHEN
        int vehiculeUpdate = recurringVehiculeService.updateRecurringVehicule(recurringVehicule);

        //THEN
        assertEquals(1, vehiculeUpdate, "Vehicule is not update in DBB (Stub)");
        verify(recurringVehiculeDAO, Mockito.times(1))
                .updateRecurrentVehicule(any(RecurringVehicule.class));
    }

    @DisplayName("Apply Discount for Recurring vehicle")
    @Order(4)
    @Test
    public void applyDiscount(){

        //WHEN
        boolean discount = recurringVehiculeService.applyDiscount(recurrentVehRegNumber);
        boolean nodiscount = recurringVehiculeService.applyDiscount(notRecurrentVehRegNumber);

        //THEN
        assertFalse(nodiscount, "Vehicle shouldn't have a discount");
        verify(recurringVehiculeDAO, Mockito.times(1))
                .addRecurrentVehicule(any(RecurringVehicule.class));
        assertTrue(discount, "Vehicle should have a discount");
        verify(recurringVehiculeDAO, Mockito.times(1))
                .updateRecurrentVehicule(any(RecurringVehicule.class));
    }


}
