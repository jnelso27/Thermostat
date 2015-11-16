package application.thermostat;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import application.thermostat.db.TemperatureSensorData;
import application.thermostat.log.ThermostatLogger;
import application.thermostat.message.MessageReceiver;
import application.thermostat.message.MessageSender;
import application.thermostat.sensors.AlarmLevel;
import application.thermostat.sensors.AlarmSensor;
import application.thermostat.sensors.Sensor;
import application.thermostat.sensors.SensorType;
import application.thermostat.sensors.TemperatureSensor;

/**
 * Primary backend class that represents the Thermostat system minus the GUI.
 * Contains multiple objects and attributes to support the frontend Graphical
 * User Interface (GUI).
 *
 * Date of Last Change: 2015-11-10
 *
 * @author J Nelson
 *
 */
public class Thermostat extends Observable
{
	/** Index of configured TMP102 Sensor */
	private static final int TMP102_SENSOR = 0;

	/** Index of configured alarm (LED) Sensor */
	private static final int ALARM_SENSOR = 1;

	/** Current measured temperature */
	private static double currentTemperature = 0;

	/** Configured high threshold value used for activation of WARNING/DANGER alarm indicators */
	private double highThreshold = 80;

	/** Configured low threshold value used for activation of NORMAL/WARNING alarm indicators */
	private double lowThreshold = 75;

	/** The current activated alarm level indicator */
	private int alarmLevel = AlarmLevel.NORMAL;

	/** Used for displaying whether temperature is in Farenheit or Celcius */
	private boolean temperatureUnitsInFarenheit = true;

	/** Temperature Sensor measurement period in milliseconds */
	private long tempSensorMeasurementPeriod = 1000;

	/** List of all sensors that are in the Thermostat per XML Settings File */
	private LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

	/** Thermostat Logger for debugging */
	private ThermostatLogger thermostatLogger;

	/** Message receiver for incoming messages */
	private MessageReceiver messageReciever;

	/** Message sender for sending messages */
	private MessageSender messageSender;

	/** Used for starting and stopping sensor measurements */
	private ScheduledExecutorService scheduledMeasurementService = null;
	private Runnable sensorMeasurementsTask;

	/** "Database" for saving temperature data */
	private TemperatureSensorData tempDB = new TemperatureSensorData();

	/**
	 * Default Constructor
	 */
	public Thermostat()
	{
		//Do Nothing
	}

	/**
	 * Overloaded Constructor
	 *
	 * @param highThreshold The high threshold for the temperature.
	 * @param lowThreshold The low threshold for the temperature.
	 * @param temperatureUnitsInFarenheit True to display temp measurements in Farenheit, false for Celcius
	 * @param sensorSuite The list of system-configured sensors as found in the XML settings configuration file.
	 * @param comPort The COM port which the system is connected to the host computer/laptop.
	 * @param logger System logger.
	 */
	public Thermostat(double highThreshold, double lowThreshold, boolean temperatureUnitsInFarenheit, LinkedList<Sensor> sensorSuite,
			String comPort, ThermostatLogger logger)
	{
		this.highThreshold = highThreshold;
		this.lowThreshold = lowThreshold;
		this.temperatureUnitsInFarenheit = temperatureUnitsInFarenheit;
		this.sensorSuite = sensorSuite;
		this.thermostatLogger = logger;

		messageReciever = new MessageReceiver(sensorSuite, comPort);
		messageSender = new MessageSender();

		getSensor(ALARM_SENSOR).requestSensorData(AlarmSensor.NORMAL_ALARM_REQUEST);
		alarmLevel = AlarmLevel.NORMAL;

		sensorMeasurementsTask = new Runnable()
		{
			@Override
			public void run()
			{
				getCurrentTemperature();
			}
		};
	}

