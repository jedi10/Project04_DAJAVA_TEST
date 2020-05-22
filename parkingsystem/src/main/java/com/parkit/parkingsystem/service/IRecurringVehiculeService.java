package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.RecurringVehicule;

public interface IRecurringVehiculeService {
    boolean applyDiscount(String vehicleRegNumber);

    RecurringVehicule checkRecurringVehicule(String vehicleRegNumber);

    RecurringVehicule addRecurringVehicule(RecurringVehicule recurringVehicule);

    int updateRecurringVehicule(RecurringVehicule recurringVehicule);
}
