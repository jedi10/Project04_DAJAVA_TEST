package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.model.RecurringVehicule;

import java.util.List;

/**
 * CRUD operations for RecurringVehicle Table
 */
public interface IRecurringVehiculeDAO {

    public List<RecurringVehicule> getListOfRecurrentVehicule();

    public RecurringVehicule getRecurringVehicule(String vehicleRegNumber);

    public RecurringVehicule addRecurrentVehicule(RecurringVehicule recurringVehicule);

    public int updateRecurrentVehicule(RecurringVehicule recurringVehicule);

    /**
     * <b>Setter for Resource needed to connect with DBB</b>
     * <p>useful to choose Type of Table used (Production or Test)</p>     *
     * @param dataBaseConfig resource needed to connect with DBB
     * @see DataBaseConfig
     */
    public void setDataBaseConfig(DataBaseConfig dataBaseConfig);

    /**
     * Getter for Resource needed to connect with DBB
     * @return dataBaseConfig
     * @see DataBaseConfig
     */
    public DataBaseConfig getDataBaseConfig();

}
