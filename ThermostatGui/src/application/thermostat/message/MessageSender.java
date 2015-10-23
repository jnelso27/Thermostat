package application.thermostat.message;

import application.thermostat.log.ThermostatLogger;
import jssc.SerialPort;
import jssc.SerialPortException;

/***
 * Class which is used to send RS232 messages from a host platform (laptop)
 * to the Teensy 3.1 microcontroller
 *
 * Date of Last Change: 2015-10-23
 *
 * @author J Nelson
 *
 */
public class MessageSender
{
	/** Serial Port used to send messages */
	private static SerialPort serialPort;

	/**
	 * Default Constructor
	 */
	public MessageSender()
	{
		serialPort = MessageReceiver.getSerialPortInstance();
	}

	/**
	 * Used to send a Message over the configured Serial Port
	 *
	 * @param messageToSend The message to send over the serial port to the Microcontroller
	 */
	public static void sendMessage(Message messageToSend)
	{
		try
		{
			//Attempt to send the Message
			serialPort.writeBytes(messageToSend.getMessageBytes());
		}
		catch(SerialPortException ex)
		{
			ThermostatLogger.logger.severe("The Exception: " + ex + "Has Occurred");
		}
	}
}
