

package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.RecurringVehicule;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RecurringVehiculeDAO implements IRecurringVehiculeDAO {

    private static final Logger logger = LogManager.getLogger("RecurringVehiculeDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    @Override
    public List<RecurringVehicule> getListOfRecurrentVehicule() {
        RecurringVehicule recurringVehicule = null;
        List<RecurringVehicule> recurringVehiculeList = new ArrayList<>();
        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    DBConstants.GET_RECURRING_VEHICLE_LIST);) {
            //ID, VEHICLE_REG_NUMBER, LAST_VISIT)
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    recurringVehicule = new RecurringVehicule(
                            rs.getString(2),
                            rs.getTimestamp(3).toInstant());
                    recurringVehicule.setId(rs.getInt(1));
                    recurringVehiculeList.add(recurringVehicule);
                }
            }
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }

        return recurringVehiculeList;
    }

    @Override
    public RecurringVehicule getRecurringVehicule(String vehicleRegNumber) {
        RecurringVehicule recurringVehicule = null;
        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    DBConstants.GET_RECURRING_VEHICLE);) {
            //ID, VEHICLE_REG_NUMBER, LAST_VIST)
            ps.setString(1, vehicleRegNumber);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    recurringVehicule = new RecurringVehicule(
                            rs.getString(2),
                            rs.getTimestamp(3).toInstant());
                    recurringVehicule.setId(rs.getInt(1));
                } else if (!rs.isBeforeFirst() ) {
                    //System.out.println("No data");
                    logger.error("getRecurringVehicule: No Data ");
                }
            }
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }

        return recurringVehicule;
    }

    @Override
    public RecurringVehicule addRecurrentVehicule(RecurringVehicule recurringVehicule) {
        RecurringVehicule result = null;
        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    DBConstants.SAVE_RECURRING_VEHICLE,
                    Statement.RETURN_GENERATED_KEYS);) {
            //ID, VEHICLE_REG_NUMBER, LAST_VISIT)
            //OffsetDateTime odt = OffsetDateTime.ofInstant(recurringVehicule.getLastVisit(), ZoneOffset.UTC);
            ps.setString(1, recurringVehicule.getVehicleRegNumber());
            ps.setTimestamp(2, Timestamp.from(recurringVehicule.getLastVisit()));
            //https://www.codota.com/code/java/classes/java.sql.PreparedStatement
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Recurrent Vehicle failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    result = new RecurringVehicule(recurringVehicule.getVehicleRegNumber(),
                            recurringVehicule.getLastVisit());
                    result.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating Recurrent Vehicle failed, no ID obtained.");
                }
            }
        }catch (Exception ex){
            logger.error("Error create new entry in recurring_vehicle base",ex);
        }

        return result;
    }

    @Override
    public int updateRecurrentVehicule(RecurringVehicule recurringVehicule) {
        int result = 0;
        try(Connection con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    DBConstants.UPDATE_RECURRING_VEHICLE);) {
            //ID, VEHICLE_REG_NUMBER, LAST_VISIT)
            ps.setString(1, recurringVehicule.getVehicleRegNumber());
            ps.setTimestamp(2, Timestamp.from(recurringVehicule.getLastVisit()));
            ps.setInt(3,recurringVehicule.getId());
            //https://stackoverflow.com/questions/23088708/prepared-statement-returns-false-but-row-is-inserted
            result = ps.executeUpdate();
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error saving recurringVehicle info",ex);
        }
        return result;
    }
}



////https://stackoverflow.com/questions/42766674/java-convert-java-time-instant-to-java-sql-timestamp-without-zone-offset
//https://stackoverflow.com/questions/43259722/java-date-and-timestamp-from-instance-of-zoneddatetime-utc