	/**
	 * Method used to add observers to variables
	 */
	public void addSensorObservers()
	{
		for(int i=0;i<sensorSuite.size();i++)
		{
			if(sensorSuite.get(i).getSensorType() == SensorType.TEMPERATURE)
			{
				System.out.println("Adding observer for: " + sensorSuite.get(i).getSensorName());

				sensorSuite.get(i).addObserver((Observable obj, Object arg)->
				{
					validateAlarmIndicator((double)arg);
					Thermostat.currentTemperature = (double)arg;

					setChanged();
					notifyObservers(Thermostat.currentTemperature);

					tempDB.addRecord((double)arg, LocalDateTime.now());
				});
			}
			else if(sensorSuite.get(i).getSensorType() == SensorType.LED)
			{
				System.out.println("No observer required for: " + sensorSuite.get(i).getSensorName());
			}
			else
			{
				System.out.println("Unable to add observer. Unknown sensor at index: " + sensorSuite.get(i).getSensorName());
			}
		}
	}

	/**
	 * Method which is used to startup the sensor measurements.
	 * It runs a single task for measuring the temperature
	 */
	public void startSensorMeasurements()
	{
		if(scheduledMeasurementService == null)
		{
			scheduledMeasurementService = Executors.newScheduledThreadPool(1);
			scheduledMeasurementService.scheduleWithFixedDelay(sensorMeasurementsTask, 1, tempSensorMeasurementPeriod, TimeUnit.MILLISECONDS);
		}
		else if(scheduledMeasurementService.isShutdown())
		{
			scheduledMeasurementService = Executors.newScheduledThreadPool(1);
			scheduledMeasurementService.scheduleWithFixedDelay(sensorMeasurementsTask, 1, tempSensorMeasurementPeriod, TimeUnit.MILLISECONDS);
		}
		else
		{
			//Do Nothing
		}
	}

	/**
	 * Method which is used to stop the sensor measurements.
	 * It will shutdown the currently running task.
	 */
	public void stopSensorMeasurements()
	{
		if(scheduledMeasurementService != null)
		{
			scheduledMeasurementService.shutdown();
		}
	}

	/**
	 * Method used to validate the currently set alarm indicator against the most
	 * recently measured temperature reading. Giving the comparison, it will use the
	 * requestSensorData() method for the AlarmSensor.
	 *
	 * @param temperatureReading The current temperature reading.
	 */
	public void validateAlarmIndicator(double temperatureReading)
	{
		if(temperatureReading <= lowThreshold)
		{
			if(alarmLevel != AlarmLevel.NORMAL)
			{
				System.out.println("temperatureReading <= lowThreshold at: "+temperatureReading);
				getSensor(ALARM_SENSOR).requestSensorData(AlarmSensor.NORMAL_ALARM_REQUEST);
				alarmLevel = AlarmLevel.NORMAL;
			}
		}
		else if(temperatureReading > lowThreshold && temperatureReading < highThreshold)
		{
			if(alarmLevel != AlarmLevel.WARNING)
			{
				System.out.println("temperatureReading > lowThreshold && temperatureReading < highThreshold at: "+temperatureReading);
				getSensor(ALARM_SENSOR).requestSensorData(AlarmSensor.WARNING_ALARM_REQUEST);
				alarmLevel = AlarmLevel.WARNING;
			}
		}
		else
		{
			System.out.println("temperatureReading > highThreshold at: "+temperatureReading);
			getSensor(ALARM_SENSOR).requestSensorData(AlarmSensor.DANGER_ALARM_REQUEST);
			alarmLevel = AlarmLevel.DANGER;
		}
	}

	/**
	 * Method used to save the temperature sensor measurement history
	 */
	public void saveTemperatureRecords()
	{
		tempDB.buildCSVFile();
	}

	/**
	 * Method used to save all sensor record history
	 * (FUTURE IMPLEMENTATION)
	 */
	public void saveSensorRecords()
	{
		//Do Nothing
	}

