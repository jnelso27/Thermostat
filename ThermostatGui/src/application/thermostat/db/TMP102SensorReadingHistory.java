package application.thermostat.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Class used to write the TMP102Sensor Records. Currently implemented to write to a simple LinkedList and then
 * save to a normal text file upon command.
 *
 * @author Joshua Nelson
 *
 */
public class TMP102SensorReadingHistory
{
	//List of temperature sensor records
	List<TMP102SensorRecord> tempReading = new LinkedList<TMP102SensorRecord>();

	//File for writing to
	File file = null;

	//FileWriter for writing to
	FileWriter fileWriter = null;

	//
	PrintWriter printWriter = null;

	//Path of the file to export to
	String filePath = "C:\\Users\\DeveloperMain\\thermostat-project-workspace\\ThermostatGui\\thermostat-readings.csv";

	/**
	 * Default constructor
	 */
	public TMP102SensorReadingHistory()
	{
		//Do Nothing
	}

	/**
	 * Method for adding a TMP102SensorRecord to the list of records
	 *
	 * @param tempMeasurement The temperature reading
	 * @param localDateTime The date of the reading
	 */
	public void addRecord(double tempMeasurement, LocalDateTime localDateTime)
	{
		tempReading.add(new TMP102SensorRecord(tempMeasurement, localDateTime));
	}

	/**
	 * Method to print to the console all of the records in the "database"
	 */
	public void printRecords()
	{
		for(int i=0;i<tempReading.size();i++)
		{
			System.out.println(tempReading.get(i));
		}
	}

	/**
	 * Method used to build the CSV file of temperature records
	 */
	public void buildCSVFile()
	{
		file = new File(filePath);

	    try
	    {
	    	fileWriter = new FileWriter(file, true);
	        printWriter = new PrintWriter(fileWriter);

	        for(int i=0;i<tempReading.size();i++)
			{
	        	printWriter.append(tempReading.get(i).getTempMeasurementDate().toString() + ","+tempReading.get(i).getTempMeasurement()+"\n");
			}

	        printWriter.close();
	    }
	    catch (IOException e)
	    {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}

	/**
	 * Method used to build the CSV file of temperature records
	 *
	 * @param pathOfFile
	 */
	public void buildCSVFile(String pathOfFile)
	{
		file = new File(pathOfFile);

	    try
	    {
	    	fileWriter = new FileWriter(file, true);
	        printWriter = new PrintWriter(fileWriter);

	        for(int i=0;i<tempReading.size();i++)
			{
	        	printWriter.append(tempReading.get(i).getTempMeasurementDate().toString() + ","+tempReading.get(i).getTempMeasurement()+"\n");
			}

	        printWriter.close();
	    }
	    catch (IOException e)
	    {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
