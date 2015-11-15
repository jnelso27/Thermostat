package application.thermostat.message.messages;

/***
 * Message used to request the temperature reading of the TMP102 sensor.
 *
 * Date of Last Change: 2015-11-12
 *
 * @author J Nelson
 *
 */
public class RequestGetTempReadingMsg extends Message
{
	/**
	 * Default Constructor
	 */
	public RequestGetTempReadingMsg()
	{

	}

	/**
	 * Overloaded constructor
	 *
	 * @param messageData The message data to add to the message.
	 */
	public RequestGetTempReadingMsg(byte[] messageData)
	{
		super(MessageType.TEMP_SENSOR_READING_REQUEST_MSG, messageData);
	}
}
