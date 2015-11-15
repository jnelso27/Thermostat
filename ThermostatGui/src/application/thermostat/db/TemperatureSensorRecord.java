package application.thermostat.db;

import java.time.LocalDateTime;

/**
 * Class that represents a single TMP102 sensor record.
 *
 * Date of Last Change: 2015-11-13
 *
 * @author J Nelson
 *
 */
public class TemperatureSensorRecord
{
	/** Reading from the temperature sensor */
	private double tempMeasurement = 0.0;

	/** Date for the time of the reading */
	private LocalDateTime tempMeasurementDate;

	/**
	 * Default Constructor
	 */
	public TemperatureSensorRecord()
	{
		//Do nothing
	}

	/**
	 * Overloaded constructor
	 *
	 * @param tempMeasurement The reading as measured from the temperature sensor.
	 * @param localDateTime The date/time of the reading.
	 */
	public TemperatureSensorRecord(double tempMeasurement, LocalDateTime localDateTime)
	{
		this.tempMeasurement = tempMeasurement;
		this.tempMeasurementDate = localDateTime;
	}

	/**
	 * Method to get the temperature measurement.
	 *
	 * @return The tempMeasurement.
	 */
	public double getTempMeasurement()
	{
		return tempMeasurement;
	}

	/**
	 * Method used to set the temperature measurement.
	 *
	 * @param tempMeasurement The tempMeasurement to set.
	 */
	public void setTempMeasurement(double tempMeasurement)
	{
		this.tempMeasurement = tempMeasurement;
	}

	/**
	 * Method used to obtain the date/time of the temperature measurement.
	 *
	 * @return The tempMeasurementDate.
	 */
	public LocalDateTime getTempMeasurementDate()
	{
		return tempMeasurementDate;
	}

	/**
	 * Method used to set the date/time of the temperature measurement.
	 *
	 * @param tempMeasurementDate the tempMeasurementDate to set.
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
