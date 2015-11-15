// ***************************************************************************
//
// MessageSender.h - Library for sending a message of a constant size
// from the client to the host.
// 
// ***************************************************************************
#ifndef MessageSender_h
#define MessageSender_h

//Arduino libraries
#include "Arduino.h"

class MessageSender
{
  public:
   MessageSender();
   void sendMessage(byte message[]);
  private:
    byte msg_header = '0';
    const int MSG_SIZE = 7;
};

#endif
