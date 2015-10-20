package application.thermostat.message;

import java.nio.ByteBuffer;

import application.thermostat.crc.CRC16;

/***
 * Class Description
 *
 * @author Joshua
 *
 */
public abstract class Message
{
	//Character representing the start of the message
	public static byte messageHeader = '&';

	//Character representing the end of the message
	public static byte messageFooter = '$';

	//
	public byte messageType = MessageType.DEFAULT_MSG;

	//Message Indexes
	public static final int REC_MSG_HEADER_NDX = 0;
	public static final int REC_MSG_TYPE_NDX = 1;
	public static final int REC_MSG_DATA_MSB_NDX = 2;
	public static final int REC_MSG_DATA_LSB_NDX = 3;
	public static final int REC_MSG_CRCBYTE1_NDX = 4;
	public static final int REC_MSG_CRCBYTE2_NDX = 5;
	public static final int REC_MSG_FOOTER_NDX = 6;

	//Message Size
	public static final int MESSAGE_SIZE = 7;

	//
	public byte messageData[] = {'A','A'};

	//
	private byte message[] = {'0','0','0','0','0','0','0'};

	//
	private byte messagePayload[] = {'0','0','0'};


	/**
	 * Default Constructor
	 */
	public Message()
	{
		//Do nothing
	}

	/**
	 * Method
	 *
	 * @param messageType
	 * @param messageData
	 */
	public Message(byte messageType, byte[] messageData2)
	{
		this.message[REC_MSG_HEADER_NDX] = messageHeader;
		this.message[REC_MSG_TYPE_NDX] = messageType;
		this.message[REC_MSG_DATA_MSB_NDX] = messageData2[0];
		this.message[REC_MSG_DATA_LSB_NDX] = messageData2[1];
		this.message[REC_MSG_FOOTER_NDX] = messageFooter;

		//Save the message payload portion
		this.messagePayload[0] = this.message[REC_MSG_TYPE_NDX];
		this.messagePayload[1] = this.message[REC_MSG_DATA_MSB_NDX];
		this.messagePayload[2] = this.message[REC_MSG_DATA_LSB_NDX];

		//Calculate the CRC and add to the appropriate message fields
		int crc = CRC16.calculateCRCCCITTXModem(messagePayload);

		this.message[REC_MSG_CRCBYTE1_NDX] = (byte) (crc >> 8);	//Get first byte from the integer
		this.message[REC_MSG_CRCBYTE2_NDX] = (byte) (crc);		//Get second byte from the integer

	}

	/**
	 * Method
	 *
	 * @param messageType
	 * @param messageData2
	 * @return
	 */
	public byte[] buildMessage(byte messageType, byte[] messageData2)
	{
		this.message[REC_MSG_HEADER_NDX] = messageHeader;
		this.message[REC_MSG_TYPE_NDX] = messageType;
		this.message[REC_MSG_DATA_MSB_NDX] = messageData2[0];
		this.message[REC_MSG_DATA_LSB_NDX] = messageData2[1];
		this.message[REC_MSG_FOOTER_NDX] = messageFooter;

		//Save the message payload portion
		this.messagePayload[0] = this.message[REC_MSG_TYPE_NDX];
		this.messagePayload[1] = this.message[REC_MSG_DATA_MSB_NDX];
		this.messagePayload[2] = this.message[REC_MSG_DATA_LSB_NDX];

		//Calculate the CRC and add to the appropriate message fields
		int crc = CRC16.calculateCRCCCITTXModem(messagePayload);

		this.message[REC_MSG_CRCBYTE1_NDX] = (byte) (crc >> 8);	//Get first byte from the integer
		this.message[REC_MSG_CRCBYTE2_NDX] = (byte) (crc);		//Get second byte from the integer

		return message;
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