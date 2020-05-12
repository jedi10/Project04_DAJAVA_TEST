package com.parkit.parkingsystem;

import com.parkit.parkingsystem.model.RecurringVehicule;
import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;

import com.parkit.parkingsystem.service.RecurringVehiculeService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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

    @Mock
    private static IRecurringVehiculeDAO recurringVehiculeDAO;

    @BeforeAll
    private static void setUp() {

    }

    @BeforeEach
    private void setUpPerTest() {
        //GIVEN
        String vehRegNumber = "ABCDEFG";
        List<RecurringVehicule> listRecurringVehicule = new ArrayList<>();
        listRecurringVehicule.add(new RecurringVehicule("MPLOKIJ", Date.from(now())));
        recurringVehicule = new RecurringVehicule(vehRegNumber, Date.from(now()));
        listRecurringVehicule.add(recurringVehicule);
        when(recurringVehiculeDAO.getListOfVehiculeRegNumber()).thenReturn(listRecurringVehicule);
        when(recurringVehiculeDAO.addVehiculeRegNumber(recurringVehicule)).thenReturn(true);
        when(recurringVehiculeDAO.updateVehiculeRegNumber(recurringVehicule)).thenReturn(true);
        recurringVehiculeService = new RecurringVehiculeService(recurringVehiculeDAO);
    }

    //a car leave parking. Price is calculating. Want to check if discount available.
    @DisplayName("Check Recurring Vehicule")
    @Order(1)
    @Test
    public void checkRecurringVehicule(){

        //WHEN
        //give vehiculeRegNumber to a class service which return true if present or false if not
        boolean vehiculeIsRecurring = recurringVehiculeService.checkRecurringVehicule(recurringVehicule);

        //THEN
        //assert presence return true
        assertTrue(vehiculeIsRecurring, "Vehicule is not recurring. Should be.");
    }

    @DisplayName("Add Recurring Vehicule")
    @Order(2)
    @Test
    public void addRecurringVehicule(){

        //WHEN
        //give vehiculeRegNumber to a class service which can save it in Model Class vehiculeRegList
        boolean vehiculeAddIsOK = recurringVehiculeService.addRecurringVehicule(recurringVehicule);

        //THEN
        //assert vehicule is reccorded
        assertTrue(vehiculeAddIsOK, "Vehicule save is OK");

    }

    @DisplayName("Update Recurring Vehicule")
    @Order(3)
    @Test
    public void updateRecurringVehicule(){

        //WHEN
        //give vehiculeRegNumber to a class service which can save it in Model Class vehiculeRegList
        boolean vehiculeUpdateIsOK = recurringVehiculeService.updateRecurringVehicule(recurringVehicule);

        //THEN
        //assert vehicule is reccorded
        assertTrue(vehiculeUpdateIsOK, "Vehicule update is OK");
    }
}
