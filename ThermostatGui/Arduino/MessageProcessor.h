// ***************************************************************************
//
// MessageProcessor.h - Library used for processing incoming messages from
// the host-side controller of the Thermostat System.
// 
// ***************************************************************************
#ifndef MessageProcessor_h
#define MessageProcessor_h

//Arduino libraries
#include "Arduino.h"

//Project libaries
#include "TemperatureSensor.h"
#include "AlarmSensor.h"

class MessageProcessor
{
  public:
    MessageProcessor();
    void processMessage(byte message[]);
  private:   
    TemperatureSensor tmp102sensor;
    AlarmSensor alarmSensor;
    
    //
    const byte REC_MSG_HEADER = '&';      
    const byte REC_MSG_TYPE = '0';        
    const byte REC_MSG_DATA_MSB = '0';    
    const byte REC_MSG_DATA_LSB = '0';    
    const byte REC_MSG_FOOTER = '$';      
    
    const int REC_MSG_HEADER_NDX = 0;      
    const int REC_MSG_TYPE_NDX = 1;        
    const int REC_MSG_DATA_MSB_NDX = 2;          
    const int REC_MSG_DATA_LSB_NDX = 3;    
    //const int REC_MSG_FOOTER_NDX = 4;      
         
     //Message used as a default message type
    const byte DEFAULT_MSG = 0x50;    
    const byte TEMP_SENSOR_READING_REQUEST_MSG = 0x56;    
    const byte NORMAL_ALARM_SET_MSG = 0x53;	    
    const byte WARNING_ALARM_SET_MSG = 0x54;    
    const byte DANGER_ALARM_SET_MSG = 0x55; 
};

#endif
