package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @DisplayName("Calculate Car Fare When Parking Time equal or greater than 60 Minutes")
    @ParameterizedTest(name = "For {0} Minute(s) Car Parking Time")
    @CsvSource({"60, 1",  "90, 1.5", "120, 2" })
    public void calculateFareCar(int minuteTime, double priceRatio){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  minuteTime * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(priceRatio * Fare.CAR_RATE_PER_HOUR,
                ticket.getPrice(), "Ticket Price is bad");
    }

    @DisplayName("Calculate Bike Fare When Parking Time equal or greater than 60 Minutes")
    @ParameterizedTest(name = "For {0} Minute(s) Bike Parking Time")
    @CsvSource({"60, 1",  "90, 1.5", "120, 2" })
    public void calculateFareBike(int minuteTime, double priceRatio){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  minuteTime * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( priceRatio * Fare.BIKE_RATE_PER_HOUR,
                ticket.getPrice(), "Ticket Price is bad");
    }

    @Test
    public void calculateFareUnknownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @DisplayName("Calculate Bike Fare When Parking Time between 30-60 Minutes")
    @ParameterizedTest(name = "For {0} Minute(s) Bike Parking Time, Price is not 0")
    @CsvSource({"30, 0.5",  "45, 0.75", "54, 0.9" })
    public void calculateFareBikeWithLessThanOneHourParkingTime(int minuteTime, double priceRatio){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( minuteTime  * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertNotEquals(0, ticket.getPrice());
        assertEquals( priceRatio * Fare.BIKE_RATE_PER_HOUR,
                ticket.getPrice(), "Ticket Price is bad");
    }

    @DisplayName("Calculate Car Fare When Parking Time between 30-60 Minutes")
    @ParameterizedTest(name = "For {0} Minute(s) Car Parking Time, Price is not 0")
    @CsvSource({"30, 0.5",  "45, 0.75", "54, 0.9" })
    public void calculateFareCarWithLessThanOneHourParkingTime(int minuteTime, double priceRatio){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  minuteTime * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertNotEquals(0, ticket.getPrice());
        assertEquals( priceRatio * Fare.CAR_RATE_PER_HOUR,
                ticket.getPrice(), "Ticket Price is bad");
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @DisplayName("free Fare Car When Short Parking Time")
    @ParameterizedTest(name = "For {0} Minute(s) Car Parking Time, Price should be 0")
    @ValueSource(ints = { 1, 15, 29 })
    public void calculateFareCarWithLessThan30MinParkingTime(int arg){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (arg * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice(), "Ticket Price is bad");
    }

    @DisplayName("free Fare Bike When Short Parking Time")
    @ParameterizedTest(name = "For {0} Minute(s) Bike Parking Time, Price should be 0")
    @ValueSource(ints = { 1, 15, 29 })
    public void calculateFareBikeWithLessThan30MinParkingTime(int arg){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (arg * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice(), "Ticket Price is bad");
    }
}
