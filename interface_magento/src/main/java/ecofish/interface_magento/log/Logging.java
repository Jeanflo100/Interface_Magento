package ecofish.interface_magento.log;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Setting up logs writting
 * @author Jean-Florian Tassart
 */
public class Logging{
	
	public static final Logger LOGGER = Logger.getLogger(Logging.class.getName());
	
	private static Formatter logFormatter = null;
	private static Handler logConsoleHandler = null;
	private static Handler logFileHandler = null;
	
	/**
	 * Initialization of the different handlers to write logs
	 */
	public static void setLogging() {
		setLoggingConsole();
		setLoggingFile();
	}
	
	/**
	 * Initialization of the display of logs in the console
	 */
	private static void setLoggingConsole() {
		logConsoleHandler = new ConsoleHandler();
		LOGGER.addHandler(logConsoleHandler);
	}
	
	/**
	 * Initialization of the display of logs in a file
	 */
	private static void setLoggingFile() {
		
		logFormatter = new Formatter() {
			@Override
			public String format(LogRecord record) {
				String log = "";
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis(record.getMillis());
				log += "[" + date.getTime() + "] " + record.getLevel() + ": " + record.getMessage() + "\n";
				return log;
			}
		};
		
		try {
			logFileHandler = new FileHandler("./Interface_Magento.log", 0, 1, true);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Writing to file isn't set");
		}
		
		if (logFileHandler != null) {
			logFileHandler.setFormatter(logFormatter);
			logFileHandler.setLevel(Level.INFO);
			LOGGER.addHandler(logFileHandler);
		}

	}
	
	/**
	 * Open the file containing the logs
	 */
	public static void openLoggingFile() {
		try {
			Desktop.getDesktop().open(new File("./Interface_Magento.log"));
		} catch (IOException e) {
			Logging.LOGGER.log(Level.WARNING, "Error when opening logs file:\n" + e.getMessage());
		}
	}
	
}