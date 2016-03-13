package temp_logger.logger;

import java.util.Date;

public class TemperatureRecord {

	private double temperature;
	private double humidity;
	private double heatIndex;
	private Date timestamp;
	
	public TemperatureRecord(double temperature, double humidity, double heatIndex, Date timestamp) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.heatIndex = heatIndex;
		this.timestamp=timestamp;
	}
	
	public Date getTimestamp(){
		return timestamp;
	}

	public double getTemperature() {
		return temperature;
	}

	public double getHumidity() {
		return humidity;
	}

	public double getHeatIndex() {
		return heatIndex;
	}
	
	
}
