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

import application.thermostat.Thermostat;
import application.thermostat.log.ThermostatLogger;
import application.thermostat.sensors.AlarmSensor;
import application.thermostat.sensors.Sensor;
import application.thermostat.sensors.SensorType;
import application.thermostat.sensors.TemperatureSensor;

/***
 * Class Description
 *
 * @author Joshua Nelson
 *
 */
public class XMLFileLoader
{
	//Thermostat object to load settings to
	Thermostat thermostat = null;

	ThermostatLogger logger = null;

	//List of Sensors to Build
	LinkedList<Sensor> listOfSensors = new LinkedList<Sensor>();

	/**
	 * Method used to load up the configuration file
	 *
	 * @return
	 */
	public Thermostat loadXMLConfigurationFile()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = null;
	    Document document = null;

	    //Filepath of the XML File to load
	    String defaultFilePath = "C:\\Users\\DeveloperMain\\thermostat-project-workspace\\ThermostatGui\\thermostat-config-settings.xml";

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

        //
        String loggingPath = "";

        //Represents the default COM port
        int comPort = 7;

		try
		{
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			document = builder.parse(new File(defaultFilePath));
			System.out.println("Successfully Loaded Configuration File");
		}
		catch (SAXException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//
	    NodeList nodeList = document.getDocumentElement().getChildNodes();

	    //Loop through XML File to load
	    for (int i = 0; i < nodeList.getLength(); i++)
	    {
	         Node node = nodeList.item(i);

	         if (node.getNodeName() == "ApplicationSettings")
	         {
	        	 System.out.println("Load XML Settings-ApplicationSettings");

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
	              comPort =  Integer.parseInt(elem.getElementsByTagName("ComPort").item(0).getChildNodes().item(0).getNodeValue());
	              System.out.println("Load XML Settings-Loading Com Port setting:" + comPort);
	         }
	         if(node.getNodeName() == "Sensor")
	         {
	        	 System.out.println("Load XML Settings-Sensor");

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
	        		 //thermostat.addSensor(new AlarmSensor());
	        		 listOfSensors.add(new AlarmSensor(sensorName,sensorType,testingFlag));
	        		 System.out.println("Load XML Settings-Adding Sensor: " + sensorName + ", " + sensorType + ", " + testingFlag);
	        	 }
	        	 else
	        	 {
	        		 System.out.println("An unknown parsed sensor has been detected and not loaded!");
	        	 }
	         }
	    }

	    System.out.println("Creating new thermostat object");
	    logger = new ThermostatLogger(loggingPath, loggingEnabled, loggingLevel);
        thermostat = new Thermostat(highThreshold, lowThreshold, tempUnits, listOfSensors, logger);

	    //Add Data Observers
	    thermostat.addSensorObservers();
	    //thermostat.setLoggerEnabled(loggingEnabled);
	    //thermostat.setLoggingLevel(loggingLevel);

	    ThermostatLogger.logger.severe("XML Settings Loaded Successfully");

	    return thermostat;
	}
}
