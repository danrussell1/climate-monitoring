package temp_logger.logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class TempLogger {

	private String dbURL = "jdbc:derby:/opt/apache/derby_databases/climate_data;";
    private String tableName = null;
    private Connection conn = null;
    
	@RequestMapping("/log_temp")
	public void logTemp(@RequestParam(value="tableName") String tableName,@RequestParam(value="temp") double temperature,@RequestParam(value="hum") double humidity,@RequestParam(value="hi") double heatIndex){
		this.tableName=tableName;
		createConnection();
		TemperatureRecord record = new TemperatureRecord(temperature, humidity, heatIndex);
		insertClientRecord(record);
		shutdown();
	}
	
	public void getLastTenOfficeTemps(){
		
	}
	
	private void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	private void insertClientRecord(TemperatureRecord record)
    {
        try
        {
        	String query = String.format("INSERT INTO %s (LOG_DATA,LOG_DATE) VALUES (?,CURRENT_TIMESTAMP)",tableName);
        	PreparedStatement dataUpdate = conn.prepareStatement(query);
            dataUpdate.setString(1,(new Gson()).toJson(record));
            dataUpdate.executeUpdate();
            dataUpdate.clearParameters();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
	
	private void shutdown()
    {
        try
        {
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
