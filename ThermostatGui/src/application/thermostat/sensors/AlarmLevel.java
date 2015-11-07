package application.thermostat.sensors;

/**
 * Class represents the different levels of alarms for the thermostat
 *
 * DANGER(RED)    : Temperature is above the high threshold mark
 * WARNING(YELLOW): Temperature is below the high threshold mark but above the low threshold mark
 * NORMAL (GREEN) : Temperature is below or at the low threshold mark
 *
 * Date of Last Change: 2015-11-07
 *
 * @author J Nelson
 *
 */
public class AlarmLevel
{
	/** Represents the state of the alarm when the temperature is below or at the low threshold */
	public static final int NORMAL = 0;

	/** Represents the state of the alarm when the temperature is below the high threshold but above the low threshold */
	public static final int WARNING = 1;

	/** Represents the state of the alarm when the temperature is above the high threshold */
	public static final int DANGER = 2;
}
