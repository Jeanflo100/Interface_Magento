package ecofish.interface_magento.daos;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.ini4j.Wini;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import javafx.scene.control.Alert;

/**
 * Exchanging with the configuration file
 * @author Jean-Florian Tassart
 */
public class DatabaseAccess	{
	
	private static final File configFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "config_test.ini");

	private static final String generalComment = "If modifications are made, the application will have to be restarted to retrieve them";	
	private static final String databaseSection = "database";
	private static final String serverVariable = "server";
	private static final String portVariable = "port";
	private static final String nameVariable = "name";
	
	private static String server;
	private static String port;
	private static String name;
	
	private static Boolean newConfigFile;
	
	/**
	 * Initialization of database connection information
	 * @return Variable allowing initialization of connections
	 */
	protected static MysqlDataSource getConnectionInformation() {
		if (readingConfigurationFile()) {
			if (server != null && port != null && name != null && !server.isEmpty() && !port.isEmpty() && !name.isEmpty()) {
				MysqlDataSource dataSource = new MysqlDataSource();
				dataSource.setURL("jdbc:mysql://" + server + ":" + port + "/" + name);
				dataSource.setServerName(server);
				try {
					dataSource.setPort(Integer.parseInt(port));
				} catch (NumberFormatException e) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.initOwner(StageService.getSecondaryStage());
					alert.setHeaderText("Error when recovering database connection data");
					alert.setContentText("The port specified in the configuration file is invalid.\n"
											+ "Please correct this and try again.\n"
											+ "--> Access to the configuration file by clicking on the database file icon on the authentication screen");
					alert.showAndWait();
					return null;
				}
				dataSource.setDatabaseName(name);
				return dataSource;
			}
			else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setHeaderText("Error when when recovering database connection data");
				alert.setContentText("Some connection information is incomplete.\n"
									+ "Please specify them in the configuration file before attempting a connection.\n"
									+ "--> Access to the configuration file by clicking on the database file icon on the authentication screen");
				alert.showAndWait();
			}
		}
		return null;
	}
	
	/**
	 * Checks if the file information is compliant and retrieve the connection information
	 */
	private static Boolean readingConfigurationFile() {
		newConfigFile = false;
		
		try {
			if (!configFile.exists()) {
				configFile.createNewFile();
				newConfigFile = true;
			}
		} catch (IOException e) {
			Logging.getLogger().log(Level.CONFIG, "Error when creating the configuration file:\n" + e.getMessage());
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(StageService.getSecondaryStage());
			alert.setHeaderText("Error when creating the configuration file\n");
			alert.setContentText("Configuration file not founded  and a new one failed to be created.\n"
								+ "Please try again or restart the application if the problems persists");
			alert.showAndWait();
			return false;
		}
		
		try {
			Wini ini = new Wini(configFile);
			
			server = ini.get(databaseSection, serverVariable);
			port = ini.get(databaseSection, portVariable);
			name = ini.get(databaseSection, nameVariable);
			
			if (!generalComment.equals(ini.getComment())) ini.setComment(generalComment);
			if (server == null || server.isEmpty()) ini.put(databaseSection, serverVariable, null);
			if (port == null || port.isEmpty()) ini.put(databaseSection, portVariable, null);
			if (name == null || name.isEmpty()) ini.put(databaseSection, nameVariable, null);
			ini.store();
		}
		catch (IOException e) {
			Logging.getLogger().log(Level.CONFIG, "Error when opening the configuration file:\n" + e.getMessage());
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(StageService.getSecondaryStage());
			alert.setHeaderText("Error when opening the configuration file\n");
			alert.setContentText("Impossible to recover connection information.\n"
								+ "Please try again or restart the application if the problems persists");
			alert.showAndWait();
			newConfigFile = false;
			return false;
		}
			
		if (newConfigFile) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(StageService.getSecondaryStage());
			alert.setHeaderText("Error when accessing the configuration file");
			alert.setContentText("Configuration file not founded.\n"
								+ "A new configuration file has been created at the expected location.\n"
								+ "Please initialize it before attempting a connection.\n"
								+ "--> Access to the configuration file by clicking on the database file icon on the authentication screen");
			alert.showAndWait();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Opens the configuration file
	 */
	public static void openConfigurationFile() {
		if (readingConfigurationFile() || newConfigFile) {
			try {
				Desktop.getDesktop().open(configFile);
			} catch (IOException e) {
				Logging.getLogger().log(Level.CONFIG, "Error when opening configuration file:\n" + e.getMessage());
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setHeaderText("Error when opening configuration file");
				alert.setContentText("Please try again or restart the application if the problems persists");
				alert.showAndWait();
			}
		}
	}
	
}