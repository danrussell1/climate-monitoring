package temp_logger.logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempLogger {

	private String dbURL = "jdbc:derby:/home/drussell/Derby_Databases/climate_data;";
    private String tableName = null;
    private Connection conn = null;
    
	@RequestMapping("/log_temp")
	public void logTemp(@RequestParam(value="tableName") String tableName,@RequestParam(value="temp") String temperature,@RequestParam(value="hum") String humidity,@RequestParam(value="hi") String heatIndex){
		this.tableName=tableName;
		createConnection();
		insertClientRecord(temperature, humidity, heatIndex);
		shutdown();
	}
	
	private void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
	
	private void insertClientRecord(String temperature, String humidity, String heatIndex)
    {
        try
        {
        	String query = String.format("INSERT INTO %s (TEMPERATURE,HUMIDITY,HEAT_INDEX,TIMESTAMP) VALUES (?,?,?,CURRENT_TIMESTAMP)",tableName);
        	PreparedStatement dataUpdate = conn.prepareStatement(query);
            dataUpdate.setString(1,temperature);
            dataUpdate.setString(2, humidity);
            dataUpdate.setString(3, heatIndex);
            dataUpdate.executeUpdate();
            dataUpdate.clearParameters();
        	//stmt = conn.createStatement();
            //stmt.execute("insert into " + tableName + " (TEMPERATURE,HUMIDITY,HEAT_INDEX,TIMESTAMP) values ("+ temperature + "," + humidity + ","+ heatIndex +",CURRENT_TIMESTAMP)");
            //stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
	
	private void shutdown()
    {
        try
        {
            /*if (stmt != null)
            {
                stmt.close();
            }*/
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
}
