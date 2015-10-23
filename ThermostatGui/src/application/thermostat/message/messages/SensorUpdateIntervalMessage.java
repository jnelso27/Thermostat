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
