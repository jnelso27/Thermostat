// ***************************************************************************
//
// MessageProcessor.cpp - Library used for processing incoming messages from
// the host-side controller of the Thermostat System.
// 
// ***************************************************************************

//Project libraries
#include "MessageProcessor.h"

//
// Default Constructor
//
MessageProcessor::MessageProcessor()
{
  //Do Nothing
}

//
// Function used to process the messages.
//
void MessageProcessor::processMessage(byte message[])
{  
  if(message[REC_MSG_TYPE_NDX] == DEFAULT_MSG)
  {
   Serial.println("A message of type DEFAULT_MSG has been received.");
   Serial.println("The DEFAULT_MSG type should not be sent.");
   Serial.println("");
  }  
  else if(message[REC_MSG_TYPE_NDX] == TEMP_SENSOR_READING_REQUEST_MSG)
  {        
    tmp102sensor.getTemperatureReading();
  }  
  else if(message[REC_MSG_TYPE_NDX] == NORMAL_ALARM_SET_MSG)
  {        
    alarmSensor.activateNormalAlarm();
    alarmSensor.deactivateWarningAlarm();
    alarmSensor.deactivateDangerAlarm();     
  }
  else if(message[REC_MSG_TYPE_NDX] == WARNING_ALARM_SET_MSG)
  {        
    alarmSensor.activateWarningAlarm();
    alarmSensor.deactivateNormalAlarm();
    alarmSensor.deactivateDangerAlarm();    
  }
  else if(message[REC_MSG_TYPE_NDX] == DANGER_ALARM_SET_MSG)
  {    
    alarmSensor.activateDangerAlarm();
    alarmSensor.deactivateNormalAlarm();
    alarmSensor.deactivateWarningAlarm();    
  }
  else
  {
    Serial.println("An Unknown Message has been received");
  }
}

