package application.thermostat.message;

/***
 * Class represents the various types of messages that can be sent or
 * received between the Thermistat and Computer
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class MessageType
{
	/** Message used as a default message type */
	public static byte DEFAULT_MSG = 0x50;

	/** Message used to set the temperature sensor refresh periodicity */
	public static byte TEMP_SENSOR_REFRESH_PERIOD_MSG = 0x51;

	/** Message Type used to identify that the message contains temperature sensor measurement */
	public static byte TEMP_SENSOR_READING_MSG = 0x52;

	/** Message type used to request the temperature */
	public static byte TEMP_SENSOR_READING_REQUEST_MSG = 0x56;

	/** Variable Description */
	public static byte TEMP_SENSOR_DANGER_ALARM_THRESHOLD_SET_MSG = 0x57;

	/** Variable Description */
	public static byte TEMP_SENSOR_WARNING_ALARM_THRESHOLD_SET_MSG = 0x58;

	/** Message used to set thermistat alarm level to normal threshold */
	public static byte NORMAL_ALARM_SET_MSG = 0x53;

	/** Message used to set thermistat alarm level to warning threshold */
	public static byte WARNING_ALARM_SET_MSG = 0x54;

	/** Message used to set thermistat alarm level to danger threshold */
	public static byte DANGER_ALARM_SET_MSG = 0x55;
}
