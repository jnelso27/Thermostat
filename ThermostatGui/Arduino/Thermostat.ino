// ***************************************************************************
// 
// Thermostat - The main Thermostat sketch
//
// ***************************************************************************

//Arduino libraries
#include "Wire.h"                //library to communicate with the i2c TMP102 sensor 

//Project libraries
#include "MessageReceiver.h"

//Serial port
#define SERIAL_RECEIVER Serial1  //Serial port for the host-to-client connection

//Size of expected messages
const int MSG_SIZE = 7;

MessageReceiver m;

//
// Arduino function for setting up the basic items on startup
// 
// For the Thermostat application, 
//
void setup() 
{   
  //Startup serial port and i2c communications
  SERIAL_RECEIVER.begin(9600);    
  Wire.begin();
}

//
// Arduino function for constantly looping during execution
//
// Function loops, waiting for the MSG_SIZE bytes to be received
// Once they are received, a call to receiveMessage() occurs to
// process the received bytes.
//
void loop() 
{    
  //Check to see if there is a message available
  if(SERIAL_RECEIVER.available() >= MSG_SIZE)
  {    
    m.receiveMessage();
  }
}
