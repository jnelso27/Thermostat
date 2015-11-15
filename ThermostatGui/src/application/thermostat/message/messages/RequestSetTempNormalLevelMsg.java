package application.thermostat.message.messages;

/***
 * Class Description
 *
 * Date of Last Change: 2015-11-07
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
	 * @param messageData
	 */
	public RequestSetTempNormalLevelMsg(byte[] messageData)
	{
		super(MessageType.NORMAL_ALARM_SET_MSG, messageData);
	}
}
