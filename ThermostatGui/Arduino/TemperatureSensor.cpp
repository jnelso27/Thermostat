// ***************************************************************************
//
// TemperatureSensor.cpp - Library representing the TMP102 i2C temperature
// sensor. Contains the necessary functions for communication with the sensor
// connected to the Thermostat system.
// 
// ***************************************************************************

//Arduino libraries
//#include "Arduino.h"

//Project libraries
#include "TemperatureSensor.h"

// Number of bytes to request from the TMP102 temp sensor
const int BYTES_TO_REQUEST = 2;

// Message Header/Footer characters
const byte MSG_HEADER = '&';      
const byte MSG_FOOTER = '$';   

//Message type for the 
const byte TEMP_SENSOR_READING_MSG = 0x52;       

//
// Default Constructor
//
TemperatureSensor::TemperatureSensor()
{
  tmp102Address = 0x48;
}

//
// Function used to obtain the temperature reading from the
// TMP102 i2c enabled temperature sensor.
//
// Function uses the Arduino Wire library to request 2 bytes
// from the sensor (per datasheet).
//
void TemperatureSensor::getTemperatureReading()
{
  Wire.requestFrom(tmp102Address, BYTES_TO_REQUEST); 
  
  byte msb = Wire.read();  //Read the most significant byte
  byte lsb = Wire.read();  //Read the least significant byte

  msb &= 0xFF;
  lsb &= 0xFF;
  
  //create payload for CRC calculation
  byte messagePayload[] = {TEMP_SENSOR_READING_MSG, msb, lsb};  

  //Calculate the CRC and add to the appropriate message fields
  int crcValue = crc.calculateCRCCITTXModem(messagePayload);
  
  Serial.println("Calculated CRC: ");
  Serial.print(crcValue);
  Serial.println("");
  
  //Build bytes for sending to host based off of CRC  
  byte crcMSBByte = (byte) (crcValue >> 8);  //get first byte from the CRC integer
  byte crcLSBByte = (byte) (crcValue);	 //get second byte from the CRC integer
  
  //Build message for sending to control application
  byte message[] = {MSG_HEADER, TEMP_SENSOR_READING_MSG, msb, lsb, crcMSBByte, crcLSBByte, MSG_FOOTER};

  //Send the message
  messageSender.sendMessage(message);  
}
