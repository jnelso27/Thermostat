/**
 *
 */
package application.thermostat.sensors;

import java.util.Observable;

import application.thermostat.message.Message;

/**
 * @author DeveloperMain
 *
 */
public class Sensor extends Observable
{
	//Name of the Sensor
	public String sensorName = "";

	//Type of Sensor
	public int sensorType = SensorType.DEFAULT;

	//Testing flag used to simulated sensor functionality for testing purposes
	public boolean testingFlag = true;

	/**
	 * Default Constructor
	 */
	public Sensor()
	{
		//Do Nothing
	}
	/**
	 * Constructor
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

	//Do nothing right now
	public void processReading(byte[] message)
	{

	}

	public void requestData(Message message)
	{

	}

	/**
	 * @return the sensorName
	 */
	public String getSensorName()
	{
		return sensorName;
	}

	/**
	 * @param sensorName the sensorName to set
	 */
	public void setSensorName(String sensorName)
	{
		this.sensorName = sensorName;
	}

	/**
	 * @return the sensorType
	 */
	public int getSensorType()
	{
		return sensorType;
	}

	/**
	 * @param sensorType the sensorType to set
	 */
	public void setSensorType(int sensorType)
	{
		this.sensorType = sensorType;
	}

	/**
	 * @return the testingFlag
	 */
	public boolean isTestingFlag()
	{
		return testingFlag;
	}

	/**
	 * @param testingFlag the testingFlag to set
	 */
	public void setTestingFlag(boolean testingFlag)
	{
		this.testingFlag = testingFlag;
	}
}
