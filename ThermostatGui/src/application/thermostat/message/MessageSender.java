package application.thermostat.message;

import jssc.SerialPort;
import jssc.SerialPortException;

/***
 * Class Description
 *
 * @author Joshua Nelson
 *
 */
public class MessageSender
{
	//Serial Port used to send messages
	private static SerialPort serialPort;

	/**
	 * Default Constructor
	 */
	public MessageSender()
	{
		serialPort = MessageReceiver.getSerialPortInstance();
	}

	/**
	 * Constructor
	 *
	 * @param baud The baud rate to use for the serial communication
	 * @param data_bits The number of data bits to use for the serial communication
	 * @param stop_bits The number of stop bits to use for the serial communication
	 * @param parity The number of parity bits to use
	 */
	public MessageSender(int baud, int data_bits, int stop_bits, int parity)
	{
		//Future Implementation
	}

	/**
	 * Used to send a Message over the configured Serial Port
	 *
	 * @param messageToSend
	 */
	public static void sendMessage(Message messageToSend)
	{
		System.out.println("Sending Message");

		try
		{
			//Attempt to send the Message
			serialPort.writeBytes(messageToSend.getMessageBytes());
		}
		catch(SerialPortException ex)
		{
			System.out.println("The Exception: " + ex + "Has Occurred");
		}
	}
}
