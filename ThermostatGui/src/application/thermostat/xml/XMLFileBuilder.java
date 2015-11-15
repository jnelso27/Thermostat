package application.thermostat.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.thermostat.Thermostat;

/***
 * The XMLFileBuilder class is used to create the XML Configuration File for the
 * Host (PC) Thermostat system application.
 *
 * Date of Last Change: 2015-11-15
 *
 * @author J Nelson
 *
 */
public class XMLFileBuilder
{
	/**
	 * Default Constructor
	 */
	public XMLFileBuilder()
	{
		//Do nothing
	}

	/**
	 * Method used to build the configuration settings XML file.
	 *
	 * @param thermostat A thermostat object holding all configured sensors with updated configuration settings.
	 * @param filePath The path to save the configuration settings file to.
	 */
	public void buildXMLConfigurationFile(Thermostat thermostat, String filePath)
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();

			Document doc = builder.newDocument();
			Element rootElement = doc.createElement("ThermostatSettings");
			doc.appendChild(rootElement);

			Element applicationSettings = doc.createElement("ApplicationSettings");
			rootElement.appendChild(applicationSettings);

			Element highThreshold = doc.createElement("Highthreshold");
			highThreshold.appendChild(doc.createTextNode(Double.toString(thermostat.getHighThreshold())));
			applicationSettings.appendChild(highThreshold);

			Element lowThreshold = doc.createElement("Lowthreshold");
			lowThreshold.appendChild(doc.createTextNode(Double.toString(thermostat.getLowThreshold())));
			applicationSettings.appendChild(lowThreshold);

			Element temperatureUnitsInFarenheit = doc.createElement("TemperatureUnitsInFarenheit");
			temperatureUnitsInFarenheit.appendChild(doc.createTextNode(Boolean.toString(thermostat.isTemperatureUnitsInFarenheit())));
			applicationSettings.appendChild(temperatureUnitsInFarenheit);

			Element loggingEnabled = doc.createElement("LoggingEnabled");
			loggingEnabled.appendChild(doc.createTextNode(Boolean.toString(thermostat.getThermostatLogger().isEnabled())));
			applicationSettings.appendChild(loggingEnabled);

			Element loggingLevel = doc.createElement("LoggingLevel");
			loggingLevel.appendChild(doc.createTextNode(Integer.toString(thermostat.getThermostatLogger().getLoggingLevel())));
			applicationSettings.appendChild(loggingLevel);

			Element loggingPath = doc.createElement("LoggingPath");
			loggingPath.appendChild(doc.createTextNode(thermostat.getThermostatLogger().getPathOfLogFile()));
			applicationSettings.appendChild(loggingPath);

			Element comPort = doc.createElement("ComPort");
			comPort.appendChild(doc.createTextNode(thermostat.getMessageReciever().getComPort()));
			applicationSettings.appendChild(comPort);

			//Now loop through the list of sensors to create a <Sensor> tag with settings.
			for(int i=0; i<thermostat.getSensorListSize(); i++)
			{
				Element sensor = doc.createElement("Sensor");
				rootElement.appendChild(sensor);

				Element sensorName = doc.createElement("SensorName");
				sensorName.appendChild(doc.createTextNode(thermostat.getSensor(i).getSensorName()));
				sensor.appendChild(sensorName);

				Element sensorType = doc.createElement("SensorType");
				sensorType.appendChild(doc.createTextNode(String.valueOf(thermostat.getSensor(i).getSensorType())));
				sensor.appendChild(sensorType);

				Element testingFlag = doc.createElement("TestingFlag");
				testingFlag.appendChild(doc.createTextNode(String.valueOf(thermostat.getSensor(i).getSensorType())));
				sensor.appendChild(testingFlag);
			}

			// Write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));

			transformer.transform(source, result);

			//Replace with a Logger message here in a future version...
		}
		catch (ParserConfigurationException pce)
		{
			//Replace with a Logger message here in a future version...
			pce.printStackTrace();
		}
		catch (TransformerException tfe)
		{
			//Replace with a Logger message here in a future version...
			tfe.printStackTrace();
		}
	}
}
