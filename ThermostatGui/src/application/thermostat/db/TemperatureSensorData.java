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
 * Date of Last Change: 2015-11-14
 *
 * @author J Nelson
 *
 */
public class TemperatureSensorData
{
	/** List of temperature sensor records */
	private List<TemperatureSensorRecord> tempReading = new LinkedList<TemperatureSensorRecord>();

	/** File for writing to */
	private File file = null;

	/** FileWriter for writing to */
	private FileWriter fileWriter = null;

	/** Variable Description */
	private PrintWriter printWriter = null;

	/** Default Path of the file to export to */
	private String filePath = "C:\\Users\\DeveloperMain\\thermostat-project-workspace\\ThermostatGui\\thermostat-readings.csv";

	/**
	 * Default constructor
	 */
	public TemperatureSensorData()
	{
		//Do Nothing in the default constructor
	}

	/**
	 * Method for adding a TMP102SensorRecord to the list of records
	 *
	 * @param tempMeasurement The temperature reading
	 * @param localDateTime The date of the reading
	 */
	public void addRecord(double tempMeasurement, LocalDateTime localDateTime)
	{
		tempReading.add(new TemperatureSensorRecord(tempMeasurement, localDateTime));
	}

	/**
	 * Method to print to the console all of the records in the "database"
	 * This method was previously used for debugging purposes only
	 */
	public void printRecords()
	{
		for(int i=0; i<tempReading.size(); i++)
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

	        for(int i=0; i<tempReading.size(); i++)
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
	 * @param pathOfFile The path of the file
	 */
	public void buildCSVFile(String pathOfFile)
	{
		file = new File(pathOfFile);

	    try
	    {
	    	fileWriter = new FileWriter(file, true);
	        printWriter = new PrintWriter(fileWriter);

	        for(int i=0; i<tempReading.size(); i++)
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
