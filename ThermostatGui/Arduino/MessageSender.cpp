// ***************************************************************************
//
// MessageSender.cpp - Library for sending a message of a constant size
// from the client to the host.
// 
// ***************************************************************************

//Arduino libraries
//#include "Arduino.h"

//Project libraries
#include "MessageSender.h"

#define SERIAL_RECEIVER Serial1

//
// Default Constructor
//
MessageSender::MessageSender()
{
  //Do Nothing
}

//
// Function used to send a message from the client to the host
//
void MessageSender::sendMessage(byte message[])
{
  //msg_header = SERIAL_RECEIVER.write(message, MSG_SIZE);
  SERIAL_RECEIVER.write(message, MSG_SIZE);
}
