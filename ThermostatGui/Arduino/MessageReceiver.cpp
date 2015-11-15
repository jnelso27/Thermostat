// ***************************************************************************
//
// MessageReceiver.cpp - Library for receiving messages from the host
// 
// ***************************************************************************

//Arduino libraries
//#include "Arduino.h"

//Project libraries
#include "MessageReceiver.h"

//Serial port to receive messages on
#define SERIAL_RECEIVER Serial1

//
// Default Constructor
//
MessageReceiver::MessageReceiver()
{
  //Do Nothing
}

//
// Function used to receive a message from the host.
//
// This function reads of the bytes received (message), calculates
// the CRC of the message and sends it to the message processor to
// take action.
//
void MessageReceiver::receiveMessage()
{
   //Read the first byte in the serial buffer
   msg_header = SERIAL_RECEIVER.read();
   
   //Verify that the byte matches the message header,
   //if so, then read the rest of the bytes to build the message
   if(msg_header == MSG_HEADER)
   {
      msg_type = SERIAL_RECEIVER.read();
      msg_data_msb = SERIAL_RECEIVER.read();
      msg_data_lsb = SERIAL_RECEIVER.read();
      msg_crcbyte1 = SERIAL_RECEIVER.read();
      msg_crcbyte2 = SERIAL_RECEIVER.read();
      msg_footer = SERIAL_RECEIVER.read();
      
      //Build full message to send it for processing
      byte received_msg[] = {msg_header, msg_type, msg_data_msb, msg_data_lsb, msg_crcbyte1, msg_crcbyte2,  msg_footer};
            
      //Build the payload to calculate the CRC
      byte payload[] = {msg_type, msg_data_msb, msg_data_lsb};
      
      //Calculate the CRC
      int calculatedMessageCRC = crc.calculateCRCCITTXModem(payload);
      
      //Convert Received Message CRC to int for comparison
      int receivedMessageCRC = received_msg[4];
      receivedMessageCRC <<= 8;
      receivedMessageCRC |= received_msg[5] & 0xFF;

      if(calculatedMessageCRC == receivedMessageCRC)
      {
        Serial.println("Received properly formatted message with good CRC");
        messageProcessor.processMessage(received_msg);
      }
      else
      {
        Serial.println("Received improperly formatted message with a bad CRC");
        Serial.println(receivedMessageCRC);
        Serial.println(calculatedMessageCRC);
      }      
   }
   else
   {
      Serial.println("Message not of the right format!"); 
      Serial.println(msg_header);
      Serial.println();
   }
 }
