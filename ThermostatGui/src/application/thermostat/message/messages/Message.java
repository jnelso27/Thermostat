package application.thermostat.message.messages;

import application.thermostat.crc.CRCGenerator;

/***
 * Class Description
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public abstract class Message
{
	/** Character representing the start of the message */
	public static byte messageHeader = '&';

	/** Character representing the end of the message */
	public static byte messageFooter = '$';

	/** Message Indexes */
	public static final int REC_MSG_HEADER_NDX = 0;
	public static final int REC_MSG_TYPE_NDX = 1;
	public static final int REC_MSG_DATA_MSB_NDX = 2;
	public static final int REC_MSG_DATA_LSB_NDX = 3;
	public static final int REC_MSG_CRCBYTE1_NDX = 4;
	public static final int REC_MSG_CRCBYTE2_NDX = 5;
	public static final int REC_MSG_FOOTER_NDX = 6;

	/** Size of the message */
	public static final int MESSAGE_SIZE = 7;

	/** Indexes used for message */
	private final int MSG_MSB = 0;
	private final int MSG_LSB = 1;
	private final int BYTE_SIZE_IN_BITS = 8;

	/** Byte array that represents the full message */
	private byte message[] = {'0','0','0','0','0','0','0'};

	/** Payload portion of the message
	 *  Byte[0] = Message Type
	 *  Byte[1] = MSB of the MSG Data
	 *  Byte[2] = LSB of the MSG Data
	 */
	private byte messagePayload[] = {'0','0','0'};

	/**
	 * Default Constructor
	 */
	public Message()
	{
		//Do nothing
	}

	/**
	 * Overloaded Constructor
	 * Used to construct a message object for sending
	 *
	 * @param messageType The type of message to construct
	 * @param messageData The message data. Should be a byte array of size 2.
	 */
	public Message(byte messageType, byte[] messageData)
	{
		this.message[REC_MSG_HEADER_NDX] = messageHeader;
		this.message[REC_MSG_TYPE_NDX] = messageType;
		this.message[REC_MSG_DATA_MSB_NDX] = messageData[MSG_MSB];
		this.message[REC_MSG_DATA_LSB_NDX] = messageData[MSG_LSB];
		this.message[REC_MSG_FOOTER_NDX] = messageFooter;

		//Save the message payload portion for passing to the CRC Generator
		this.messagePayload[0] = this.message[REC_MSG_TYPE_NDX];
		this.messagePayload[1] = this.message[REC_MSG_DATA_MSB_NDX];
		this.messagePayload[2] = this.message[REC_MSG_DATA_LSB_NDX];

		//Calculate the CRC and add to the appropriate message fields
		int crc = CRCGenerator.calculateCRCCCITTXModem(messagePayload);

		//Add CRC to the message
		this.message[REC_MSG_CRCBYTE1_NDX] = (byte) (crc >> BYTE_SIZE_IN_BITS);	//Get first byte from the integer
		this.message[REC_MSG_CRCBYTE2_NDX] = (byte) (crc);		//Get second byte from the integer
	}

	/**
	 * Method used to get the message byte array
	 *
	 * @return
	 */
	public byte[] getMessageBytes()
	{
		return message;
	}

	/**
	 * Method used to obtain the type of the message
	 *
	 * @return
	 */
	public byte getMessageType()
	{
		return message[REC_MSG_TYPE_NDX];
	}

	/**
	 * Method used to obtain the Most Significant Bit of the Message
	 *
	 * @return
	 */
	public byte getMessageMSB()
	{
		return message[REC_MSG_DATA_MSB_NDX];
	}

	/**
	 * Method used to obtain the Least Significant Bit of the Message
	 *
	 * @return
	 */
	public byte getMessageLSB()
	{
		return message[REC_MSG_DATA_LSB_NDX];
	}

	/**
	 * Method used to return the message as a String
	 */
	public String toString()
	{
		StringBuilder messageToString = new StringBuilder();

		messageToString.append((char) message[REC_MSG_HEADER_NDX]);
		messageToString.append((char) message[REC_MSG_TYPE_NDX]);
		messageToString.append((char) message[REC_MSG_DATA_MSB_NDX]);
		messageToString.append((char) message[REC_MSG_DATA_LSB_NDX]);
		messageToString.append((char) message[REC_MSG_FOOTER_NDX]);

		return messageToString.toString();
	}
}