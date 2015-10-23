package application.thermostat.message.messages;

import application.thermostat.message.Message;
import application.thermostat.message.MessageType;

/***
 * Class Description
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
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
