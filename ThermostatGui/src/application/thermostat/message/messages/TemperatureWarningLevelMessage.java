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
public class TemperatureWarningLevelMessage extends Message
{
	/** Default Message Data */
	public static byte defaultMessageData[] = {0x00, 0x01};

	/**
	 * Default Constructor
	 */
	public TemperatureWarningLevelMessage()
	{
		super(MessageType.WARNING_ALARM_SET_MSG, defaultMessageData);
	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TemperatureWarningLevelMessage(byte[] messageData)
	{
		//super.buildMessage(MessageType.WARNING_ALARM_SET_MSG, messageData);
		super(MessageType.WARNING_ALARM_SET_MSG, messageData);
	}
}
