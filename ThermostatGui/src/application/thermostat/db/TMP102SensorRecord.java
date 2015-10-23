package application.thermostat.db;

import java.time.LocalDateTime;

/**
 * Class that represents a single TMP102 sensor record.
 *
 * Date:
 *
 * @author J Nelson
 *
 */
public class TMP102SensorRecord
{
	/** Variable Description */
	private double tempMeasurement = 0.0;

	/** Variable Description */
	private LocalDateTime tempMeasurementDate;

	/**
	 * Default Constructor
	 */
	public TMP102SensorRecord()
	{
		//Do nothing in the Default Constructor
	}

	/**
	 * Constructor Description
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
	 * Method Description
	 *
	 * @return the tempMeasurement
	 */
	public double getTempMeasurement()
	{
		return tempMeasurement;
	}

	/**
	 * Method Description
	 *
	 * @param tempMeasurement the tempMeasurement to set
	 */
	public void setTempMeasurement(double tempMeasurement)
	{
		this.tempMeasurement = tempMeasurement;
	}

	/**
	 * Method Description
	 *
	 * @return the tempMeasurementDate
	 */
	public LocalDateTime getTempMeasurementDate()
	{
		return tempMeasurementDate;
	}

	/**
	 * Method Description
	 *
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
