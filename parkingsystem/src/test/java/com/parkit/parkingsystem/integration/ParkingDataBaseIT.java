package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
//import org.junit.Assert;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    private final static String vehiculeRegNumber = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehiculeRegNumber);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        //Get Ticket by vehicleRegNumber
        Ticket ticket = ticketDAO.getTicket(vehiculeRegNumber);
        assertNotNull(ticket,
                "Request don't return anything: no ticket");
        assertTrue(ticket instanceof Ticket,
                "Returned Object is not a Ticket ");
        assertEquals(vehiculeRegNumber, ticket.getVehicleRegNumber(),
                "Vehicule Registration Number is not the same");
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        assertNotNull(parkingSpot,
                "Ticket don't have any parking spot (null)");
        assertTrue(parkingSpot instanceof ParkingSpot,
                "Returned object is not a parking Spot");
        assertFalse(parkingSpot.isAvailable(),
                "Availability of parking spot has not been updated");
    }

    @Test
    public void testParkingLotExit(){
        //GIVEN
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //Create a Ticket with a proper InTime
        Ticket ticket = ticketDAO.getTicket(vehiculeRegNumber);
        assertNotNull(ticket,
                "Request don't return anything: no ticket");
        assertTrue(ticket instanceof Ticket,
                "Returned Object is not a Ticket ");

        ticket.setInTime(Date.from(Instant.now().minus(Duration.ofHours(1))));
        ticket.setOutTime(ticket.getInTime());
        boolean updateTicket = ticketDAO.updateTicket(ticket);
        assertTrue(updateTicket,"Tests can't go further because ticket Time is not updated");

        //WHEN
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database

        //THEN
        ticket = ticketDAO.getTicket(vehiculeRegNumber);
        assertNotNull(ticket,
                "Request don't return anything: no ticket");
        assertTrue(ticket instanceof Ticket,
                "Returned Object is not a Ticket ");
        assertNotNull(ticket.getPrice(),
                "Price is null");
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice(),
                "Price is not the proper one");

        assertNotNull(ticket.getOutTime(),
                "Out Time is null");
        Instant expectedOutTime  = ticket.getInTime().toInstant().plus(Duration.ofHours(1));
        assertEquals(expectedOutTime, ticket.getOutTime().toInstant(),
                "OutTime is not the expected one");

    }

}
