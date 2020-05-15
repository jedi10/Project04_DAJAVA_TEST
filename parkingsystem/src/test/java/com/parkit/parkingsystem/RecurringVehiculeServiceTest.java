package com.parkit.parkingsystem;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RecurringVehiculeServiceTest {
    private static RecurringVehiculeService recurringVehiculeService;
    private static RecurringVehicule recurringVehicule;
    private static String vehRegNumber = "ABCDEFG";

    @Mock
    private static IRecurringVehiculeDAO recurringVehiculeDAO;

    @BeforeAll
    private static void setUp() {

    }

    @BeforeEach
    private void setUpPerTest() {
        //GIVEN
        recurringVehicule = new RecurringVehicule(vehRegNumber, now());
        when(recurringVehiculeDAO.getRecurringVehicule(vehRegNumber)).thenReturn(recurringVehicule);
        //when(recurringVehiculeDAO.getListOfRecurrentVehicule()).thenReturn(listRecurringVehicule);
        when(recurringVehiculeDAO.addRecurrentVehicule(recurringVehicule)).thenReturn(new RecurringVehicule("ABCDEF", now()));
        when(recurringVehiculeDAO.updateRecurrentVehicule(recurringVehicule)).thenReturn(1);
        recurringVehiculeService = new RecurringVehiculeService(recurringVehiculeDAO);
    }

    //a car leave parking. Price is calculating. Want to check if discount available.
    @DisplayName("Check Recurring Vehicule")
    @Order(1)
    @Test
    public void checkRecurringVehicule(){

        //WHEN
        //give vehiculeRegNumber to a class service which return true if present or false if not
        RecurringVehicule vehiculeIsRecurring = recurringVehiculeService.checkRecurringVehicule(vehRegNumber);

        //THEN
        assertNotNull(vehiculeIsRecurring,
                "Vehicule is not recurring: checkRecurringVehicule method doesn't work");
        assertEquals(recurringVehicule, vehiculeIsRecurring,
                "Vehicule is not recurring: checkRecurringVehicule method doesn't work");
        verify(recurringVehiculeDAO, Mockito.times(1)).getRecurringVehicule(any(String.class));
    }

    @DisplayName("Add Recurring Vehicule")
    @Order(2)
    @Test
    public void addRecurringVehicule(){

        //WHEN
        //give vehiculeRegNumber to a class service which can save it in Model Class vehiculeRegList
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
        //give vehiculeRegNumber to a class service which can save it in Model Class vehiculeRegList
        int vehiculeUpdate = recurringVehiculeService.updateRecurringVehicule(recurringVehicule);

        //THEN
        assertEquals(1, vehiculeUpdate, "Vehicule is not update in DBB (Stub)");
        verify(recurringVehiculeDAO, Mockito.times(1))
                .updateRecurrentVehicule(any(RecurringVehicule.class));
    }
}
