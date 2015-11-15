package application.thermostat.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import application.thermostat.Thermostat;

/***
 * Logger for logging messages of various levels.
 *
 * Date of Last Change: 2015-10-22
 *
 * @author J Nelson
 *
 */
public class ThermostatLogger
{
	/** Logger */
	public static final Logger logger = Logger.getLogger(Thermostat.class.getName());

	/** Formatter for the logger output */
	private Formatter loggerFormatter = null;

	/** Handler for setting up the log file path */
	private Handler fileHandler;

	/** Represents whether the logger is enabled or not */
	private boolean enabled = false;

	/** Represents the logging level for the logger */
	private int loggingLevel = 0;

	/** Default path of log file */
	private String pathOfLogFile = "C:\\Users\\DeveloperMain\\thermostat-project-workspace\\ThermostatGui\\thermostatlog.log";

	/**
	 * Default Constructor
	 */
	public ThermostatLogger()
	{
		try
		{
			fileHandler = new FileHandler(pathOfLogFile);
			logger.addHandler(fileHandler);
			fileHandler.setLevel(Level.ALL);
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Overloaded Constructor
	 *
	 * @param pathOfLogFile The path to save the log file.
	 * @param enabled Determines whether the Logger is enabled or not.
	 * @param level The level of logging information to save.
	 */
	public ThermostatLogger(String pathOfLogFile, boolean enabled, int level)
	{
		this.pathOfLogFile = pathOfLogFile;
		this.enabled = enabled;
		this.loggingLevel = level;

		try
		{
			loggerFormatter = new SimpleFormatter();
			fileHandler = new FileHandler(pathOfLogFile);
			fileHandler.setFormatter(loggerFormatter);
			logger.addHandler(fileHandler);

			if(enabled)
			{
				setLoggingLevel(level);
			}
			else
			{
				fileHandler.setLevel(Level.OFF);
			}
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method used to obtain the fileHandler for the class.
	 *
	 * @return The fileHandler.
	 */
	public Handler getFileHandler()
	{
		return fileHandler;
	}

	/**
	 * Method used to set the fileHandler
	 *
	 * @param fileHandler The fileHandler to set.
	 */
	public void setFileHandler(Handler fileHandler)
	{
		this.fileHandler = fileHandler;
	}

	/**
	 * Method used to obtain the path where the log file will be saved to.
	 *
	 * @return The path of the log file saved.
	 */
	public String getPathOfLogFile()
	{
		return pathOfLogFile;
	}

	/**
	 * Method used to set the path for the log file.
	 *
	 * @param pathOfLogFile The pathOfLogFile to set
	 */
	public void setPathOfLogFile(String pathOfLogFile)
	{
		this.pathOfLogFile = pathOfLogFile;
	}

	/**
	 * Method used to obtain whether or not the logger is enabled.
	 *
	 * @return Whether or not the Logger is enabled.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Method Description
	 *
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled)
	{
		//Set the enabled flag
		this.enabled = enabled;

		if(enabled)
		{
			fileHandler.setLevel(Level.ALL);
		}
		else
		{
			fileHandler.setLevel(Level.OFF);
		}
	}

	/**
	 * Method used to obtain the current logging level.
	 *
	 * @return The loggingLevel.
	 */
	public int getLoggingLevel()
	{
		return loggingLevel;
	}

	/**
	 * Method used to obtain the logging level of the Logger.
	 *
	 * @param loggingLevel the loggingLevel to set
	 */
	public void setLoggingLevel(int loggingLevel)
	{
		this.loggingLevel = loggingLevel;

		if(loggingLevel == 0)
		{
			fileHandler.setLevel(Level.OFF);
		}
		else if(loggingLevel == 1)
		{
			fileHandler.setLevel(Level.SEVERE);
		}
		else if(loggingLevel == 2)
		{
			fileHandler.setLevel(Level.WARNING);
		}
		else if(loggingLevel == 3)
		{
			fileHandler.setLevel(Level.INFO);
		}
		else if(loggingLevel == 4)
		{
			fileHandler.setLevel(Level.CONFIG);
		}
		else
		{
			System.out.println("Unable to Configure Logger");
		}
	}
}
