package application.thermostat.message.messages;

/***
 * Message used to request the MCU to enable the Normal Alarm
 * indicator (LED).
 *
 * This message is currently sent from the Host (PC)
 * to the Client (MCU) when the temperature is below or at the configured value
 * for the lowThreshold value (Thermostat class).
 *
 * Date of Last Change: 2015-11-15
 *
 * @author J Nelson
 *
 */
public class RequestSetTempNormalLevelMsg extends Message
{
	/** Default Message Data */
	public static byte defaultMessageData[] = {0x00, 0x01};

	/**
	 * Default Constructor
	 */
	public RequestSetTempNormalLevelMsg()
	{
		super(MessageType.NORMAL_ALARM_SET_MSG, defaultMessageData);
	}

	/**
	 * TODO
	 *
	 * @param messageData The message data to add to the message.
	 */
	public RequestSetTempNormalLevelMsg(byte[] messageData)
	{
		super(MessageType.NORMAL_ALARM_SET_MSG, messageData);
	}
}
