package application.thermostat.sensors;

import application.thermostat.message.MessageSender;
import application.thermostat.message.messages.TemperatureDangerLevelMessage;
import application.thermostat.message.messages.TemperatureNormalLevelMessage;
import application.thermostat.message.messages.TemperatureWarningLevelMessage;

/**
 * Class represents an Alarm (LED) Sensor.
 * Provides multiple static variables for which calling objects should use for the specific
 * types of requests to be made. For the Alarm Sensor, there are currently three types of
 * requests to be called:
 *
 * NORMAL_ALARM_REQUEST: Request to set the Normal Alarm
 * WARNING_ALARM_REQUEST: Request to set the Warning Alarm
 * DANGER_ALARM_REQUEST: Request to set the Danger Alarm
 *
 * Date of Last Change: 2015-11-07
 *
 * @author J Nelson
 *
 */
public class AlarmSensor extends Sensor
{
	/** Used to request what type of alarms to set */
	public static int NORMAL_ALARM_REQUEST = 0;
	public static int WARNING_ALARM_REQUEST = 1;
	public static int DANGER_ALARM_REQUEST = 2;

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
	 * @param sensorName The name of the sensor
	 * @param sensorType The type of sensor to create
	 * @param testingFlag The testing flag. Set to "true" to put sensor in testing mode
	 */
	public AlarmSensor(String sensorName,int sensorType, boolean testingFlag)
	{
		super(sensorName, sensorType, testingFlag);
	}

	@Override
	public void processSensorData(byte[] message)
	{
		//Do nothing for this sensor
	}

	/*
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
	*/

	@Override
	public void requestSensorData(int request)
	{
		if(request == NORMAL_ALARM_REQUEST)
		{
			requestAlarmLow();
		}
		else if(request == WARNING_ALARM_REQUEST)
		{
			requestAlarmMed();
		}
		else if(request == DANGER_ALARM_REQUEST)
		{
			requestAlarmHigh();
		}
		else
		{
			System.out.println("Don't know how to process");
		}
	}

	/**
	 * Method used to request the DANGER level alarm
	 */
	public void requestAlarmHigh()
	{
		//MessageSender.sendMessage(new TemperatureDangerLevelMessage(data1));
		MessageSender.sendMessage(new TemperatureDangerLevelMessage());
	}

	/**
	 * Method used to request the WARNING level alarm
	 */
	public void requestAlarmMed()
	{
		//MessageSender.sendMessage(new TemperatureWarningLevelMessage(data1));
		MessageSender.sendMessage(new TemperatureWarningLevelMessage());
	}

	/**
	 * Method used to request the NORMAL level alarm
	 */
	public void requestAlarmLow()
	{
		//MessageSender.sendMessage(new TemperatureNormalLevelMessage(data1));
		MessageSender.sendMessage(new TemperatureNormalLevelMessage());
	}
}
