// ***************************************************************************
//
// AlarmSensor.h - Library representing the three different alarm indicators
// of the Thermostat system.
//
// Contains functions to turn on (activateXAlarm()) and off (deactivateXAlarm())
// as well as constants for the pin numbers.
// 
// ***************************************************************************
#ifndef AlarmSensor_h
#define AlarmSensor_h

//Arduino libraries
#include "Arduino.h"
#include "Wire.h"

class AlarmSensor
{
  public:
    AlarmSensor();
    void activateNormalAlarm();
    void deactivateNormalAlarm();
    void activateWarningAlarm();
    void deactivateWarningAlarm();
    void activateDangerAlarm();
    void deactivateDangerAlarm();
  private:    
    const int NORMAL_ALARM_PIN_NUM = 14;
    const int WARNING_ALARM_PIN_NUM = 15;
    const int DANGER_ALARM_PIN_NUM = 16;
};

#endif
