package com.parkit.parkingsystem.model;

import java.util.Date;
import java.util.Objects;

public class RecurringVehicule {

    private int id;
    private String vehicleRegNumber;
    private Date lastVisit;

    public RecurringVehicule(String vehicleRegNumber, Date lastVisit) {
        this.vehicleRegNumber = vehicleRegNumber;
        this.lastVisit = lastVisit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecurringVehicule that = (RecurringVehicule) o;
        return vehicleRegNumber.equals(that.vehicleRegNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleRegNumber);
    }
}
