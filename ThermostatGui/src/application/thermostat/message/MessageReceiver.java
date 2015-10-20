package application.thermostat.message;

import java.util.LinkedList;

import application.thermostat.crc.CRC16;
import application.thermostat.message.processor.MessageProcessor;
import application.thermostat.sensors.Sensor;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/***
 * Class used to receive a message
 *
 * @author Joshua Nelson
 *
 */
public class MessageReceiver
{
	//Instance of the SerialPort Object
	private static SerialPort serialPort;

	//COM Port name
	private String comPort = "COM7";

	//Default Baud Rate
	private final int baud_rate = 9600;

	//Default Data Bits
	private final int data_bits = 8;

	//Default Stop Bits
	private final int stop_bits = 1;

	//Default Parity Bits
	private final int parity_bits = 0;

	//
	LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

	/**
	 * Constructor
	 */
	public MessageReceiver(LinkedList<Sensor> sensorSuite)
	{
		this.sensorSuite = sensorSuite;
		serialPort = new SerialPort(comPort);

		//Set the mask
		int mask = SerialPort.MASK_RXCHAR;

		try
		{
			serialPort.openPort();
			serialPort.setParams(baud_rate, data_bits, stop_bits, parity_bits);
			serialPort.setEventsMask(mask);
			serialPort.addEventListener(new SerialPortReader(this.sensorSuite));

			System.out.println("Message Reciever Initialized Successfully"); //DEBUG Message
		}
		catch(SerialPortException ex)
		{
			System.out.println("The exception: " + ex + "occurred."); //Add a logger
		}
	}

	public MessageReceiver(LinkedList<Sensor> sensorSuite, String comPort)
	{
		this.sensorSuite = sensorSuite;
		serialPort = new SerialPort(comPort);

		//Set the mask
		int mask = SerialPort.MASK_RXCHAR;

		try
		{
			serialPort.openPort();
			serialPort.setParams(baud_rate, data_bits, stop_bits, parity_bits);
			serialPort.setEventsMask(mask);
			serialPort.addEventListener(new SerialPortReader(this.sensorSuite));

			System.out.println("Message Reciever Initialized Successfully"); //DEBUG Message
		}
		catch(SerialPortException ex)
		{
			System.out.println("The exception: " + ex + "occurred."); //Add a logger
		}
	}

	public static SerialPort getSerialPortInstance()
	{
		return serialPort;
	}

	/**
	 * Constructor - Future Implementation
	 *
	 * @param baud
	 * @param data_bits
	 * @param stop_bits
	 * @param parity
	 */
	public MessageReceiver(int baud, int data_bits, int stop_bits, int parity)
	{
		//Future Implementation
	}

	/**
	 * Method used to check the validity of the Serial Message
	 *
	 * @param receivedMessage
	 * @return msgIsValid
	 */
	public static boolean verifyMessageValidity(byte[] receivedMessage)
	{
		//Set message validity to false initially
		boolean msgIsValid = false;

		//
		if(receivedMessage[Message.REC_MSG_HEADER_NDX] == Message.messageHeader && receivedMessage[Message.REC_MSG_FOOTER_NDX] == Message.messageFooter)
		{
			byte[] payload = new byte[3];

			//Get Payload
			for(int i=1;i<4;i++)
			{
				payload[i-1] = receivedMessage[i];
			}
			int calculatedMessageCRC = CRC16.calculateCRCCCITTXModem(payload);

			System.out.println("calculatedMessageCRC: "+ calculatedMessageCRC);

			//Convert Received Message CRC to int for comparison
			int receivedMessageCRC = receivedMessage[Message.REC_MSG_CRCBYTE1_NDX] &0xFF;
			receivedMessageCRC <<= 8;
			receivedMessageCRC |= receivedMessage[Message.REC_MSG_CRCBYTE2_NDX] & 0xFF;

			System.out.println("receivedMessageCRC: "+ receivedMessageCRC);

			if(calculatedMessageCRC == receivedMessageCRC)
			{
				msgIsValid = true;
			}
		}

		return msgIsValid;
	}

	/**
	 * Method
	 *
	 * @author DeveloperMain
	 *
	 */
	static class SerialPortReader implements SerialPortEventListener
	{
		//
		boolean msgValidity;

		//
		MessageProcessor msgProcessor;

		//
		LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

		public SerialPortReader(LinkedList<Sensor> sensorSuite)
		{
			this.sensorSuite = sensorSuite;

			msgProcessor = new MessageProcessor(this.sensorSuite);
		}

	    public void serialEvent(SerialPortEvent event)
	    {
	        if(event.isRXCHAR())
	        {
	            if(event.getEventValue() == Message.MESSAGE_SIZE)
	            {
	                try
	                {
	                	final byte buffer[] = serialPort.readBytes(event.getEventValue());

	                	msgValidity = verifyMessageValidity(buffer);

	                	try
	                	{
		                	if(msgValidity)
		                	{
		                		System.out.println("Received Message with Good CRC. Processing Msg");
		                		msgProcessor.processMessage(buffer);
		                	}
		                	else
		                	{
		                		System.out.println("Received Message with Bad CRC");
		                	}
	                	}
	                	catch(Exception e)
	                	{
	                		System.out.println("An exception occurred: " + e);
	                	}
	                }
	                catch (SerialPortException ex)
	                {
	                	System.out.println("An exception occurred: " + ex);
	                }
	            }
	        }
	        //If the CTS line status has changed, then the method event.getEventValue() returns 1 if the line is ON and 0 if it is OFF.
	        else if(event.isCTS())
	        {
	            if(event.getEventValue() == 1)
	            {
	                System.out.println("CTS - ON");
	            }
	            else
	            {
	                System.out.println("CTS - OFF");
	            }
	        }
	        else if(event.isDSR())
	        {
	            if(event.getEventValue() == 1)
	            {
	                System.out.println("DSR - ON");
	            }
	            else
	            {
	                System.out.println("DSR - OFF");
	            }
	        }
	    }
	}
}
