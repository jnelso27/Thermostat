package application.thermostat;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import application.thermostat.db.TMP102SensorReadingHistory;
import application.thermostat.log.ThermostatLogger;
import application.thermostat.message.MessageReceiver;
import application.thermostat.message.MessageSender;
import application.thermostat.message.messages.TemperatureDangerLevelMessage;
import application.thermostat.message.messages.TemperatureNormalLevelMessage;
import application.thermostat.message.messages.TemperatureReadingRequestMessage;
import application.thermostat.message.messages.TemperatureWarningLevelMessage;
import application.thermostat.sensors.AlarmLevel;
import application.thermostat.sensors.AlarmSensor;
import application.thermostat.sensors.Sensor;
import application.thermostat.sensors.SensorType;
import application.thermostat.sensors.TemperatureSensor;

/**
 * Class Description
 *
 * Date:
 *
 * @author J Nelson
 *
 */
public class Thermostat extends Observable
{
	/** Variable Description */
	private static double currentTemperature = 0;

	/** Variable Description */
	private double highThreshold = 80;

	/** Variable Description */
	private double lowThreshold = 75;

	/** Variable Description */
	private int alarmLevel = AlarmLevel.NORMAL;

	/** Variable Description */
	private boolean temperatureUnitsInFarenheit = true;

	/** Temperature Sensor measurement period in milliseconds */
	private long tempSensorMeasurementPeriod = 1000;

	/** List of all sensors that are in the Thermostat */
	LinkedList<Sensor> sensorSuite = new LinkedList<Sensor>();

	/** Thermostat Logger */
	ThermostatLogger thermostatLogger;

	/** Message receiver for incoming messages */
	MessageReceiver messageReciever;

	/** MessageSender for sending messages */
	MessageSender messageSender;

	/** Variable Description */
	ScheduledExecutorService scheduledPool = null;

	/** Variable Description */
	Runnable runnabledelayedTask;

	//NEED TO REMOVE, FIND A BETTER WAY...
	byte data1[] = {0x00, 0x01}; //DEBUG CODE ONLY



	/** Variable Description */
	private TMP102SensorReadingHistory tempDB = new TMP102SensorReadingHistory();

	/**
	 * Default Constructor
	 */
	public Thermostat()
	{
		//Do Nothing
	}

