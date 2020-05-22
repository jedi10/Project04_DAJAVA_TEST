package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Ticket;

public interface IFareCalculatorService {
    void setRecurringVehiculeService(IRecurringVehiculeService recurringVehiculeService);

    void calculateFare(Ticket ticket);
}
