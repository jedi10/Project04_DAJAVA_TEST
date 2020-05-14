package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.RecurringVehiculeDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.RecurringVehicule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecurringVehiculeDAOTest {

    private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private RecurringVehiculeDAO recurringVehiculeDAO;
    private DataBasePrepareService dataBasePrepareService;
    private String vehiculeRegNumber = "MPLOKIJ";
    private RecurringVehicule recurringVehiculeTest;

    @BeforeAll
    private void setUp() throws Exception{
        recurringVehiculeDAO = new RecurringVehiculeDAO();
        recurringVehiculeDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

        Date lastVisit = Date.from(now().minus(Duration.ofHours(1)));
        recurringVehiculeTest = new RecurringVehicule( vehiculeRegNumber, lastVisit);
    }

    @AfterAll
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Order(1)
    @Test
    public void addRecurrentVehicule(){
        //GIVEN

        //WHEN
        RecurringVehicule lineCreation = recurringVehiculeDAO.addRecurrentVehicule(recurringVehiculeTest);

        //THEN
        assertNotNull(lineCreation, "recurring Vehicule can't be saved in DBB");
        assertNotEquals(recurringVehiculeTest.getId(), lineCreation.getId(), "recurring Vehicule ID hasn't been generated by DBB");
    }

    @Order(2)
    @Test
    public void getRecurrentVehicule(){
        //GIVEN

        //WHEN
        RecurringVehicule recurringVehiculeDBB = recurringVehiculeDAO.getRecurringVehicule(vehiculeRegNumber);
        RecurringVehicule recurringVehiculeDBB2 = recurringVehiculeDAO.getRecurringVehicule("ABCDEF");

        //THEN
        assertNotNull(recurringVehiculeDBB, "DBB doesn't return Recurring Véhicule");
        assertEquals(recurringVehiculeTest, recurringVehiculeDBB, "Test Recurring Vehicule mismatch with DBB Recurring Véhicule" );

        assertNull(recurringVehiculeDBB2, "DAO doesn't return Null when Recurring Vehicule not in DBB");

    }

    @Order(3)
    @Test
    public void getListOfRecurrentVehicule(){
        //GIVEN
        RecurringVehicule recurringVehiculeTest2 = new RecurringVehicule("ABCDEF", Date.from(now().minus(Duration.ofHours(1))));
        RecurringVehicule lineCreation = recurringVehiculeDAO.addRecurrentVehicule(recurringVehiculeTest2);

        //WHEN
        List<RecurringVehicule> recurringVehiculeList = recurringVehiculeDAO.getListOfRecurrentVehicule();

        //THEN
        assertNotNull(recurringVehiculeList, "List of Recurring Vehicule is Null");
        assertEquals(2, recurringVehiculeList.size(), "List doesn't have 2 Recurring Vehicule");
        assertEquals(recurringVehiculeTest, recurringVehiculeList.get(0), "First Element of Recurrent Vehicule List mismatch");
        assertEquals(recurringVehiculeTest2, recurringVehiculeList.get(1), "Second Element of Recurrent Vehicule List mismatch");
    }


    @Order(4)
    @Test
    public void updateRecurrentVehicule(){
        //GIVEN
        RecurringVehicule recurringVehiculeDBB_before = recurringVehiculeDAO.getRecurringVehicule(vehiculeRegNumber);
        Date dateNew = Date.from(now());
        recurringVehiculeDBB_before.setLastVisit(dateNew);

        //WHEN
        int updateRecurrentVehicle = recurringVehiculeDAO.updateRecurrentVehicule(recurringVehiculeDBB_before);
        RecurringVehicule recurringVehiculeDBB_after = recurringVehiculeDAO.getRecurringVehicule(vehiculeRegNumber);

        //THEN
        assertEquals(1, updateRecurrentVehicle, "Update has not been executed properly");
        assertNotNull(recurringVehiculeDBB_before, "DBB doesn't return Recurring Vehicule");
        assertNotNull(recurringVehiculeDBB_after, "DBB doesn't return Recurring Vehicule");
        assertTrue(recurringVehiculeDBB_before.getLastVisit().equals(recurringVehiculeDBB_after.getLastVisit()),
                "DBB Recurrent Vehicle Date isn't updated in DBB");
    }
}
