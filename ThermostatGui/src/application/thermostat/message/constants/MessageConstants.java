package application.thermostat.message.constants;

/***
 * Class Description
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class MessageConstants
{
	/** Variable Description */
	public static byte MSG_PADDING = 0x00;

	/** Variable Description */
	public static byte ACTIVATE = 0x01;

	/** Variable Description */
	public static byte DEACTIVATE = 0x02;

	/** Variable Description */
	public static final int REC_MSG_HEADER_NDX = 0;

	/** Variable Description */
	public static final int REC_MSG_TYPE_NDX = 1;

	/** Variable Description */
	public static final int REC_MSG_DATA_MSB_NDX = 2;

	/** Variable Description */
	public static final int REC_MSG_DATA_LSB_NDX = 3;

	/** Variable Description */
	public static final int REC_MSG_FOOTER_NDX = 4;
}
