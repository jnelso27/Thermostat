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
