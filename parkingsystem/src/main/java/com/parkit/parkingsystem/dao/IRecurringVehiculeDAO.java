package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.model.RecurringVehicule;

import java.util.List;

public interface IRecurringVehiculeDAO {

    public List<RecurringVehicule> getListOfVehiculeRegNumber();

    public boolean addVehiculeRegNumber();

    public boolean updateVehiculeRegNumber();

}
