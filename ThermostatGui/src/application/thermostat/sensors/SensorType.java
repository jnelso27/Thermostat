package application.thermostat.sensors;

/***
 * Class Description
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class SensorType
{
	/** Variable Description */
	public static final int DEFAULT = 0;

	/** Variable Description */
	public static final int TEMPERATURE = 1;

	/** Variable Description */
	public static final int LED = 2;

	/** Variable Description */
	public static final int ACCELEROMETER = 3;

	/**
	 * Method Description (DELETE IF NO LONGER USED)
	 *
	 * @return the default
	 */
	public static int getDefault()
	{
		return DEFAULT;
	}

	/**
	 * Method Description (DELETE IF NO LONGER USED)
	 *
	 * @return the temperature
	 */
	public static int getTemperature()
	{
		return TEMPERATURE;
	}

	/**
	 * Method Description (DELETE IF NO LONGER USED)
	 *
	 * @return the led
	 */
	public static int getLed()
	{
		return LED;
	}

	/**
	 * @return the accelerometer (DELETE IF NO LONGER USED)
	 */
	public static int getAccelerometer()
	{
		return ACCELEROMETER;
	}
}