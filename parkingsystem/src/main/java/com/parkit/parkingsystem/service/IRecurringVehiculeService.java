package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.RecurringVehicule;

/**
 * <b>When it is needed to deal with recurrent vehicle</b>
 * <ul>
 *     <li>recurrent vehicle can be stored; </li>
 *     <li>it is possible to know if a vehicle is recurrent or not</li>
 * </ul>
 */
public interface IRecurringVehiculeService {

    /**
     * <b>Is a vehicle recurrent  or not</b>
     * <ul>
     *     <li>if not recurrent, is stored in DBB, return false; </li>
     *     <li>if recurrent, DBB is updated (last_visit), return true</li>
     * </ul>
     * @param vehicleRegNumber Number of the vehicle
     * @return boolean (false if vehicle is not recurrent)
     */
    boolean applyDiscount(String vehicleRegNumber);

    /**
     * <b>Get recurrent vehicle</b>
     * @param vehicleRegNumber Number of the vehicle
     * @return RecurringVehicule or null
     */
    RecurringVehicule checkRecurringVehicule(String vehicleRegNumber);

    /**
     * <b>Add Recurring Vehicle</b>
     * @param recurringVehicule Recurrent Vehicle to save
     * @return RecurringVehicule with id or null
     */
    RecurringVehicule addRecurringVehicule(RecurringVehicule recurringVehicule);

    /**
     * <b>Update recurrent vehicle</b>
     * <p>last_visit property can be updated</p>
     * @param recurringVehicule Recurrent Vehicle to update
     * @return Number of updated line
     */
    int updateRecurringVehicule(RecurringVehicule recurringVehicule);
}
