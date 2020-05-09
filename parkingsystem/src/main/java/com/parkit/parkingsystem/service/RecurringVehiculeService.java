package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;
import com.parkit.parkingsystem.model.RecurringVehicule;

import java.util.List;

public class RecurringVehiculeService {

    private IRecurringVehiculeDAO recurringVehiculeDAO;

    public RecurringVehiculeService(IRecurringVehiculeDAO recurringVehiculeDAO) {
        this.recurringVehiculeDAO = recurringVehiculeDAO;
    }

    public boolean checkRecurringVehicule(RecurringVehicule vehiculeToCheck) {
        boolean result = false;
        List<RecurringVehicule> recurringvehiculeList =  recurringVehiculeDAO.getListOfVehiculeRegNumber();
        for (RecurringVehicule each :  recurringvehiculeList ){
            if (each.equals(vehiculeToCheck)){
                result = true;
            };
        }
        return result;
    }

    public boolean addRecurringVehicule(RecurringVehicule recurringVehicule) {
        boolean result = false;
        result = recurringVehiculeDAO.addVehiculeRegNumber(recurringVehicule);
        return result;
    }

    public boolean updateRecurringVehicule(RecurringVehicule recurringVehicule) {
        boolean result = false;
        result = recurringVehiculeDAO.updateVehiculeRegNumber(recurringVehicule);
        return result;
    }
}
