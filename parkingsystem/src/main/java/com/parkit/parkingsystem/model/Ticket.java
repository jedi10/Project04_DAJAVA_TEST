package com.parkit.parkingsystem.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return this.inTime == null ? null : new Date(this.inTime.getTime());
    }

    public void setInTime(Date inTime) {
        this.inTime = Optional.ofNullable(inTime)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
                //new Date(inTime.getTime());
    }

    public Date getOutTime() {
        return this.outTime == null ? null : new Date(this.outTime.getTime());
    }

    public void setOutTime(Date outTime) {
        this.outTime = Optional.ofNullable(outTime)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
                //new Date(outTime.getTime());
    }
}
