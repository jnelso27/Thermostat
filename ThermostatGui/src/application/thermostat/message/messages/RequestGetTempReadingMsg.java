package application.thermostat.message.messages;

/***
 * Class Description
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
	 * TODO
	 *
	 * @param messageData
	 */
	public RequestGetTempReadingMsg(byte[] messageData)
	{
		super(MessageType.TEMP_SENSOR_READING_REQUEST_MSG, messageData);
	}
}
