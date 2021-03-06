package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.AppType;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;
import com.parkit.parkingsystem.dao.RecurringVehiculeDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.Instant;

import static com.parkit.parkingsystem.constants.Fare.RECURRENT_DISCOUNT;

/**
 * <b>Simple brute force implementation of Interface IFareCalculatorService</b>
 *  @see IFareCalculatorService
 *  @author Jedy10
 *  @since beta1.0.0
 */
public class FareCalculatorService implements IFareCalculatorService {

    /**
     * @see FareCalculatorService#setRecurringVehiculeService(IRecurringVehiculeService)
     */
    private IRecurringVehiculeService recurringVehiculeService;// new RecurringVehiculeService(new RecurringVehiculeDAO());

    /**
     * Constructor for Test
      */
    public FareCalculatorService() {

    }

    /**
     * Constructor
     * @param recurringVehiculeDAO resource DAO needed to access recurring Vehicle DBB
     */
    public FareCalculatorService(IRecurringVehiculeDAO recurringVehiculeDAO) {
        this.recurringVehiculeService = new RecurringVehiculeService(recurringVehiculeDAO);
    }

    /**
     * <b>Setter for Recurring Vehicle Service</b>
     * <p>Useful for Test to inject RecurringVehiculeService Mock</p>
     * @param recurringVehiculeService  Service needed to check recurring Vehicle
     */
    @Override
    public void setRecurringVehiculeService(IRecurringVehiculeService recurringVehiculeService) {
        this.recurringVehiculeService = recurringVehiculeService;
    }

    @Override
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Instant inInstant = ticket.getInTime().toInstant();
        Instant outInstant = ticket.getOutTime().toInstant();

        Duration duration = Duration.between(inInstant, outInstant);
        long durationMinute = duration.toMinutes();

        if (durationMinute > Fare.FREE_PERIOD_TIME_IN_MINUTES){
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    double price = durationMinute * (Fare.CAR_RATE_PER_HOUR/60);
                    price = recurrentDiscount(price, ticket.getVehicleRegNumber());
                    ticket.setPrice(price);
                    break;
                }
                case BIKE: {
                    double price = durationMinute * (Fare.BIKE_RATE_PER_HOUR/60);
                    price = recurrentDiscount(price, ticket.getVehicleRegNumber());
                    ticket.setPrice(price);
                    break;
                }
                default: throw new IllegalArgumentException("Unknown Parking Type");
            }
        } else {
            ticket.setPrice(0);
            if (AppType.PRESENTATION) {
                //Force Record of vehicle in recurrent vehicle table
                recurrentDiscount(0, ticket.getVehicleRegNumber());
            }
        }
    }

    /**
     * <b>needed to know if a vehicle is recurrent and can have a discount</b>
     * <ul>
     *     <li>return same price if vehicle is not recurrent</li>
     *     <li>return discounted price if vehicle is recurrent</li>
     * </ul>
     * @param price price for the vehicle calcul on elapsed time in parking
     * @param vehicleRegNumber vehicle number
     * @return price discounted if recurrent vehicle
     */
    private double recurrentDiscount(double price, String vehicleRegNumber){
        if(recurringVehiculeService.applyDiscount(vehicleRegNumber)){
            double promoScreenNumber = Math.round(price * (1-Fare.RECURRENT_DISCOUNT) * 100.00)/100.00;
            System.out.println("You get a DISCOUNT of "+ promoScreenNumber);
            price = Math.round(price * Fare.RECURRENT_DISCOUNT * 100.00)/100.00;
        }
        return price;
    }
}