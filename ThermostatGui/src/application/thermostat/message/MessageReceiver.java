package application.thermostat.message;

import java.util.LinkedList;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import application.thermostat.crc.CRC16;
import application.thermostat.message.processor.MessageProcessor;
import application.thermostat.sensors.Sensor;

/***
 * Class used to receive a message
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class MessageReceiver
{
	/** Instance of the SerialPort Object */
	private static SerialPort serialPort;

	/** COM Port name */
	private String comPort = "COM7";

	/** Default Baud Rate */
	private int baudRate = 9600;

	/** Default Data Bits */
	private int dataBits = 8;

	/** Default Stop Bits */
	private int stopBits = 1;

	/** Default Parity Bits */
	private int parityBits = 0;

	/** Variable Description */
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
			serialPort.setParams(baudRate, dataBits, stopBits, parityBits);
			serialPort.setEventsMask(mask);
			serialPort.addEventListener(new SerialPortReader(this.sensorSuite));

			System.out.println("Message Reciever Initialized Successfully"); //DEBUG Message
		}
		catch(SerialPortException ex)
		{
			System.out.println("The exception: " + ex + "occurred."); //Add a logger
		}
	}

	/**
	 * Method Description
	 *
	 * @param sensorSuite
	 * @param comPort
	 */
	public MessageReceiver(LinkedList<Sensor> sensorSuite, String comPort)
	{
		this.sensorSuite = sensorSuite;
		serialPort = new SerialPort(comPort);

		//Set the mask
		int mask = SerialPort.MASK_RXCHAR;

		try
		{
			serialPort.openPort();
			serialPort.setParams(baudRate, dataBits, stopBits, parityBits);
			serialPort.setEventsMask(mask);
			serialPort.addEventListener(new SerialPortReader(this.sensorSuite));

			System.out.println("Message Reciever Initialized Successfully"); //DEBUG Message
		}
		catch(SerialPortException ex)
		{
			System.out.println("The exception: " + ex + "occurred."); //Add a logger
		}
	}

	/**
	 * Method used to obtain the serial port
	 *
	 * @return
	 */
	public static SerialPort getSerialPortInstance()
	{
		return serialPort;
	}

	/**
	 * Method used to obtain the name of the COM port
	 *
	 * @return the comPort
	 */
	public String getComPort()
	{
		return comPort;
	}

	/**
	 * Method used to set the name of the COM port
	 *
	 * @param comPort the comPort to set
	 */
	public void setComPort(String comPort)
	{
		this.comPort = comPort;
	}

	/**
	 * Method used to obtain the currently set baud rate
	 *
	 * @return the baudRate
	 */
	public int getBaudRate()
	{
		return baudRate;
	}

	/**
	 * Method used to set the baud rate
	 *
	 * @param baudRate the baudRate to set
	 */
	public void setBaudRate(int baudRate)
	{
		this.baudRate = baudRate;
	}

	/**
	 * Method used to obtain the currently set data bits
	 *
	 * @return the dataBits
	 */
	public int getDataBits()
	{
		return dataBits;
	}

	/**
	 * Method used to set the data bits for the serial port
	 *
	 * @param dataBits the dataBits to set
	 */
	public void setDataBits(int dataBits)
	{
		this.dataBits = dataBits;
	}

	/**
	 * Method used to obtain the currently set stop bits for the serial port
	 *
	 * @return the stopBits
	 */
	public int getStopBits()
	{
		return stopBits;
	}

	/**
	 * Method used to set the number of stop bits for the serial port
	 *
	 * @param stopBits the stopBits to set
	 */
	public void setStopBits(int stopBits)
	{
		this.stopBits = stopBits;
	}

	/**
	 * Method used to obtain the number of currently set parity bits of the serial port
	 *
	 * @return the parityBits
	 */
	public int getParityBits()
	{
		return parityBits;
	}

	/**
	 * Method used to set the number of parity bits for the serial port
	 *
	 * @param parityBits the parityBits to set
	 */
	public void setParityBits(int parityBits)
	{
		this.parityBits = parityBits;
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

			//Convert Received Message CRC to integer for comparison
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
	 * Class Description
	 *
	 * @author J Nelson
	 *
	 */
	static class SerialPortReader implements SerialPortEventListener
	{
		/** Variable Description */
		boolean msgValidity;

		/** Variable Description */
		MessageProcessor msgProcessor;

		/** Variable Description */
		LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

		/**
		 * Method Description
		 *
		 * @param sensorSuite
		 */
		public SerialPortReader(LinkedList<Sensor> sensorSuite)
		{
			this.sensorSuite = sensorSuite;

			msgProcessor = new MessageProcessor(this.sensorSuite);
		}

		/**
		 * Method Description
		 */
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
