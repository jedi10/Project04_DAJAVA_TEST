package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;
import com.parkit.parkingsystem.model.RecurringVehicule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static java.time.Instant.now;

public class RecurringVehiculeService {

    private static final Logger logger = LogManager.getLogger("RecurringVehiculeService");

    private IRecurringVehiculeDAO recurringVehiculeDAO;

    public RecurringVehiculeService(IRecurringVehiculeDAO recurringVehiculeDAO) {
        this.recurringVehiculeDAO = recurringVehiculeDAO;
    }

    public boolean applyDiscount(String vehicleRegNumber){
        boolean result = false;
        RecurringVehicule recurringVehicule = checkRecurringVehicule(vehicleRegNumber);
        if (null != recurringVehicule){
            result = true;
            recurringVehicule.setLastVisit(now());
            updateRecurringVehicule(recurringVehicule);
        } else {
            result = false;
            RecurringVehicule recurringVehiculeToSaveInDBB = new RecurringVehicule(vehicleRegNumber, now());
            addRecurringVehicule(recurringVehiculeToSaveInDBB);
        }
        return result;
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
        if (null == result){
            logger.info("Creation of Recurring Vehicule in DBB failed"+ recurringVehicule.toString());
        }
        return result;
    }

    public int updateRecurringVehicule(RecurringVehicule recurringVehicule) {
        int result = 0;
        result = recurringVehiculeDAO.updateRecurrentVehicule(recurringVehicule);
        if (0 == result){
            logger.info("Update of Recurring Vehicule in DBB failed"+ recurringVehicule.toString());
        }
        return result;
    }
}
