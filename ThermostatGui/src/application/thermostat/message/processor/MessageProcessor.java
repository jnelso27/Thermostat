package application.thermostat.message.processor;

import java.util.LinkedList;

import application.thermostat.message.messages.Message;
import application.thermostat.message.messages.MessageType;
import application.thermostat.sensors.Sensor;

/***
 * Class is used to process incoming messages from the thermostat
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class MessageProcessor
{
	/** Variable Description */
	private LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

	/** Variable Description */
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
			System.out.println("Received a TEMP_SENSOR_READING_MSG");
			sensor = sensorSuite.get(0);
			sensor.processSensorData(serialMessage);
		}
		else
		{
			System.out.println("An unknown message was received. Unable to Process! " + serialMessage.toString());
		}
	}
}
