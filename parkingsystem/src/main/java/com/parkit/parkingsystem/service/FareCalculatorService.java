package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.Instant;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Instant inInstant = ticket.getInTime().toInstant();
        Instant outInstant = ticket.getOutTime().toInstant();

        Duration duration = Duration.between(inInstant, outInstant);
        long durationMinute = duration.toMinutes();

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationMinute * (Fare.CAR_RATE_PER_HOUR/60));
                break;
            }
            case BIKE: {
                ticket.setPrice(durationMinute * (Fare.BIKE_RATE_PER_HOUR/60));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}