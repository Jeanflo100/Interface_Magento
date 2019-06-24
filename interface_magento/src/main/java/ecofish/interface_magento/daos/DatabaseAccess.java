package ecofish.interface_magento.daos;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.ini4j.Wini;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ecofish.interface_magento.log.Logging;
import javafx.scene.control.Alert;

/**
 * Exchanging with the configuration file
 * @author Jean-Florian Tassart
 */
public class DatabaseAccess	{
	
	private static final File configFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "config_test.ini");
	
	/**
	 * Retrieve database connection information
	 * @return Variable allowing initialization of connections
	 */
	protected static MysqlDataSource getInformationConnection() {
		MysqlDataSource dataSource = new MysqlDataSource();
		try {
			Wini ini = new Wini(configFile);
			String server = ini.get("database", "server");
			String port = ini.get("database", "port");
			String name = ini.get("database", "name");
			dataSource.setURL("jdbc:mysql://" + server + ":" + port + "/" + name);
			dataSource.setServerName(server);
			try {
				dataSource.setPort(Integer.parseInt(port));
			} catch (NumberFormatException e) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setHeaderText("Error when recovering database connection data");
				alert.setContentText("The port specified in the configuration file is invalid.\n"
										+ "Please correct this and restart the application.\n"
										+ "(Access to the configuration file by clicking on the database file icon on the authentication screen that follows)");
				alert.showAndWait();
			}
			dataSource.setDatabaseName(name);
		}
		catch (IOException e){
			Logging.getLogger().log(Level.WARNING, "Error when recovering database connection data: " + "Impossible to open the configuration file");
			String message = "";
			if (!configFile.exists() && createDefaultConfigurationFile()) {
				message = "Configuration file not founded.\n"
						+ "A new one has been created at the expected location.\n"
						+ "It must now be initialized and the application will have to restart to recover the changes.\n"
						+ "(Access to the configuration file by clicking on the database file icon on the authentication screen that follows)";
			}
			else {
				message = "Unable to open the configuration file and/or create a new one.\n"
						+ "Retry the action or restart the application if the problem persists";
			}
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Error when recovering database connection data");
			alert.setContentText(message);
			alert.showAndWait();	
		}
		return dataSource;
	}
	
	/**
	 * Create a default configuration file if none exists
	 * @return True is the file has been created, false else
	 */
	private static Boolean createDefaultConfigurationFile() {
		try {
			configFile.createNewFile();
			try {
				Wini ini = new Wini(configFile);
				ini.setComment("If modifications are made, the application will have to be restarted to retrieve them");
				ini.put("database", "server", null);
				ini.put("database", "port", null);
				ini.put("database", "name", null);
				ini.store();
				return true;
			} catch (IOException e) {
				Logging.getLogger().log(Level.CONFIG, "Error when writing in configuration file:\n" + e.getMessage());
			}
		} catch (IOException e) {
			Logging.getLogger().log(Level.CONFIG, "Error when creation of configuration file:\n" + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Opens the configuration file
	 */
	public static void openConfigurationFile() {
		try {
			if (!configFile.exists()) createDefaultConfigurationFile();
			Desktop.getDesktop().open(configFile);
		} catch (IOException e) {
			Logging.getLogger().log(Level.CONFIG, "Error when opening logs file:\n" + e.getMessage());
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Error when opening configuration file");
			alert.setContentText("Retry the action or restart the application if the problem persists");
			alert.showAndWait();
		}
	}
	
}