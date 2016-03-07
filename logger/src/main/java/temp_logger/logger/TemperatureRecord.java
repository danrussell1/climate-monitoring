package temp_logger.logger;

public class TemperatureRecord {

	private double temperature;
	private double humidity;
	private double heatIndex;
	
	public TemperatureRecord(double temperature, double humidity, double heatIndex) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.heatIndex = heatIndex;
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
