package application.thermostat.message.messages;

/***
 * Class Description
 *
 * Date of Last Change: 2015-11-07
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
	 * TODO
	 *
	 * @param messageData
	 */
	public RequestSetTempDangerLevelMsg(byte[] messageData)
	{
		super(MessageType.DANGER_ALARM_SET_MSG, messageData);
	}
}
