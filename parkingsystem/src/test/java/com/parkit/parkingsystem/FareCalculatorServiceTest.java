package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.RecurringVehicule;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.IRecurringVehiculeService;
import com.parkit.parkingsystem.service.RecurringVehiculeService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Date;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private RecurringVehicule recurringVehicule;

    @Mock
    private IRecurringVehiculeService recurringVehiculeService;

    @BeforeAll
    private void setUp() {

    }

    @BeforeEach
    private void setUpPerTest() {
        //GIVEN
        ticket = new Ticket();
        fareCalculatorService = new FareCalculatorService();
        recurringVehicule = new RecurringVehicule("ABCDEFG", now());
        when(recurringVehiculeService.applyDiscount("ABCDEFG")).thenReturn(true);
        //Give the mock to the FareCalculatorService
        fareCalculatorService.setRecurringVehiculeService(recurringVehiculeService);
    }

    @DisplayName("Calculate Car Fare When Parking Time equal or greater than 60 Minutes")
    @Order(5)
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
    @Order(6)
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

    @Order(9)
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

    @Order(10)
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
    @Order(4)
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
    @Order(3)
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

    @DisplayName("Calculate Car Fare When Parking Time equal or greater than 1 Day")
    @Order(7)
    @ParameterizedTest(name = "For {0} Day(s) Car Parking Time")
    @CsvSource({"1, 24", "2, 48", "3, 72" })
    public void calculateFareCarWithMoreThanADayParkingTime(int dayTime, double priceRatio){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ((24 * dayTime) * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(priceRatio * Fare.CAR_RATE_PER_HOUR ,
                ticket.getPrice(), "Ticket Price is bad");
    }

    @DisplayName("Calculate Bike Fare When Parking Time equal or greater than 1 Day")
    @Order(8)
    @ParameterizedTest(name = "For {0} Day(s) Bike Parking Time")
    @CsvSource({"1, 24", "2, 48", "3, 72" })
    public void calculateFareBikeWithMoreThanADayParkingTime(int dayTime, double priceRatio){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ((24 * dayTime) * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(priceRatio * Fare.BIKE_RATE_PER_HOUR,
                ticket.getPrice(), "Ticket Price is bad");
    }

    @DisplayName("free Car Fare When Short Parking Time")
    @Order(1)
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

    @DisplayName("free Bike Fare When Short Parking Time")
    @Order(2)
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


    @DisplayName("Calculate Car Fare When Discount for Recurring")
    @Test
    @Order(11)
    public void calculateFareCarWithDiscountRecurringVehicule(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setVehicleRegNumber("ABCDEFG");
        ticket.setParkingSpot(parkingSpot);

        //WHEN
        fareCalculatorService.calculateFare(ticket);

        //THEN
        assertEquals( Math.round(Fare.RECURRENT_DISCOUNT * Fare.CAR_RATE_PER_HOUR * 100.00)/100.00,
                ticket.getPrice(), "Ticket Price is bad");
    }


    @DisplayName("Calculate Bike Fare When Discount for Recurring")
    @Test
    @Order(12)
    public void calculateFareBikeWithDiscountRecurringVehicule(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setVehicleRegNumber("ABCDEFG");
        ticket.setParkingSpot(parkingSpot);

        //WHEN
        fareCalculatorService.calculateFare(ticket);

        //THEN
        assertEquals( Math.round(Fare.RECURRENT_DISCOUNT * Fare.BIKE_RATE_PER_HOUR * 100.00)/100.00,
                ticket.getPrice(), "Ticket Price is bad");
    }

}