	/**
	 * Constructor
	 *
	 * @param highThreshold The value to set for the high threshold temperature
	 * @param lowThreshold The value to set for the low threshold temperature
	 * @param tempUnits
	 */
	public Thermostat(double highThreshold, double lowThreshold, boolean temperatureUnitsInFarenheit, LinkedList<Sensor> sensorSuite,
			String comPort, ThermostatLogger logger)
	{
		this.highThreshold = highThreshold;
		this.lowThreshold = lowThreshold;
		this.temperatureUnitsInFarenheit = temperatureUnitsInFarenheit;
		this.sensorSuite = sensorSuite;
		this.thermostatLogger = logger;

		//
		//thermostatLogger = new ThermostatLogger();

		//Create RS232 Message Receiver and Sender
		messageReciever = new MessageReceiver(sensorSuite, comPort);
		messageSender = new MessageSender();

		//Start sensor measurements
		runnabledelayedTask = new Runnable()
		{
			@Override
			public void run()
			{
				startSensors();
			}
		};

		ThermostatLogger.logger.config("Working!");
		ThermostatLogger.logger.severe("Severe Msg Being Logged");
		//ThermostatLogger.logger.log(Level.SEVERE, "TestingSevere!");
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
					System.out.println("\nValueChange: " + arg);
					compare((double)arg);
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
	 *
	 */
	public void startSensors()
	{
		System.out.println("Thermostat.startSensors() is being called");
		getCurrentTemperature();
	}

	/**
	 *
	 */
	public void stopSensors()
	{

	}

	/**
	 * Method Description
	 */
	public void startSensorMeasurements()
	{
		if(scheduledPool == null)
		{
			scheduledPool = Executors.newScheduledThreadPool(1);
			scheduledPool.scheduleWithFixedDelay(runnabledelayedTask, 1, tempSensorMeasurementPeriod, TimeUnit.MILLISECONDS);
		}
		else if(scheduledPool.isShutdown())
		{
			scheduledPool = Executors.newScheduledThreadPool(1);
			scheduledPool.scheduleWithFixedDelay(runnabledelayedTask, 1, tempSensorMeasurementPeriod, TimeUnit.MILLISECONDS);
		}
		else
		{
			//Do Nothing
		}
	}

	/**
	 * Method Description
	 */
	public void stopSensorMeasurements()
	{
		if(scheduledPool != null)
		{
			scheduledPool.shutdown();
		}
	}

	/**
	 * Method Description
	 *
	 * @param temperatureReading
	 */
	public void compare(double temperatureReading)
	{
		if(temperatureReading <= lowThreshold)
		{
			if(alarmLevel != AlarmLevel.NORMAL)
			{
				System.out.println("temperatureReading <= lowThreshold at: "+temperatureReading);
				getSensor(1).requestSensorData(AlarmSensor.NORMAL_ALARM_REQUEST);
				alarmLevel = AlarmLevel.NORMAL;
			}
		}
		else if(temperatureReading > lowThreshold && temperatureReading < highThreshold)
		{
			if(alarmLevel != AlarmLevel.WARNING)
			{
				System.out.println("temperatureReading > lowThreshold && temperatureReading < highThreshold at: "+temperatureReading);
				getSensor(1).requestSensorData(AlarmSensor.WARNING_ALARM_REQUEST);
				alarmLevel = AlarmLevel.WARNING;
			}
		}
		else
		{
			System.out.println("temperatureReading > highThreshold at: "+temperatureReading);
			getSensor(1).requestSensorData(AlarmSensor.DANGER_ALARM_REQUEST);
			alarmLevel = AlarmLevel.DANGER;
		}
	}

	/**
	 * Method used to save the record history
	 */
	public void saveRecords()
	{
		tempDB.buildCSVFile();
	}

	/**
	 * Method Description
	 *
	 */
	public void getCurrentTemperature()
	{
		getSensor(0).requestSensorData(TemperatureSensor.TEMPERATURE_READING_REQUEST);
	}

	/**
	 * Method used for testing
	 *
	 * @throws InterruptedException
	 */
	public void getSimulatedCurrentTemperature() throws InterruptedException
	{
		System.out.println("Called getSimulatedCurrentTemperature()");	//Replace with a logger of some sort

		currentTemperature = (Math.random() + 2) * 4.36;
	}

	/**
	 * Method used to manually set the current temperature
	 *
	 * @param currentTemperature
	 */
	public void setCurrentTemperature(double currentTemperature)
	{
		this.currentTemperature = currentTemperature;
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
	 * @return
	 */
	public double getTemperatureMeasurementPeriod()
	{
		return tempSensorMeasurementPeriod;
	}

	/**
	 * Method used to return the size of the sensor list
	 *
	 * @return
	 */
	public int getSensorListSize()
	{
		return sensorSuite.size();
	}

	/**
	 * Method Description
	 *
	 * @param sensor
	 */
	public void addSensor(Sensor sensor)
	{
		sensorSuite.add(sensor);
	}

	/**
	 * Method used to get a particular sensor by its index
	 *
	 * @param index
	 * @return
	 */
	public Sensor getSensor(int index)
	{
		return sensorSuite.get(index);
	}

	/**
	 * Method used to get the high threshold value
	 *
	 * @return the highThreshold
	 */
	public double getHighThreshold()
	{
		return highThreshold;
	}

	/**
	 * Method used to set the high threshold value
	 *
	 * @param highThreshold the highThreshold to set
	 */
	public void setHighThreshold(double highThreshold)
	{
		this.highThreshold = highThreshold;
	}

	/**
	 * Method used to get the low threshold value
	 *
	 * @return the lowThreshold
	 */
	public double getLowThreshold()
	{
		return lowThreshold;
	}

	/**
	 * Method used to set the low threshold value
	 *
	 * @param lowThreshold the lowThreshold to set
	 */
	public void setLowThreshold(double lowThreshold)
	{
		this.lowThreshold = lowThreshold;
	}

	/**
	 * Method Description
	 *
	 * @return the temperatureUnitsInFarenheit
	 */
	public boolean isTemperatureUnitsInFarenheit()
	{
		return temperatureUnitsInFarenheit;
	}

	/**
	 * Method Description
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
	 * @return the thermostatLogger
	 */
	public ThermostatLogger getThermostatLogger()
	{
		return thermostatLogger;
	}

	/**
	 * Method used to set the logger for the Thermostat
	 *
	 * @param thermostatLogger the thermostatLogger to set
	 */
	public void setThermostatLogger(ThermostatLogger thermostatLogger)
	{
		this.thermostatLogger = thermostatLogger;
	}

	/**
	 * Method used to obtain the MessageReciever of the Thermostat
	 *
	 * @return the messageReciever
	 */
	public MessageReceiver getMessageReciever()
	{
		return messageReciever;
	}

	/**
	 * Message used to set the MessageReceiver of the Thermostat
	 *
	 * @param messageReciever the messageReciever to set
	 */
	public void setMessageReciever(MessageReceiver messageReciever)
	{
		this.messageReciever = messageReciever;
	}
}
