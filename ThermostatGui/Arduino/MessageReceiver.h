// ***************************************************************************
//
// MessageReceiver.h - Library for receiving messages from the host
// 
// ***************************************************************************
#ifndef MessageReceiver_h
#define MessageReceiver_h

//Arduino libraries
#include "Arduino.h"

//Project libraries
#include "CRCGenerator.h"
#include "MessageProcessor.h"

class MessageReceiver
{
  public:
   MessageReceiver();   
   void receiveMessage();
  private:    
    CRCGenerator crc;
    MessageProcessor messageProcessor;
    byte msg_header = '0';
    byte msg_type = '0';
    byte msg_data_msb = '0';
    byte msg_data_lsb = '0';
    byte msg_crcbyte1 = '0';
    byte msg_crcbyte2 = '0';
    byte msg_footer = '0';
    const byte MSG_HEADER = (byte)'&';
};

#endif
