// ***************************************************************************
//
// CRCGenerator.h - Library used for generating CRC for the Thermostat
// system.
// 
// ***************************************************************************
#ifndef CRCGenerator_h
#define CRCGenerator_h

//Arduino libraries
#include "Arduino.h"

class CRCGenerator
{
  public:
    CRCGenerator();
    int calculateCRCCITTXModem(byte message[]);
  private:    
};

#endif
