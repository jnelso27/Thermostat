package application.thermostat.message;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import application.ThermostatGUI;
import application.thermostat.crc.CRCGenerator;
import application.thermostat.message.messages.Message;
import application.thermostat.message.processor.MessageProcessor;
import application.thermostat.sensors.Sensor;

/***
 * Class used to receive messages on the configured serial port
 * for the Thermostat system.
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
	private LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

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
			serialPort.addEventListener(new SerialPortMessageListener(this.sensorSuite));
		}
		catch(SerialPortException ex)
		{
			//Replace with a Logger message here in a future version...
			System.out.println("The exception: " + ex + "occurred.");
		}
	}

	/**
	 * Overloaded Constructor
	 *
	 * @param sensorSuite List of configured sensors as loaded from the XML configuration settings file.
	 * @param comPort The COM port to receive messages on.
	 */
	public MessageReceiver(LinkedList<Sensor> sensorSuite, String comPort)
	{
		this.sensorSuite = sensorSuite;
		this.comPort = comPort;
		serialPort = new SerialPort(comPort);

		//Set the mask
		int mask = SerialPort.MASK_RXCHAR;

		try
		{
			serialPort.openPort();
			serialPort.setParams(baudRate, dataBits, stopBits, parityBits);
			serialPort.setEventsMask(mask);
			serialPort.addEventListener(new SerialPortMessageListener(this.sensorSuite));

			//Replace with a Logger message here in a future version...
			System.out.println("Message Reciever Initialized Successfully");
		}
		catch(SerialPortException ex)
		{
			//Replace with a Logger message here in a future version...
			System.out.println("The exception: " + ex + "occurred.");
		}
	}

	/**
	 * Method used to obtain the serial port instance.
	 *
	 * @return The SerialPort instance.
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
	 * @return The number of configured data bits for the serial port.
	 */
	public int getDataBits()
	{
		return dataBits;
	}

	/**
	 * Method used to set the data bits for the serial port
	 *
	 * @param dataBits The number of data bits to configure for the serial port.
	 */
	public void setDataBits(int dataBits)
	{
		this.dataBits = dataBits;
	}

	/**
	 * Method used to obtain the currently set stop bits for the serial port
	 *
	 * @return The number of configured stop bits for the serial port.
	 */
	public int getStopBits()
	{
		return stopBits;
	}

	/**
	 * Method used to set the number of stop bits for the serial port
	 *
	 * @param stopBits The number of stop bits to configure for the serial port.
	 */
	public void setStopBits(int stopBits)
	{
		this.stopBits = stopBits;
	}

	/**
	 * Method used to obtain the number of currently set parity bits of the serial port
	 *
	 * @return The number of configured parity bits for the serial port.
	 */
	public int getParityBits()
	{
		return parityBits;
	}

	/**
	 * Method used to set the number of parity bits for the serial port
	 *
	 * @param parityBits The number of parity bits to configure for the serial port.
	 */
	public void setParityBits(int parityBits)
	{
		this.parityBits = parityBits;
	}

	/**
	 * Method used to check the validity of the Serial Message
	 *
	 * @param receivedMessage The message that has been received on the serial port.
	 * @return msgIsValid True/false as to whether the message has passed the CRC.
	 */
	public static boolean verifyMessageValidity(byte[] receivedMessage)
	{
		//Assume message is not valid
		boolean msgIsValid = false;

		//Validate the header and footer
		if(receivedMessage[Message.REC_MSG_HEADER_NDX] == Message.messageHeader &&
				receivedMessage[Message.REC_MSG_FOOTER_NDX] == Message.messageFooter)
		{
			byte[] payload = new byte[Message.PAYLOAD_SIZE];

			//Build Payload from the received message
			for(int i=1; i<4; i++)
			{
				payload[i-1] = receivedMessage[i];
			}

			//Calculate the CRC of the message
			int calculatedMessageCRC = CRCGenerator.calculateCRCCCITTXModem(payload);

			//Convert Received Message CRC to integer for comparison
			int receivedMessageCRC = receivedMessage[Message.REC_MSG_CRCBYTE1_NDX] &0xFF;
			receivedMessageCRC <<= 8;	//shift one byte
			receivedMessageCRC |= receivedMessage[Message.REC_MSG_CRCBYTE2_NDX] & 0xFF;

			//Compare the calculated CRC with received CRC and
			//set msgIsValide to true if they are equal.
			if(calculatedMessageCRC == receivedMessageCRC)
			{
				msgIsValid = true;
			}
		}

		return msgIsValid;
	}

	/**
	 * SerialPortEventListener implementation for receiving messages on the serial port.
	 *
	 * @author J Nelson
	 *
	 */
	public static class SerialPortMessageListener implements SerialPortEventListener
	{
		/** Variable Description */
		boolean msgValidity;

		/** Variable Description */
		MessageProcessor msgProcessor;

		/** Variable Description */
		LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

		/** Period to wait for message before assuming an error has occurred */
		private int receivedMessageTimeOutPeriod = 5;

		/** Callable object used as a timer */
		Callable messageNotReceivedTask = new Callable()
		{
			@Override
			public Object call() throws Exception
			{
				ThermostatGUI.showErrorDialog("No messages received in the last 30 seconds");
				return "Message Not Received in the last 30 seconds!";
			}
		};

		/** Threadpool for power disruption service thread */
		ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);

		/** Variable Description */
		ScheduledFuture sf = scheduledPool.schedule(messageNotReceivedTask, receivedMessageTimeOutPeriod, TimeUnit.SECONDS);

		/**
		 * Method Description
		 *
		 * @param sensorSuite
		 */
		public SerialPortMessageListener(LinkedList<Sensor> sensorSuite)
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
	        	//Message has been received so restart the "timer"
	        	sf.cancel(true);
	        	sf = scheduledPool.schedule(messageNotReceivedTask, receivedMessageTimeOutPeriod, TimeUnit.SECONDS);

	            if(event.getEventValue() == Message.MESSAGE_SIZE)
	            {
	                try
	                {
	                	//Read the bytes from the serial port
	                	final byte buffer[] = serialPort.readBytes(event.getEventValue());

	                	//Validate the message integrity
	                	msgValidity = verifyMessageValidity(buffer);

	                	try
	                	{
		                	if(msgValidity)
		                	{
		                		//Add code here to reset the task

		                		msgProcessor.processMessage(buffer);
		                	}
		                	else
		                	{
		                		//Add Logger statement here in a future update.
		                	}
	                	}
	                	catch(Exception e)
	                	{
	                		//Add Logger statement here in a future update.
	                		System.out.println("An exception occurred: " + e);
	                	}
	                }
	                catch (SerialPortException ex)
	                {
	                	//Add Logger statement here in a future update.
	                	System.out.println("An exception occurred: " + ex);
	                }

	            } //end if if(event.getEventValue() == Message.MESSAGE_SIZE)
	        } //end if(event.isRXCHAR())
	    } // end serialEvent()
	} // end class SerialPortMessageListener
} // class MessageReceiver
