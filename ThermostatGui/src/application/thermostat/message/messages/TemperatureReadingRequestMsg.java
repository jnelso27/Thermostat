package application.thermostat.message.messages;

import application.thermostat.message.Message;
import application.thermostat.message.MessageType;

/**
 * @author DeveloperMain
 *
 */
public class TemperatureReadingRequestMsg extends Message
{
	/**
	 * Default Constructor
	 */
	public TemperatureReadingRequestMsg()
	{

	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TemperatureReadingRequestMsg(byte[] messageData)
	{
		super.buildMessage(MessageType.TEMP_SENSOR_READING_REQUEST_MSG, messageData);
	}
}
