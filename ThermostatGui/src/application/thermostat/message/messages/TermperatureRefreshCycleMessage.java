package application.thermostat.message.messages;

import application.thermostat.message.Message;
import application.thermostat.message.MessageType;

/***
 * Class
 *
 * @author Joshua
 *
 */
public class TermperatureRefreshCycleMessage extends Message
{
	/**
	 *
	 */
	public TermperatureRefreshCycleMessage()
	{
		//Do Nothing
	}

	/**
	 * TODO
	 *
	 * @param messageData
	 */
	public TermperatureRefreshCycleMessage(byte[] messageData)
	{
		super.buildMessage(MessageType.TEMP_SENSOR_REFRESH_PERIOD_MSG, messageData);
	}
}
