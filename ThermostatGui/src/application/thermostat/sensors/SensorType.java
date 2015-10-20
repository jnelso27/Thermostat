package application.thermostat.sensors;

/**
 * @author DeveloperMain
 *
 */
public class SensorType
{
	public static final int DEFAULT = 0;

	public static final int TEMPERATURE = 1;

	public static final int LED = 2;

	public static final int ACCELEROMETER = 3;

	/**
	 * @return the default
	 */
	public static int getDefault()
	{
		return DEFAULT;
	}

	/**
	 * @return the temperature
	 */
	public static int getTemperature()
	{
		return TEMPERATURE;
	}

	/**
	 * @return the led
	 */
	public static int getLed()
	{
		return LED;
	}

	/**
	 * @return the accelerometer
	 */
	public static int getAccelerometer()
	{
		return ACCELEROMETER;
	}
}