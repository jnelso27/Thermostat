package application.thermostat.message.processor;

import java.util.LinkedList;

import application.thermostat.message.messages.Message;
import application.thermostat.message.messages.MessageType;
import application.thermostat.sensors.Sensor;

/***
 * Class is used to process incoming messages from the thermostat
 *
 * Date of Last Change: 2015-11-15
 *
 * @author J Nelson
 *
 */
public class MessageProcessor
{
	/** Index of configured TMP102 Sensor */
	private static final int TMP102_SENSOR = 0;

	/** Linked list of sensors that are available for processing */
	private LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

	/** A default sensor */
	private Sensor sensor = new Sensor();

	/**
	 * Default Constructor
	 */
	public MessageProcessor(LinkedList<Sensor> sensorSuite)
	{
		this.sensorSuite = sensorSuite;
	}

	/**
	 * Method that is used to process the incoming messages
	 *
	 * @param serialMessage The byte array (message) to process
	 */
	public void processMessage(byte[] serialMessage)
	{
		if(serialMessage[Message.REC_MSG_TYPE_NDX] == MessageType.TEMP_SENSOR_READING_MSG)
		{
			sensor = sensorSuite.get(TMP102_SENSOR);
			sensor.processSensorData(serialMessage);
		}
		else
		{
			//Replace with a Logger message here in a future version...
		}
	}
}
