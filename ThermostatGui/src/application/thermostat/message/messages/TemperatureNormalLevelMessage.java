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
	/** Default Message Data */
	public static byte defaultMessageData[] = {0x00, 0x01};

	/**
	 * Default Constructor
	 */
	public TemperatureNormalLevelMessage()
	{
		super(MessageType.NORMAL_ALARM_SET_MSG, defaultMessageData);
	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TemperatureNormalLevelMessage(byte[] messageData)
	{
		//super.buildMessage(MessageType.NORMAL_ALARM_SET_MSG, messageData);
		super(MessageType.NORMAL_ALARM_SET_MSG, messageData);
	}
}
