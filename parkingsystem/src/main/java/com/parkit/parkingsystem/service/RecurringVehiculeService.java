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
        List<RecurringVehicule> recurringvehiculeList =  recurringVehiculeDAO.getListOfRecurrentVehicule();
        for (RecurringVehicule each :  recurringvehiculeList ){
            if (each.equals(vehiculeToCheck)){
                result = true;
            };
        }
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
