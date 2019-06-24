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

import javafx.scene.control.Alert;

/**
 * Setting up logs writting
 * @author Jean-Florian Tassart
 */
public class Logging{
	
	private static final File logFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "Interface_Magento_test.log");
	
	private final Logger LOGGER;
	
	private Formatter logFormatter;
	private Handler logConsoleHandler;
	private Handler logFileHandler;
	
	/**
	 * Initialization of the different handlers to write logs
	 */
	private Logging() {
		this.LOGGER = Logger.getLogger(Logging.class.getName());
		setLoggingConsole();
		setLoggingFile();
	}
	
	/**
	 * Initialization of the display of logs in the console
	 */
	private void setLoggingConsole() {
		this.logConsoleHandler = new ConsoleHandler();
		this.logConsoleHandler.setLevel(Level.ALL);
		this.LOGGER.addHandler(this.logConsoleHandler);
	}
	
	/**
	 * Initialization of the display of logs in a file
	 */
	private void setLoggingFile() {
		
		this.logFormatter = new Formatter() {
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
			this.logFileHandler = new FileHandler(logFile.getPath(), 0, 1, true);
		} catch (IOException e) {
			this.LOGGER.log(Level.WARNING, "Writing logs to file isn't set");
		}
		
		if (this.logFileHandler != null) {
			this.logFileHandler.setFormatter(this.logFormatter);
			this.logFileHandler.setLevel(Level.INFO);
			this.LOGGER.addHandler(this.logFileHandler);
		}

	}
	
	public static Logger getLogger() {
		return LoggingHolder.INSTANCE.LOGGER;
	}
	
	/**
	 * Open the file containing the logs
	 */
	public static void openLoggingFile() {
		try {
			if (!logFile.exists()) logFile.createNewFile();
			Desktop.getDesktop().open(logFile);
		} catch (IOException e) {
			Logging.getLogger().log(Level.CONFIG, "Error when opening logs file:\n" + e.getMessage());
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Error when opening logs file");
			alert.setContentText("Retry the action or restart the application if the problem persists");
			alert.showAndWait();
		}
	}
	
	/**
	 * Make the class static
	 * @author Jean-Florian Tassart
	 */
	private static class LoggingHolder {
		private static Logging INSTANCE = new Logging();
	}
	
}