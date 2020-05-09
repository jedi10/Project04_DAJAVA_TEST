package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;
import com.parkit.parkingsystem.model.RecurringVehicule;

public class RecurringVehiculeService {

    private IRecurringVehiculeDAO recurringVehiculeDAO;

    public RecurringVehiculeService(IRecurringVehiculeDAO recurringVehiculeDAO) {
        this.recurringVehiculeDAO = recurringVehiculeDAO;
    }

    public boolean checkRecurringVehicule(RecurringVehicule vehiculeToCheck) {
        return false;
    }

    public boolean addRecurringVehicule(RecurringVehicule recurringVehicule) {
        return false;
    }

    public boolean updateRecurringVehicule(RecurringVehicule recurringVehicule) {
        return false;
    }
}
