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
	 * Method used to process an incoming message from the sensor.
	 * All subclasses should implement their specific logic based upon
	 * the sensor message.
	 *
	 * @param message The message to process
	 */
	public void processSensorData(byte[] message)
	{
		//Logic to be implemented by subclasses
	}

	/**
	 * Method used to request data from the sensor.
	 * All subclasses should implement their specific logic based upon the
	 * types of requests to be made on that specific sensor.
	 *
	 * @param request the request for data to be sent to the sensor
	 */
	public void requestSensorData(int request)
	{
		//To be implemented by subclasses
	}

	/**
	 * Method used to obtain the name of the sensor
	 *
	 * @return the name of the sensor
	 */
	public String getSensorName()
	{
		return sensorName;
	}

	/**
	 * Method used to set the name of the sensor
	 *
	 * @param sensorName the name of the sensor
	 */
	public void setSensorName(String sensorName)
	{
		this.sensorName = sensorName;
	}

	/**
	 * Method used to obtain the type of the sensor
	 *
	 * @return the sensor type
	 */
	public int getSensorType()
	{
		return sensorType;
	}

	/**
	 * Method used to set the type of the sensor
	 *
	 * @param sensorType the type of sensor
	 */
	public void setSensorType(int sensorType)
	{
		this.sensorType = sensorType;
	}

	/**
	 * Method used to obtain whether the sensor is in test mode
	 *
	 * @return the status of whether the sensor is in testing mode
	 */
	public boolean isTestingFlag()
	{
		return testingFlag;
	}

	/**
	 * Method used to set the sensor test flag
	 *
	 * @param testingFlag the testing flag to set
	 */
	public void setTestingFlag(boolean testingFlag)
	{
		this.testingFlag = testingFlag;
	}
}
