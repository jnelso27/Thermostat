package application.thermostat.sensors;

import application.thermostat.message.Message;
import application.thermostat.message.MessageSender;
import application.thermostat.message.MessageType;
import application.thermostat.message.messages.TemperatureDangerLevelMessage;
import application.thermostat.message.messages.TemperatureWarningLevelMessage;

/**
 * Class represents the three types of "Alarms".
 *
 * Date of Last Change: 2015-10-22
 *
 * @author DeveloperMain
 *
 */
public class AlarmSensor extends Sensor
{
	/** Variable Description */
	byte data1[] = {0x00, 0x01}; //DEBUG CODE ONLY

	/**
	 * Default Constructor
	 */
	public AlarmSensor()
	{
		//Do Nothing
	}

	/**
	 * Overloaded Constructor
	 *
	 * @param sensorName
	 * @param sensorType
	 * @param testingFlag
	 */
	public AlarmSensor(String sensorName,int sensorType, boolean testingFlag)
	{
		super(sensorName, sensorType, testingFlag);
	}

	/**
	 *
	 */
	public void processReading(byte[] message)
	{
		//Do nothing for this sensor
	}

	/**
	 *
	 */
	public void requestData(Message message)
	{
		if(message.getMessageType() == MessageType.NORMAL_ALARM_SET_MSG)
		{
			MessageSender.sendMessage(message);
		}
		else if(message.getMessageType() == MessageType.WARNING_ALARM_SET_MSG)
		{
			MessageSender.sendMessage(message);
		}
		else if(message.getMessageType() == MessageType.DANGER_ALARM_SET_MSG)
		{
			MessageSender.sendMessage(message);
		}
		else
		{
			System.out.println("Don't know how to process a: " + message.getMessageType() + " type of message");
		}
	}

	/**
	 * Method used to request the DANGER level alarm
	 */
	public void requestAlarmHigh()
	{
		MessageSender.sendMessage(new TemperatureDangerLevelMessage(data1));
	}

	/**
	 * Method used to request the WARNING level alarm
	 */
	public void requestAlarmMed()
	{
		MessageSender.sendMessage(new TemperatureWarningLevelMessage(data1));
	}

	/**
	 * Method used to request the NORMAL level alarm
	 */
	public void requestAlarmLow()
	{

	}
}
