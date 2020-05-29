package com.parkit.parkingsystem.model;

import java.time.Instant;
import java.util.Objects;

/**
 * <b>Mutable Model for Recurrent Vehicle</b>
 * @author Jedy10
 * @since beta1.0.0
 */
public class RecurringVehicule {

    private int id;
    /**
     * vehicle number
     * @see RecurringVehicule#getVehicleRegNumber()
     */
    private String vehicleRegNumber;

    /**
     * last visit date for a recurrent vehicle
     * @see RecurringVehicule#getLastVisit()
     */
    private Instant lastVisit;

    /**
     *
     * @param vehicleRegNumber number of vehicle (String)
     * @param lastVisit date of last visit (Instant)
     */
    public RecurringVehicule(String vehicleRegNumber, Instant lastVisit) {
        this.vehicleRegNumber = vehicleRegNumber;
        this.lastVisit = lastVisit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get vehicle number
     * @return vehicle number (String)
     * @see RecurringVehicule#vehicleRegNumber
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Get Last Visit Date
     * @return last visit Date
     * @see RecurringVehicule#lastVisit
     */
    public Instant getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Instant lastVisit) {
        this.lastVisit = lastVisit;
    }

    /**
     * equal is tested only on vehicleRegNumber
     * @see RecurringVehicule#vehicleRegNumber
     * @param o object to compare
     * @return true if given object is equal
     */
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
