package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;
import com.parkit.parkingsystem.model.RecurringVehicule;

import java.util.List;

public class RecurringVehiculeService {

    private IRecurringVehiculeDAO recurringVehiculeDAO;

    public RecurringVehiculeService(IRecurringVehiculeDAO recurringVehiculeDAO) {
        this.recurringVehiculeDAO = recurringVehiculeDAO;
    }

    public RecurringVehicule checkRecurringVehicule(String vehicleRegNumber) {
        RecurringVehicule result = null;

        result =  recurringVehiculeDAO
                .getRecurringVehicule(vehicleRegNumber);

        return result;
    }

    public RecurringVehicule addRecurringVehicule(RecurringVehicule recurringVehicule) {
        RecurringVehicule result = null;
        result = recurringVehiculeDAO.addRecurrentVehicule(recurringVehicule);
        return result;
    }

    public int updateRecurringVehicule(RecurringVehicule recurringVehicule) {
        int result = 0;
        result = recurringVehiculeDAO.updateRecurrentVehicule(recurringVehicule);
        return result;
    }
}