	/**
	 * Method used to get the current temperature reading
	 */
	public void getCurrentTemperature()
	{
		getSensor(TMP102_SENSOR).requestSensorData(TemperatureSensor.TEMPERATURE_READING_REQUEST);
	}

	/**
	 * Method used to set the current temperature
	 *
	 * @param currentTemperature The current temperature measurement.
	 */
	public void setCurrentTemperature(double currentTemperature)
	{
		Thermostat.currentTemperature = currentTemperature;
	}

	/**
	 * Method used to set the Temperature Measurement Period Cycle
	 *
	 * @param period The measurement cycle time in milliseconds
	 */
	public void setTemperatureMeasurementPeriod(long period)
	{
		this.tempSensorMeasurementPeriod = period;
	}

	/**
	 * Method used to get the Temperature Measurement Period Cycle
	 *
	 * @return The temperature measurement period.
	 */
	public double getTemperatureMeasurementPeriod()
	{
		return tempSensorMeasurementPeriod;
	}

	/**
	 * Method used to return the size of the sensor list
	 *
	 * @return The number of sensors in the list.
	 */
	public int getSensorListSize()
	{
		return sensorSuite.size();
	}

	/**
	 * Method used to add a sensor to the sensor list.
	 *
	 * @param sensor The sensor to add.
	 */
	public void addSensor(Sensor sensor)
	{
		sensorSuite.add(sensor);
	}

	/**
	 * Method used to get a particular sensor by its index
	 *
	 * @param index The position of the sensor.
	 * @return The sensor at the supplied index.
	 */
	public Sensor getSensor(int index)
	{
		return sensorSuite.get(index);
	}

	/**
	 * Method used to get the high threshold value
	 *
	 * @return The highThreshold
	 */
	public double getHighThreshold()
	{
		return highThreshold;
	}

	/**
	 * Method used to set the high threshold value
	 *
	 * @param highThreshold The highThreshold to set
	 */
	public void setHighThreshold(double highThreshold)
	{
		this.highThreshold = highThreshold;
	}

	/**
	 * Method used to get the low threshold value
	 *
	 * @return The lowThreshold
	 */
	public double getLowThreshold()
	{
		return lowThreshold;
	}

	/**
	 * Method used to set the low threshold value
	 *
	 * @param lowThreshold The lowThreshold to set
	 */
	public void setLowThreshold(double lowThreshold)
	{
		this.lowThreshold = lowThreshold;
	}

	/**
	 * Method used to obtain whether the current measurement value is in Farenheit or Celcius.
	 *
	 * @return The temperatureUnitsInFarenheit
	 */
	public boolean isTemperatureUnitsInFarenheit()
	{
		return temperatureUnitsInFarenheit;
	}

	/**
	 * Method used to set the temperature measurement units.
	 *
	 * @param temperatureUnitsInFarenheit the temperatureUnitsInFarenheit to set
	 */
	public void setTemperatureUnitsInFarenheit(boolean temperatureUnitsInFarenheit)
	{
		this.temperatureUnitsInFarenheit = temperatureUnitsInFarenheit;
	}

	/**
	 * Method used to obtain the Thermostat logger
	 *
	 * @return The thermostatLogger
	 */
	public ThermostatLogger getThermostatLogger()
	{
		return thermostatLogger;
	}

	/**
	 * Method used to set the logger for the Thermostat
	 *
	 * @param thermostatLogger The thermostatLogger to set
	 */
	public void setThermostatLogger(ThermostatLogger thermostatLogger)
	{
		this.thermostatLogger = thermostatLogger;
	}

	/**
	 * Method used to obtain the MessageReciever of the Thermostat
	 *
	 * @return The messageReciever
	 */
	public MessageReceiver getMessageReciever()
	{
		return messageReciever;
	}

	/**
	 * Message used to set the MessageReceiver of the Thermostat
	 *
	 * @param messageReciever The messageReciever to set
	 */
	public void setMessageReciever(MessageReceiver messageReciever)
	{
		this.messageReciever = messageReciever;
	}
}
