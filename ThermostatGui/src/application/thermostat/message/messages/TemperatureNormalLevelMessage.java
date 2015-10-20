package application.thermostat.message.messages;

import application.thermostat.message.Message;
import application.thermostat.message.MessageType;

/***
 * Class
 *
 * @author Joshua
 *
 */
public class TemperatureNormalLevelMessage extends Message
{
	/**
	 * Default Constructor
	 */
	public TemperatureNormalLevelMessage()
	{
		//Do Nothing
	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TemperatureNormalLevelMessage(byte[] messageData)
	{
		super.buildMessage(MessageType.NORMAL_ALARM_SET_MSG, messageData);
	}
}
