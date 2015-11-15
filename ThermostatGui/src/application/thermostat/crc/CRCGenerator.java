package application.thermostat.crc;

/**
 * Class used to generate a 16-bit CRC
 *
 * Date of Last Change: 2015-11-14
 *
 * @author J Nelson
 *
 */
public class CRCGenerator
{
	/**
	 * Method to calculate 16-bit CRC based on Preset of 0x0000
	 * and polynomial of 0x1021
	 *
	 * @param data The array of bytes to generate CRC on.
	 * @return The calculated CRC value.
	 */
	public static int calculateCRCCCITTXModem(byte[] data)
	{
		final int POLYNOMIAL   = 0x1021;
		final int PRESET_VALUE = 0x0000;

		//Remainder
		int rem = PRESET_VALUE;

		//Loop through the entire array
		for (int i = 0; i < data.length; i++)
		{
			//XOR the data
			rem ^= data[i] << 8;

			//Loop for each bit in the byte
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
	 * Method to calculate 16-bit CRC based on Preset of 0xFFFF
	 * and polynomial of 0x1021
	 *
	 * @param data The array of bytes to generate CRC on.
	 * @return The calculated CRC value.
	 */
	public static int calculateCRCCCITTFFFF(byte[] data)
	{
		final int POLYNOMIAL   = 0x1021;
		final int PRESET_VALUE = 0xFFFF;

		int rem = PRESET_VALUE;

		for (int i = 0; i < data.length; i++ )
		{
			rem ^= data[i] << 8;

			//Loop for each bit in the byte
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
	 * @param data The array of bytes to generate CRC on.
	 * @return  The calculated CRC value.
	 */
	public static int calculateCRC16CCITTX1D0F(byte[] data)
	{
		final int POLYNOMIAL   = 0x1021;
		final int PRESET_VALUE = 0x1D0F;

		int rem = PRESET_VALUE;

		for (int i = 0; i < data.length; i++ )
		{
			rem ^= data[i] << 8;

			//Loop for each bit in the byte
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
