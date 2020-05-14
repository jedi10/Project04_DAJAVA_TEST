package com.parkit.parkingsystem.constants;

public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";

    public static final String SAVE_RECURRING_VEHICLE = "insert into recurring_vehicle(VEHICLE_REG_NUMBER, LAST_VISIT) values(?,?)";
    public static final String UPDATE_RECURRING_VEHICLE = "update recurring_vehicle set VEHICLE_REG_NUMBER=?, LAST_VISIT=? where ID=?";
    public static final String GET_RECURRING_VEHICLE_LIST = "select rv.ID, rv.VEHICLE_REG_NUMBER, rv.LAST_VISIT from recurring_vehicle rv order by rv.LAST_VISIT ASC";
    public static final String GET_RECURRING_VEHICLE = "select rv.ID, rv.VEHICLE_REG_NUMBER, rv.LAST_VISIT from recurring_vehicle rv where rv.VEHICLE_REG_NUMBER = ? order by rv.LAST_VISIT";

}
