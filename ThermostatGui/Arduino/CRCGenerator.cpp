// ***************************************************************************
//
// CRCGenerator.cpp - Library used for generating CRC for the Thermostat
// system.
// 
// ***************************************************************************

//Project libraries
#include "CRCGenerator.h"

//
// Default Constructor
//
CRCGenerator::CRCGenerator()
{
  //Do Nothing
}

//
// Function used to calculate a 16-bit CRC
//
int CRCGenerator::calculateCRCCITTXModem(byte message[])
{
  int POLYNOMIAL = 0x1021;
  int PRESET_VALUE = 0x0000;
  
  int remainder = PRESET_VALUE;
    
  for(int i=0; i<3; i++)
  {    
    remainder ^= message[i] <<8;
    
    for (int j = 0; j < 8; j++)
    {
      if ((remainder & 0x8000) != 0)
      {
      remainder = (remainder << 1) ^ POLYNOMIAL;
      }
      else
      {
        remainder = remainder << 1;
      }
    
      remainder &= 0xFFFF;
    }
  }  
  return remainder;
}

