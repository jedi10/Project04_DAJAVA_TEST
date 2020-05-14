package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.RecurringVehicule;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecurringVehiculeDAO implements IRecurringVehiculeDAO {

    private static final Logger logger = LogManager.getLogger("RecurringVehiculeDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    @Override
    public List<RecurringVehicule> getListOfRecurrentVehicule() {

        List<RecurringVehicule> recurringVehiculeList = new ArrayList<>();

        return recurringVehiculeList;
    }

    @Override
    public RecurringVehicule getRecurringVehicule(String vehicleRegNumber) {

        RecurringVehicule recurringVehicule = null;

        return recurringVehicule;
    }

    @Override
    public boolean addRecurrentVehicule(RecurringVehicule recurringVehicule) {
        boolean result = false;
        return result;
    }

    @Override
    public boolean updateRecurrentVehicule(RecurringVehicule recurringVehicule) {
        return false;
    }
}
