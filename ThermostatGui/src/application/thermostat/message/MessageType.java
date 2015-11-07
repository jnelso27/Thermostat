package application.thermostat.message;

/***
 * Class represents the various types of messages that can be sent or
 * received between the Thermostat and Computer
 *
 * Date of Last Change: 2015-11-07
 *
 * @author J Nelson
 *
 */
public class MessageType
{
	/** Message used as a default message type */
	public static byte DEFAULT_MSG = 0x50;

	/** Message Type used to identify that the message contains temperature sensor measurement */
	public static byte TEMP_SENSOR_READING_MSG = 0x52;

	/** Message type used to request the temperature */
	public static byte TEMP_SENSOR_READING_REQUEST_MSG = 0x56;

	/** Message used to set thermostat alarm level to normal threshold */
	public static byte NORMAL_ALARM_SET_MSG = 0x53;

	/** Message used to set thermostat alarm level to warning threshold */
	public static byte WARNING_ALARM_SET_MSG = 0x54;

	/** Message used to set thermostat alarm level to danger threshold */
	public static byte DANGER_ALARM_SET_MSG = 0x55;
}
