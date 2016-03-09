package temp_logger.logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value="/climate")
public class TempLogger {

	private String dbURL = "jdbc:derby:/home/drussell/apache/derby_databases/climate_data";
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
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody String getLastTenOfficeTemps(){
		
		this.tableName="office_data";
		createConnection();		
		String retVal=null;
		
		try
        {
        	String query = String.format("SELECT * FROM %s ORDER BY ID DESC FETCH FIRST 10 ROWS ONLY",tableName);
        	PreparedStatement dataUpdate = conn.prepareStatement(query);
            ResultSet rs = dataUpdate.executeQuery();
            dataUpdate.clearParameters();
            
            double avgTemp=0.0;
            double avgHum=0.0;
            double avgHeatIndex=0.0;
            int count=0;
            
            while(rs.next()){
            	TemperatureRecord tr = (new Gson()).fromJson(rs.getString("JSON_DATA"), TemperatureRecord.class);
            	avgTemp+=tr.getTemperature();
            	avgHum+=tr.getHumidity();
            	avgHeatIndex+=tr.getHeatIndex();
            	count++;
            }
            
            double temp = Math.round((avgTemp/count)*100)/100.0;
            double hum = Math.round((avgHum/count)*100)/100.0;
            double hi = Math.round((avgHeatIndex/count)*100)/100.0;
            
            TemperatureRecord tempRecord = new TemperatureRecord(temp,hum,hi);
            retVal = (new Gson()).toJson(tempRecord);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		shutdown();
		return retVal;
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
        	String query = String.format("INSERT INTO %s (JSON_DATA,DATE_RECORDED) VALUES (?,CURRENT_TIMESTAMP)",tableName);
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
