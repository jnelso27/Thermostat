package application.thermostat.message.messages;

/***
 * Message used to request the MCU to enable the Danger Alarm
 * indicator (LED).
 *
 * This message is currently sent from the Host (PC)
 * to the Client (MCU) when the temperature exceeds the configured value
 * for the highThreshold value (Thermostat class).
 *
 * Date of Last Change: 2015-11-15
 *
 * @author J Nelson
 *
 */
public class RequestSetTempDangerLevelMsg extends Message
{
	/** Default Message Data */
	public static byte defaultMessageData[] = {0x00, 0x01};

	/**
	 * Default Constructor
	 */
	public RequestSetTempDangerLevelMsg()
	{
		super(MessageType.DANGER_ALARM_SET_MSG, defaultMessageData);
	}

	/**
	 * Overloaded constructor
	 *
	 * @param messageData The message data to add to the message.
	 */
	public RequestSetTempDangerLevelMsg(byte[] messageData)
	{
		super(MessageType.DANGER_ALARM_SET_MSG, messageData);
	}
}
