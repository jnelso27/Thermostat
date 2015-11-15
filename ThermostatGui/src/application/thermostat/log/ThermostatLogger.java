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
	 * Method Description
	 *
	 * @return the fileHandler
	 */
	public Handler getFileHandler()
	{
		return fileHandler;
	}

	/**
	 * Method Description
	 *
	 * @param fileHandler the fileHandler to set
	 */
	public void setFileHandler(Handler fileHandler)
	{
		this.fileHandler = fileHandler;
	}

	/**
	 * Method Description
	 *
	 * @return the pathOfLogFile
	 */
	public String getPathOfLogFile()
	{
		return pathOfLogFile;
	}

	/**
	 * Method Description
	 *
	 * @param pathOfLogFile the pathOfLogFile to set
	 */
	public void setPathOfLogFile(String pathOfLogFile)
	{
		this.pathOfLogFile = pathOfLogFile;
	}

	/**
	 * Method Description
	 *
	 * @return the enabled
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
		this.enabled = enabled;

		if(enabled)
		{
			fileHandler.setLevel(Level.ALL);
			System.out.println("Setting logger to enabled ALL");
		}
		else
		{
			fileHandler.setLevel(Level.OFF);
			System.out.println("Setting logger to disabled OFF");
		}
	}

	/**
	 * Method Description
	 *
	 * @return the loggingLevel
	 */
	public int getLoggingLevel()
	{
		return loggingLevel;
	}

	/**
	 * Method Description
	 *
	 * @param loggingLevel the loggingLevel to set
	 */
	public void setLoggingLevel(int loggingLevel)
	{
		this.loggingLevel = loggingLevel;

		if(loggingLevel == 0)
		{
			fileHandler.setLevel(Level.OFF);
			System.out.println("Setting logger to OFF");
		}
		else if(loggingLevel == 1)
		{
			fileHandler.setLevel(Level.SEVERE);
			System.out.println("Setting logger to SEVERE");
		}
		else if(loggingLevel == 2)
		{
			fileHandler.setLevel(Level.WARNING);
			System.out.println("Setting logger to WARNING");
		}
		else if(loggingLevel == 3)
		{
			fileHandler.setLevel(Level.INFO);
			System.out.println("Setting logger to INFO");
		}
		else if(loggingLevel == 4)
		{
			fileHandler.setLevel(Level.CONFIG);
			System.out.println("Setting logger to CONFIG");
		}
		else
		{
			System.out.println("Unable to Configure Logger");
		}
	}
}
