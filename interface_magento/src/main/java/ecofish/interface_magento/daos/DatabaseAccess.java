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
	
	private static final File configFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "config.ini");

	private static final String generalComment = "If modifications are made, the application will have to be restarted to retrieve them";	
	private static final String variableDatabase = "database";
	private static final String variableServer = "server";
	private static final String variablePort = "port";
	private static final String variableName = "name";
	private static final String defaultServer = "127.0.0.1";
	private static final String defaultPort = "3306";
	private static final String defaultName = "";
	
	private static String server;
	private static String port;
	private static String name;
	
	/**
	 * Initialization of database connection information
	 * @return Variable allowing initialization of connections
	 */
	protected static MysqlDataSource getInformationConnection() {
		readingConfigurationFile();
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setURL("jdbc:mysql://" + server + ":" + port + "/" + name);
		dataSource.setServerName(server);
		try {
			dataSource.setPort(Integer.parseInt(port));
		} catch (NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Error when recovering database connection data");
			alert.setContentText("The port specified in the configuration file is invalid.\n"
									+ "Please correct this and restart the application.\n"
									+ "--> Access to the configuration file by clicking on the database file icon on the authentication screen that follows");
			alert.showAndWait();
		}
		dataSource.setDatabaseName(name);
		return dataSource;
	}
	
	/**
	 * Checks if the file information is compliant and retrieve the connection information
	 */
	private static void readingConfigurationFile() {
		Boolean newFile = false;
		Boolean changes = false;
		try {
			if (!configFile.exists()) {
				newFile = true;
				configFile.createNewFile();
			}
			try {
				Wini ini = new Wini(configFile);
				
				server = ini.get(variableDatabase, variableServer);
				port = ini.get(variableDatabase, variablePort);
				name = ini.get(variableDatabase, variableName);
				
				if (!generalComment.equals(ini.getComment())) ini.setComment(generalComment);
				if (server == null || server.isEmpty()) {server = defaultServer; ini.put(variableDatabase, variableServer, null); changes = true;}
				if (port == null || port.isEmpty()) {port = defaultPort; ini.put(variableDatabase, variablePort, null); changes = true;}
				if (name == null || name.isEmpty()) {name = defaultName; ini.put(variableDatabase, variableName, null); changes = true;}
				ini.store();
			}
			catch (IOException e) {
				server = defaultServer;
				port = defaultPort;
				name = defaultName;
				Logging.getLogger().log(Level.CONFIG, "Error when creating the configuration file:\n" + e.getMessage());
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setHeaderText("Error when opening the configuration file\n");
				alert.setContentText("Impossible to recover connection data.\n"
									+ "The default login information will be used.\n"
									+ "--> Server: " + defaultServer + ", Port: " + defaultPort + ", Database: " + defaultName + "\n"
									+ "\n"
									+ "Please restart the application to try to recover them again");
				alert.showAndWait();
			}
			
		} catch (IOException e) {
			server = defaultServer;
			port = defaultPort;
			name = defaultName;
			Logging.getLogger().log(Level.CONFIG, "Error when creating the configuration file:\n" + e.getMessage());
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Error when creating the configuration file\n");
			alert.setContentText("Configuration file not founded  and a new one failed to be created.\n"
								+ "The default login information will be used.\n"
								+ "--> Server: " + defaultServer + ", Port: " + defaultPort + ", Database: " + defaultName + "\n"
								+ "\n"
								+ "Please restart the application to try to recreate it again");
			alert.showAndWait();
		}
		if (newFile) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Error when when accessing the configuration file");
			alert.setContentText("Configuration file not founded.\n"
								+ "The default login information will be used.\n"
								+ "--> Server: " + defaultServer + ", Port: " + defaultPort + ", Database: " + defaultName + "\n"
								+ "\n"
								+ "A new configuration file has been created at the expected location.\n"
								+ "It must now be initialized and the application will have to restart to recover the changes.\n"
								+ "--> Access to the configuration file by clicking on the database file icon on the authentication screen that follows");
			alert.showAndWait();
		}
		else if (changes) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Error when when recovering database connection data");
			alert.setContentText("Some connection information is incomplete and their default value will be used.\n"
								+ "--> Server: " + defaultServer + ", Port: " + defaultPort + ", Database: " + defaultName + "\n"
								+ "\n"
								+ "Please specify them in the configuration file then restart the program to take them into account.\n"
								+ "--> Access to the configuration file by clicking on the database file icon on the authentication screen that follows");
			alert.showAndWait();
		}
	}
	
	/**
	 * Opens the configuration file
	 */
	public static void openConfigurationFile() {
		if (!configFile.exists()) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(StageService.getSecondaryStage());
			alert.setHeaderText("Error when accessing the configuration file");
			alert.setContentText("Retry the action or restart the application if the problem persists.\n"
								+ "If the file has been deleted, a new default file will be created on restart");
			alert.showAndWait();
		}
		else {
			try {
				Desktop.getDesktop().open(configFile);
			} catch (IOException e) {
				Logging.getLogger().log(Level.CONFIG, "Error when opening logs file:\n" + e.getMessage());
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setHeaderText("Error when opening configuration file");
				alert.setContentText("Retry the action or restart the application if the problem persists");
				alert.showAndWait();
			}
		}
	}
	
}