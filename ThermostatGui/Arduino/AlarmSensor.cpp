// ***************************************************************************
//
// AlarmSensor.cpp - Library representing the three different alarm indicators
// of the Thermostat system.
//
// Contains functions to turn on (activateXAlarm()) and off (deactivateXAlarm())
// as well as constants for the pin numbers.
// 
// ***************************************************************************

//Arduino libraries
#include "AlarmSensor.h"

//
// Default Constructor
//
AlarmSensor::AlarmSensor()
{
  //Setup Digital I/O Pins as Outputs for Alarms (LEDs)
  pinMode(NORMAL_ALARM_PIN_NUM, OUTPUT);
  pinMode(WARNING_ALARM_PIN_NUM, OUTPUT);
  pinMode(DANGER_ALARM_PIN_NUM, OUTPUT);
  
  //Deactivate all Alarms (LEDS) on startup
  deactivateNormalAlarm();
  deactivateWarningAlarm();
  deactivateDangerAlarm();
}

//
// Function used to activate the Normal Level Alarm
//
void AlarmSensor::activateNormalAlarm()
{
  digitalWrite(NORMAL_ALARM_PIN_NUM, HIGH);
}

//
// Function used to deactivate the Normal Level Alarm
//
void AlarmSensor::deactivateNormalAlarm()
{
  digitalWrite(NORMAL_ALARM_PIN_NUM, LOW);
}

//
// Function use to activate the Warning Level Alarm
//
void AlarmSensor::activateWarningAlarm()
{
  digitalWrite(WARNING_ALARM_PIN_NUM, HIGH);
}

//
// Function used to deactivate the Warning Level Alarm
//
void AlarmSensor::deactivateWarningAlarm()
{
  digitalWrite(WARNING_ALARM_PIN_NUM, LOW);
}

//
// Function used to activate the Danger Level Alarm
//
void AlarmSensor::activateDangerAlarm()
{
  digitalWrite(DANGER_ALARM_PIN_NUM, HIGH);
}

//
// Function used deactivate the Danger Level Alarm
//
void AlarmSensor::deactivateDangerAlarm()
{
  digitalWrite(DANGER_ALARM_PIN_NUM, LOW);
}
