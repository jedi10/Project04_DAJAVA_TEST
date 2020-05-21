package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;
import com.parkit.parkingsystem.dao.RecurringVehiculeDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.RecurringVehicule;
import com.parkit.parkingsystem.service.RecurringVehiculeService;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecurringVehiculeServiceIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;
    private static RecurringVehiculeService recurringVehiculeService;
    private static RecurringVehicule recurringVehicule;
    private static String recurrentVehRegNumber = "ABCDEFG";
    private static String notRecurrentVehRegNumber = "MPLOKIJ";

    private static IRecurringVehiculeDAO recurringVehiculeDAO;

    @BeforeAll
    private static void setUp() {
        recurringVehiculeDAO = new RecurringVehiculeDAO();
        recurringVehiculeDAO.setDataBaseConfig(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterAll
    private static void wipeOut() {
        recurringVehiculeDAO = null;
        recurringVehiculeDAO = null;
        dataBasePrepareService.clearDataBaseEntries();
    }

    @BeforeEach
    private void setUpPerTest() {
        //GIVEN
        recurringVehicule = new RecurringVehicule(recurrentVehRegNumber, now());
        recurringVehiculeService = new RecurringVehiculeService(recurringVehiculeDAO);
    }

    @DisplayName("Add Recurring Vehicule")
    @Order(1)
    @Test
    public void addRecurringVehicule() {

        //WHEN
        RecurringVehicule vehiculeAdded = recurringVehiculeService.addRecurringVehicule(recurringVehicule);

        //THEN
        assertNotNull(vehiculeAdded, "Vehicule is not recorded in DBB");
        assertNotNull(vehiculeAdded.getId(), "Vehicule is not created properly in DBB");
        assertTrue(vehiculeAdded.getId() > 0, "Vehicule is not created properly in DBB");
        assertEquals(recurringVehicule, vehiculeAdded,
                "Vehicule in DBB is not the one given");
    }

    @DisplayName("Check Recurring Vehicule")
    @Order(2)
    @Test
    public void checkRecurringVehicule() {

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
    }


    @DisplayName("Update Recurring Vehicule")
    @Order(3)
    @Test
    public void updateRecurringVehicule() {

        //WHEN
        RecurringVehicule vehiculeInDBB = recurringVehiculeService.checkRecurringVehicule(recurrentVehRegNumber);
        Instant instant = now().minus(Duration.ofHours(1));
        vehiculeInDBB.setLastVisit(instant);
        int vehiculeUpdate = recurringVehiculeService.updateRecurringVehicule(vehiculeInDBB);
        RecurringVehicule vehiculeInDBBAfterUpdate = recurringVehiculeService.checkRecurringVehicule(recurrentVehRegNumber);


        //THEN
        assertEquals(1, vehiculeUpdate, "Vehicule is not update in DBB");
        assertEquals(vehiculeInDBB.getLastVisit().truncatedTo(ChronoUnit.SECONDS),
                vehiculeInDBBAfterUpdate.getLastVisit().truncatedTo(ChronoUnit.SECONDS),
                "Date has not been updated");
    }

    @DisplayName("Apply Discount for Recurring vehicle")
    @Order(4)
    @Test
    public void applyDiscount() {

        //WHEN
        boolean discount = recurringVehiculeService.applyDiscount(recurrentVehRegNumber);
        boolean nodiscount = recurringVehiculeService.applyDiscount(notRecurrentVehRegNumber);

        //THEN
        assertFalse(nodiscount, "Vehicle shouldn't have a discount");
        assertTrue(discount, "Vehicle should have a discount");

    }
}
