package application.thermostat.crc;

/**
 * Class Description (TODO)
 *
 * Date:
 *
 * @author J Nelson
 *
 */
public class CRC16
{
	/**
	 * Method to calculate 16-bit CRC based on Preset of 0x0000
	 * and polynomial of 0x1021 (XModem - NEED TO VERIFY)
	 *
	 * @param buffer
	 */
	public static int calculateCRCCCITTXModem(byte[] data)
	{
		final int POLYNOMIAL   = 0x1021;
		final int PRESET_VALUE = 0x0000;

		int rem = PRESET_VALUE;

		for (int i = 0; i < data.length; i++ )
		{
			rem ^= data[i] << 8;	//XOR databyte after AND'ing

			//For each bit in the byte
			for (int j = 0; j < 8; j++)
			{
				if ((rem & 0x8000) != 0)
				{
					rem = (rem << 1) ^ POLYNOMIAL;
				}
				else
				{
					rem = rem << 1;
				}

				rem &= 0xFFFF;
			}
		}

		return rem;
	}

	/**
	 * Method Description
	 *
	 * @param data
	 * @return
	 */
	public static int calculateCRCCCITTFFFF(byte[] data)
	{
		final int POLYNOMIAL   = 0x1021;
		final int PRESET_VALUE = 0xFFFF;

		int rem = PRESET_VALUE;

		for (int i = 0; i < data.length; i++ )
		{
			rem ^= data[i] << 8;	//XOR databyte after AND'ing

			//For each bit in the byte
			for (int j = 0; j < 8; j++)
			{
				if ((rem & 0x8000) != 0)
				{
					rem = (rem << 1) ^ POLYNOMIAL;
				}
				else
				{
					rem = rem << 1;
				}

				rem &= 0xFFFF;
			}
		}

		return rem;
	}

	/**
	 * Method to calculate 16-bit CRC based on Preset of 0x1D0F
	 * and polynomial of 0x1021 ()
	 *
	 * @param buffer
	 */
	public static int calculateCRC16CCITTX1D0F(byte[] data)
	{
		final int POLYNOMIAL   = 0x1021;
		final int PRESET_VALUE = 0x1D0F;

		int rem = PRESET_VALUE;

		for (int i = 0; i < data.length; i++ )
		{
			rem ^= data[i] << 8;	//XOR databyte after AND'ing

			//For each bit in the byte
			for (int j = 0; j < 8; j++)
			{
				if ((rem & 0x8000) != 0)
				{
					rem = (rem << 1) ^ POLYNOMIAL;
				}
				else
				{
					rem = rem << 1;
				}

				rem &= 0xFFFF;
			}
		}

		return rem;
	}
}
