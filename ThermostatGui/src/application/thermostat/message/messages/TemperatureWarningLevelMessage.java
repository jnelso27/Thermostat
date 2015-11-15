package application.thermostat.message.messages;

/***
 * Message used to request the MCU to enable the Warning Alarm
 * indicator (LED).
 *
 * This message is currently sent from the Host (PC)
 * to the Client (MCU) when the temperature is above the configured value
 * for the lowThreshold value but below the configured value for
 * highThreshold (Thermostat class).
 *
 * Date of Last Change: 2015-11-15
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
	 * @param messageData The message data to add to the message.
	 */
	public TemperatureWarningLevelMessage(byte[] messageData)
	{
		super(MessageType.WARNING_ALARM_SET_MSG, messageData);
	}
}
