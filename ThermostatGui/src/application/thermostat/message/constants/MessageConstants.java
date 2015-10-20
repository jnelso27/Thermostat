/**
 *
 */
package application.thermostat.message.constants;

/**
 * @author DeveloperMain
 *
 */
public class MessageConstants
{
	//
	public static byte MSG_PADDING = 0x00;

	//
	public static byte ACTIVATE = 0x01;

	//
	public static byte DEACTIVATE = 0x02;

	//
	public static final int REC_MSG_HEADER_NDX = 0;

	//
	public static final int REC_MSG_TYPE_NDX = 1;

	//
	public static final int REC_MSG_DATA_MSB_NDX = 2;

	//
	public static final int REC_MSG_DATA_LSB_NDX = 3;

	//
	public static final int REC_MSG_FOOTER_NDX = 4;
}
