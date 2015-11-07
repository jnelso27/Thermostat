package application.thermostat.message.messages;

import application.thermostat.message.Message;
import application.thermostat.message.MessageType;

/***
 * Class Description
 *
 * Date of Last Change: 2015-11-07
 *
 * @author J Nelson
 *
 */
public class TemperatureReadingRequestMessage extends Message
{
	/**
	 * Default Constructor
	 */
	public TemperatureReadingRequestMessage()
	{

	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TemperatureReadingRequestMessage(byte[] messageData)
	{
		super(MessageType.TEMP_SENSOR_READING_REQUEST_MSG, messageData);
	}
}
