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
public class TemperatureDangerLevelMessage extends Message
{
	/** Default Message Data */
	public static byte defaultMessageData[] = {0x00, 0x01};

	/**
	 * Default Constructor
	 */
	public TemperatureDangerLevelMessage()
	{
		super(MessageType.DANGER_ALARM_SET_MSG, defaultMessageData);
	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TemperatureDangerLevelMessage(byte[] messageData)
	{
		//super.buildMessage(MessageType.DANGER_ALARM_SET_MSG, messageData);
		super(MessageType.DANGER_ALARM_SET_MSG, messageData);
	}
}
