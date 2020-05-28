package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * <b>Integration Tests needs somme DBB Service</b>
 * <p>Datas in Test DBB table have to be erased</p>
 */
public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){
        Connection connection = null;
        PreparedStatement ps = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //set parking entries to available
            ps = connection.prepareStatement("update parking set available = true");
            ps.execute();

            //clear ticket entries;
            ps = connection.prepareStatement("truncate table ticket");
            ps.execute();

            //clear recurrence vehicle;
            ps = connection.prepareStatement("truncate table recurring_vehicle");
            ps.execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closePreparedStatement(ps);
            dataBaseTestConfig.closeConnection(connection);
        }
    }


}
