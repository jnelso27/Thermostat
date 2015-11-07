package application.thermostat.sensors;

import java.util.Observable;

/***
 * Class represents a generic sensor object. All sensors should be subclasses of the Sensor
 * class.
 *
 * Date of Last Change: 2015-11-07
 *
 * @author J Nelson
 *
 */
public class Sensor extends Observable
{
	/** Name of the Sensor */
	public String sensorName = "";

	/** Type of Sensor */
	public int sensorType = SensorType.DEFAULT;

	/** Testing flag used to simulated sensor functionality for testing purposes */
	public boolean testingFlag = true;

	/**
	 * Default Constructor
	 */
	public Sensor()
	{
		//Do Nothing
	}

	/**
	 * Overloaded Constructor
	 *
	 * @param sensorName The name of the sensor
	 * @param sensorType The type of sensor
	 * @param testingFlag Whether or not the sensor is in testing mode
	 */
	public Sensor(String sensorName, int sensorType, boolean testingFlag)
	{
		this.sensorName = sensorName;
		this.sensorType = sensorType;
		this.testingFlag = testingFlag;
	}

	/**
	 * Method Description
	 *
	 * @param message
	 */
	public void processSensorData(byte[] message)
	{

	}

	/**
	 * Method Description
	 *
	 * @param request
	 */
	public void requestSensorData(int request)
	{

	}

	/**
	 * Method used to obtain the name of the sensor
	 *
	 * @return
	 */
	public String getSensorName()
	{
		return sensorName;
	}

	/**
	 * Method used to set the name of the sensor
	 *
	 * @param sensorName the sensorName to set
	 */
	public void setSensorName(String sensorName)
	{
		this.sensorName = sensorName;
	}

	/**
	 * Method used to obtain the type of the sensor
	 *
	 * @return the sensorType
	 */
	public int getSensorType()
	{
		return sensorType;
	}

	/**
	 * Method used to set the type of the sensor
	 *
	 * @param sensorType the sensorType to set
	 */
	public void setSensorType(int sensorType)
	{
		this.sensorType = sensorType;
	}

	/**
	 * Method used to obtain whether the sensor is in test mode
	 *
	 * @return the testingFlag
	 */
	public boolean isTestingFlag()
	{
		return testingFlag;
	}

	/**
	 * Method used to set the sensor test flag
	 *
	 * @param testingFlag the testingFlag to set
	 */
	public void setTestingFlag(boolean testingFlag)
	{
		this.testingFlag = testingFlag;
	}
}
