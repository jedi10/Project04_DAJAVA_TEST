package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Ticket;

/**
 * <b>All needed to calcul price for parking a vehicle</b>
 */
public interface IFareCalculatorService {
    /**
     * <b>Setter for Recurring Vehicle Service</b>
     * <p>Useful for Test to inject RecurringVehiculeService Mock</p>
     * @param recurringVehiculeService  Service needed to check recurring Vehicle
     */
    void setRecurringVehiculeService(IRecurringVehiculeService recurringVehiculeService);

    /**
     * <b>Calcul of the parking price</b>
     * <ul>
     *     <li>First 29 minutes are free; </li>
     *     <li>Car and Bike have different price; </li>
     *     <li>Recurrent Vehicle have a discount</li>
     * </ul>
     * @param ticket a parking ticket
     */
    void calculateFare(Ticket ticket);
}
