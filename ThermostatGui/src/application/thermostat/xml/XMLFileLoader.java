package application.thermostat.xml;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import application.ThermostatGUI;
import application.thermostat.Thermostat;
import application.thermostat.log.ThermostatLogger;
import application.thermostat.sensors.AlarmSensor;
import application.thermostat.sensors.Sensor;
import application.thermostat.sensors.SensorType;
import application.thermostat.sensors.TemperatureSensor;

/***
 * Class used to load the application settings XML file. Class is responsible initializing the
 * Thermostat and ThermostatLogger objects as well as building the sensor list from
 * the XML file.
 *
 * Date of Last Change: 2015-10-23
 *
 * @author J Nelson
 *
 */
public class XMLFileLoader
{
	/** Thermostat object to load settings to */
	private Thermostat thermostat = null;

	/** Application logger */
	private ThermostatLogger logger = null;

	/** List of Sensors to Build */
	private LinkedList<Sensor> listOfSensors = new LinkedList<Sensor>();

	/** Default Filepath of the XML File to load */
    private String defaultFilePath = "C:\\Users\\DeveloperMain\\thermostat-project-workspace\\ThermostatGui\\thermostat-config-settings.xml";

	/**
	 * Default Constructor
	 */
	public XMLFileLoader()
	{
		//Do nothing
	}

	/**
	 * Method used to load up the configuration file
	 *
	 * @return The loaded Thermostat object.
	 */
	public Thermostat loadXMLConfigurationFile()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = null;
	    Document document = null;

	    //High threshold for alarm functionality
	    double highThreshold = 0.0;

	    //Low threshold for alarm functionality
        double lowThreshold = 0.0;

        //Temperature Units in Farenheit(true) or Celcuis(false)
        boolean tempUnits = true;

        //Logging enabled/diabled
        boolean loggingEnabled = false;

        //Represents the logging level for the Thermostat Application - NOT FULLY IMPLEMENTED
        int loggingLevel = 0;

        //Logging path
        String loggingPath = "";

        //Represents the default COM port
        String comPort = "COM7";

		try
		{
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			ThermostatGUI.showErrorDialog("Unable to OPEN selected file");
			e.printStackTrace();
		}

		try
		{
			document = builder.parse(new File(defaultFilePath));
			System.out.println("Successfully Loaded Configuration File");
		}
		catch (SAXException | IOException e)
		{
			//Replace with a Logger message here in a future version...
			ThermostatGUI.showErrorDialog("Unable to OPEN selected file");
			//e.printStackTrace();
		}

		//Get list of nodes
	    NodeList nodeList = document.getDocumentElement().getChildNodes();

	    //Loop through XML File to load
	    for (int i=0; i<nodeList.getLength(); i++)
	    {
	         Node node = nodeList.item(i);

	         if (node.getNodeName() == "ApplicationSettings")
	         {
	        	 //Replace with a Logger message here in a future version...
	        	 //System.out.println("Load XML Settings-ApplicationSettings");

	        	 Element elem = (Element) node;

	        	 highThreshold = Double.parseDouble(elem.getElementsByTagName("Highthreshold").item(0).getChildNodes().item(0).getNodeValue());
	        	 System.out.println("Load XML Settings-Loading High Threshold:" + highThreshold);

	        	 lowThreshold = Double.parseDouble(elem.getElementsByTagName("Lowthreshold").item(0).getChildNodes().item(0).getNodeValue());
	        	 System.out.println("Load XML Settings-Loading Low Threshold:" + lowThreshold);

	        	 tempUnits = Boolean.parseBoolean(elem.getElementsByTagName("TemperatureUnitsInFarenheit").item(0).getChildNodes().item(0).getNodeValue());
	        	 System.out.println("Load XML Settings-Loading Temperature Units in Fahrenheit:" + tempUnits);

	        	 loggingEnabled =  Boolean.parseBoolean(elem.getElementsByTagName("LoggingEnabled").item(0).getChildNodes().item(0).getNodeValue());
	        	 System.out.println("Load XML Settings-Loading Logging Enabled:" + loggingEnabled);

	        	 loggingLevel =  Integer.parseInt(elem.getElementsByTagName("LoggingLevel").item(0).getChildNodes().item(0).getNodeValue());
	        	 System.out.println("Load XML Settings-Loading Logging Level Of:" + loggingLevel);

	        	 loggingPath =  elem.getElementsByTagName("LoggingPath").item(0).getChildNodes().item(0).getNodeValue();
	        	 System.out.println("Load XML Settings-Setting LoggingPath To:" + loggingPath);

	        	 comPort =  elem.getElementsByTagName("ComPort").item(0).getChildNodes().item(0).getNodeValue();
	        	 System.out.println("Load XML Settings-Loading Com Port setting:" + comPort);
	         }
	         if(node.getNodeName() == "Sensor")
	         {
	        	//Replace with a Logger message here in a future version...
	        	//System.out.println("Load XML Settings-Sensor");

	        	 Element elem = (Element) node;

	        	 String sensorName = elem.getElementsByTagName("SensorName").item(0).getChildNodes().item(0).getNodeValue();
	        	 System.out.println("Load XML Settings-Loading Sensor Named:" + sensorName);

	        	 Integer sensorType = Integer.parseInt(elem.getElementsByTagName("SensorType").item(0).getChildNodes().item(0).getNodeValue());
	        	 System.out.println("Load XML Settings-Loading Sensor Type:" + sensorType);

	        	 Boolean testingFlag = Boolean.parseBoolean(elem.getElementsByTagName("TestingFlag").item(0).getChildNodes().item(0).getNodeValue());
	        	 System.out.println("Load XML Settings-Loading Sensor Testing Flag to:" + testingFlag);

	        	 if(sensorType == SensorType.TEMPERATURE)
	        	 {
	        		 listOfSensors.add(new TemperatureSensor(sensorName,sensorType,testingFlag));
	        		 System.out.println("Load XML Settings-Adding Sensor: " + sensorName + ", " + sensorType + ", " + testingFlag);
	        	 }
	        	 else if(sensorType == SensorType.LED)
	        	 {
	        		 listOfSensors.add(new AlarmSensor(sensorName,sensorType,testingFlag));
	        		 System.out.println("Load XML Settings-Adding Sensor: " + sensorName + ", " + sensorType + ", " + testingFlag);
	        	 }
	        	 else
	        	 {
	        		 System.out.println("An unknown parsed sensor has been detected and not loaded!");
	        	 }
	         }
	    }

	    logger = new ThermostatLogger(loggingPath, loggingEnabled, loggingLevel);
        thermostat = new Thermostat(highThreshold, lowThreshold, tempUnits, listOfSensors, comPort, logger);

	    thermostat.addSensorObservers();

	    return thermostat;
	}

	/**
	 * Method used to obtain the default file path of the XML Settings File
	 *
	 * @return the defaultFilePath
	 */
	public String getDefaultFilePath()
	{
		return defaultFilePath;
	}

	/**
	 * Method used to set the default file path for loading of the XML Settings file
	 *
	 * @param defaultFilePath the defaultFilePath to set
	 */
	public void setDefaultFilePath(String defaultFilePath)
	{
		this.defaultFilePath = defaultFilePath;
	}
}
