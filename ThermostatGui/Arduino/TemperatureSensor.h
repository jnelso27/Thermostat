// ***************************************************************************
//
// TemperatureSensor.h - Library representing the TMP102 i2C temperature
// sensor. Contains the necessary functions for communication with the sensor
// connected to the Thermostat system.
// 
// ***************************************************************************
#ifndef TemperatureSensor_h
#define TemperatureSensor_h

//Arduino libraries
#include "Arduino.h"
#include "Wire.h"

//Project libraries
#include "MessageSender.h"
#include "CRCGenerator.h"

class TemperatureSensor
{
  public:
    TemperatureSensor();    
    void getTemperatureReading();
  private:
    MessageSender messageSender;
    CRCGenerator crc;
    int tmp102Address;
};
#endif
