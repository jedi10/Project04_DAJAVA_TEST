package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.model.RecurringVehicule;

import java.util.List;

public interface IRecurringVehiculeDAO {

    public List<RecurringVehicule> getListOfRecurrentVehicule();

    public RecurringVehicule getRecurringVehicule(String vehicleRegNumber);

    public boolean addRecurrentVehicule(RecurringVehicule recurringVehicule);

    public boolean updateRecurrentVehicule(RecurringVehicule recurringVehicule);

}
