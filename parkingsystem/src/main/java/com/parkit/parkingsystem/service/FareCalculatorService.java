package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.Instant;

import static com.parkit.parkingsystem.constants.Fare.RECURRENT_DISCOUNT;

public class FareCalculatorService implements IFareCalculatorService {

    private IRecurringVehiculeService recurringVehiculeService;

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
        }
    }

    private double recurrentDiscount(double price, String vehicleRegNumber){
        if(recurringVehiculeService.applyDiscount(vehicleRegNumber)){
            price = price * Fare.RECURRENT_DISCOUNT;
        }
        return price;
    }
}