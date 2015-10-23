/**
 *
 */
package application.thermostat.sensors;

/**
 * Class represents the different levels of alarms for the thermistat
 *
 * DANGER(RED)    : Temperature is above the high threshold mark
 * WARNING(YELLOW): Temperature is below the high threshold mark but above the low threshold mark
 * NORMAL (GREEN) : Temperature is below or at the low threshold mark
 *
 * Date of Last Change: 2015-10-22
 *
 * @author Joshua Nelson
 *
 */
public class AlarmLevel
{
	/** Variable Description */
	public static final int NORMAL = 0;

	/** Variable Description */
	public static final int WARNING = 1;

	/** Variable Description */
	public static final int DANGER = 2;
}
