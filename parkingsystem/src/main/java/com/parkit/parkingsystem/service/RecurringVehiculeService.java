package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.IRecurringVehiculeDAO;
import com.parkit.parkingsystem.model.RecurringVehicule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static java.time.Instant.now;

/**
 * <b>Simple brute force implementation of Interface IRecurringVehiculeService</b>
 * @see IRecurringVehiculeService
 * @author Jedy10
 * @since beta1.0.0
 */
public class RecurringVehiculeService implements IRecurringVehiculeService {

    private static final Logger logger = LogManager.getLogger("RecurringVehiculeService");

    /**
     * <b>Resource needed to the class methods</b>
     * <p>work with recurrentVehicle Table in DBB</p>
     */
    private IRecurringVehiculeDAO recurringVehiculeDAO;

    /**
     * <b>Constructor</b>
     * @param recurringVehiculeDAO resource needed to the class
     */
    public RecurringVehiculeService(IRecurringVehiculeDAO recurringVehiculeDAO) {
        this.recurringVehiculeDAO = recurringVehiculeDAO;
    }

    @Override
    public boolean applyDiscount(String vehicleRegNumber){
        boolean result = false;
        RecurringVehicule recurringVehicule = checkRecurringVehicule(vehicleRegNumber);
        //if vehicle is already in DBB
        if (null != recurringVehicule){
            result = true;
            //we update the date
            recurringVehicule.setLastVisit(now());
            updateRecurringVehicule(recurringVehicule);
        } else {
            //We create a new line in recurrent vehicle base
            RecurringVehicule recurringVehiculeToSaveInDBB = new RecurringVehicule(vehicleRegNumber, now());
            addRecurringVehicule(recurringVehiculeToSaveInDBB);
        }
        return result;
    }

    @Override
    public RecurringVehicule checkRecurringVehicule(String vehicleRegNumber) {
        RecurringVehicule result = null;

        result =  recurringVehiculeDAO
                .getRecurringVehicule(vehicleRegNumber);

        return result;
    }

    @Override
    public RecurringVehicule addRecurringVehicule(RecurringVehicule recurringVehicule) {
        RecurringVehicule result = null;
        result = recurringVehiculeDAO.addRecurrentVehicule(recurringVehicule);
        if (null == result){
            logger.error("Creation of Recurring Vehicule in DBB failed"+ recurringVehicule.toString());
        }
        return result;
    }

    @Override
    public int updateRecurringVehicule(RecurringVehicule recurringVehicule) {
        int result = 0;
        result = recurringVehiculeDAO.updateRecurrentVehicule(recurringVehicule);
        if (0 == result){
            logger.error("Update of Recurring Vehicule in DBB failed"+ recurringVehicule.toString());
        }
        return result;
    }
}
