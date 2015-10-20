package application.thermostat.db;

import java.time.LocalDateTime;

/**
 * Class that represents a single TMP102 sensor record.
 *
 * @author DeveloperMain
 *
 */
public class TMP102SensorRecord
{
	//
	private double tempMeasurement = 0.0;

	//
	private LocalDateTime tempMeasurementDate;

	/**
	 * Constructor
	 *
	 * @param tempMeasurement
	 * @param localDateTime
	 */
	public TMP102SensorRecord(double tempMeasurement, LocalDateTime localDateTime)
	{
		this.tempMeasurement = tempMeasurement;
		this.tempMeasurementDate = localDateTime;
	}

	/**
	 * @return the tempMeasurement
	 */
	public double getTempMeasurement()
	{
		return tempMeasurement;
	}

	/**
	 * @param tempMeasurement the tempMeasurement to set
	 */
	public void setTempMeasurement(double tempMeasurement)
	{
		this.tempMeasurement = tempMeasurement;
	}

	/**
	 * @return the tempMeasurementDate
	 */
	public LocalDateTime getTempMeasurementDate()
	{
		return tempMeasurementDate;
	}

	/**
	 * @param tempMeasurementDate the tempMeasurementDate to set
	 */
	public void setTempMeasurementDate(LocalDateTime tempMeasurementDate)
	{
		this.tempMeasurementDate = tempMeasurementDate;
	}

	@Override
	public String toString()
	{
		return ("Temp: " + tempMeasurement + " :Date: " + tempMeasurementDate.toString());
	}
}
