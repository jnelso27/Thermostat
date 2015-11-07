package application.thermostat.sensors;

import application.thermostat.message.Message;
import application.thermostat.message.MessageSender;
import application.thermostat.message.messages.TemperatureDangerLevelMessage;
import application.thermostat.message.messages.TemperatureReadingRequestMessage;

/***
 * Class Description
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class TemperatureSensor extends Sensor
{
	/** Used to request a temperature reading */
	public static int TEMPERATURE_READING_REQUEST = 0;

	/** Temperature in Degrees Celcius */
	public double temp_in_celcius = 0;

	/** Temperature in Degrees Farenheit */
	public double temp_in_farenheit = 0;

	/**
	 * Default Constructor
	 */
	public TemperatureSensor()
	{
		//Do Nothing
	}

	/**
	 * Overloaded Constructor
	 *
	 * @param sensorName
	 * @param sensorType
	 * @param testingFlag
	 */
	public TemperatureSensor(String sensorName,int sensorType, boolean testingFlag)
	{
		super(sensorName, sensorType, testingFlag);
	}

	//@Override
	public void processReading(byte[] message)
	{
		//Code to save off the temperature

		//Code to process reading
		temp_in_celcius = getTemperatureInCelcius(message);
		System.out.println("Temperature Reading (C): " + temp_in_celcius);

		temp_in_farenheit = (1.8 * temp_in_celcius) + 32;
		System.out.println("Temperature Reading (F): " + temp_in_farenheit);

		setChanged();
		notifyObservers(temp_in_farenheit);
	}

	/**
	 * Method Description
	 *
	 * @param message
	 * @return
	 */
	public double getTemperatureInCelcius(byte[] message)
	{
		int convertedTemp = ((message[Message.REC_MSG_DATA_MSB_NDX] << 4) + (message[Message.REC_MSG_DATA_LSB_NDX] >> 4));

		return convertedTemp*0.0625;
	}

	@Override
	public void requestData(int request)
	{
		if(request == TEMPERATURE_READING_REQUEST)
		{
			requestTemperature();
		}
	}

	private void requestTemperature()
	{
		byte messageData[] = {'G','E'};

		MessageSender.sendMessage(new TemperatureReadingRequestMessage(messageData));
	}
}
