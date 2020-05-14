

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
import java.util.ArrayList;
import java.util.List;

public class RecurringVehiculeDAO implements IRecurringVehiculeDAO {

    private static final Logger logger = LogManager.getLogger("RecurringVehiculeDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    @Override
    public List<RecurringVehicule> getListOfRecurrentVehicule() {
        Connection con = null;
        RecurringVehicule recurringVehicule = null;
        List<RecurringVehicule> recurringVehiculeList = new ArrayList<>();
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RECURRING_VEHICLE_LIST);
            //ID, VEHICLE_REG_NUMBER, LAST_VISIT)
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                recurringVehicule = new RecurringVehicule(
                        rs.getString(2),
                        rs.getTimestamp(3).toInstant());
                recurringVehicule.setId(rs.getInt(1));
                recurringVehiculeList.add(recurringVehicule);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return recurringVehiculeList;
    }

    @Override
    public RecurringVehicule getRecurringVehicule(String vehicleRegNumber) {
        Connection con = null;
        RecurringVehicule recurringVehicule = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_RECURRING_VEHICLE);
            //ID, VEHICLE_REG_NUMBER, LAST_VIST)
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                recurringVehicule = new RecurringVehicule(
                        rs.getString(2),
                        rs.getTimestamp(3).toInstant());
                recurringVehicule.setId(rs.getInt(1));
            } else if (!rs.isBeforeFirst() ) {
                //System.out.println("No data");
                logger.error("getRecurringVehicule: No Data ");
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return recurringVehicule;
    }

    @Override
    public RecurringVehicule addRecurrentVehicule(RecurringVehicule recurringVehicule) {
        Connection con = null;
        RecurringVehicule result = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    DBConstants.SAVE_RECURRING_VEHICLE,
                    Statement.RETURN_GENERATED_KEYS);
            //ID, VEHICLE_REG_NUMBER, LAST_VISIT)
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
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    @Override
    public int updateRecurrentVehicule(RecurringVehicule recurringVehicule) {
        Connection con = null;
        int result = 0;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_RECURRING_VEHICLE);
            //ID, VEHICLE_REG_NUMBER, LAST_VISIT)
            ps.setString(1, recurringVehicule.getVehicleRegNumber());
            ps.setTimestamp(2, Timestamp.from(recurringVehicule.getLastVisit()));
            ps.setInt(3,recurringVehicule.getId());
            //https://stackoverflow.com/questions/23088708/prepared-statement-returns-false-but-row-is-inserted
            result = ps.executeUpdate();
        }catch (Exception ex){
            logger.error("Error saving recurringVehicle info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }
}
