package application.thermostat.sensors;

import application.thermostat.message.MessageSender;
import application.thermostat.message.messages.Message;
import application.thermostat.message.messages.RequestGetTempReadingMsg;

/***
 * Class Description
 *
 * Date of Last Change: 2015-11-09
 *
 * @author J Nelson
 *
 */
public class TemperatureSensor extends Sensor
{
	/** Used to request a temperature reading */
	public static int TEMPERATURE_READING_REQUEST = 0;

	/** Temperature in Degrees Celcius */
	private double tempInCelcius = 0;

	/** Temperature in Degrees Farenheit */
	private double tempInFarenheit = 0;

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

	@Override
	public void processSensorData(byte[] message)
	{
		//Code to save off the temperature

		//Code to process reading
		tempInCelcius = getTemperatureInCelcius(message);
		System.out.println("Temperature Reading (C): " + tempInCelcius);

		tempInFarenheit = (1.8 * tempInCelcius) + 32;
		System.out.println("Temperature Reading (F): " + tempInFarenheit);

		setChanged();
		notifyObservers(tempInFarenheit);
	}

	@Override
	public void requestSensorData(int request)
	{
		if(request == TEMPERATURE_READING_REQUEST)
		{
			requestTemperature();
		}
	}

	/**
	 * Method Description
	 *
	 * @param message
	 * @return
	 */
	private double getTemperatureInCelcius(byte[] message)
	{
		int convertedTemp = ((message[Message.REC_MSG_DATA_MSB_NDX] << 4) + (message[Message.REC_MSG_DATA_LSB_NDX] >> 4));

		return convertedTemp*0.0625;
	}

	/**
	 * Method that is used to request the Temperature reading from the sensor.
	 */
	private void requestTemperature()
	{
		byte messageData[] = {'G','E'};

		MessageSender.sendMessage(new RequestGetTempReadingMsg(messageData));
	}
}
