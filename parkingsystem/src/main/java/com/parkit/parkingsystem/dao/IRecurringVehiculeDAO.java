package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.model.RecurringVehicule;

import java.util.List;

public interface IRecurringVehiculeDAO {

    public List<RecurringVehicule> getListOfRecurrentVehicule();

    public RecurringVehicule getRecurringVehicule(String vehicleRegNumber);

    public RecurringVehicule addRecurrentVehicule(RecurringVehicule recurringVehicule);

    public int updateRecurrentVehicule(RecurringVehicule recurringVehicule);

    public void setDataBaseConfig(DataBaseConfig dataBaseConfig);

    public DataBaseConfig getDataBaseConfig();

}
