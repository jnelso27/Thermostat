package application.thermostat.message.messages;

import application.thermostat.message.Message;
import application.thermostat.message.MessageType;

/***
 * TODO
 *
 * @author DeveloperMain
 *
 */
public class SensorUpdateIntervalMessage extends Message
{
	/**
	 * Default Constructor
	 */
	public SensorUpdateIntervalMessage()
	{
		//Do Nothing
	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public SensorUpdateIntervalMessage(byte[] messageData)
	{
		super.buildMessage(MessageType.TEMP_SENSOR_REFRESH_PERIOD_MSG, messageData);
	}
}
