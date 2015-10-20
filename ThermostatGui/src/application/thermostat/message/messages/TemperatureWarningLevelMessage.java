package application.thermostat.message.messages;

import application.thermostat.message.Message;
import application.thermostat.message.MessageType;

/***
 * Class
 *
 * @author Joshua
 *
 */
public class TemperatureWarningLevelMessage extends Message
{
	/**
	 * Default Constructor
	 */
	public TemperatureWarningLevelMessage()
	{

	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TemperatureWarningLevelMessage(byte[] messageData)
	{
		super.buildMessage(MessageType.WARNING_ALARM_SET_MSG, messageData);
	}
}
