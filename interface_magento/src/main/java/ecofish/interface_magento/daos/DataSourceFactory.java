package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Setting up the connection to the database
 * @author Jean-Florian Tassart
 */
public class DataSourceFactory {

	private final MysqlDataSource dataSource;
	private final SimpleStringProperty currentUser;
	private final ArrayList<HashMap<String, String>> currentUserPrivileges;
	
	private Boolean isNewUser;
	
	/**
	 * Initialization of variables
	 */
	private DataSourceFactory() {
		dataSource = DatabaseAccess.getInformationConnection();
		currentUser = new SimpleStringProperty();
		currentUserPrivileges = new ArrayList<HashMap<String, String>>();
	}
	
	/**
	 * Opens the window allowing a new user to authenticate himself
	 * @return True if a new user has been authenticated, false else
	 */
	public static Boolean goAuthentification() {
		DataSourceFactoryHolder.INSTANCE.isNewUser = false;
		StageService.showView(Views.viewsSecondaryStage.LoginScreen, true);
		return DataSourceFactoryHolder.INSTANCE.isNewUser;
	}
	
	/**
	 * Checks if the user has the desired privilege
	 * @param action - desired action
	 * @param database - database in which the action is to be performed
	 * @param table - table in which the action is to be performed
	 * @param column - column in which the action is to be performed
	 * @return True if the user has the desired privilege, false else
	 */
	protected static Boolean checkPrivilege(String action, String table, String column) {
		for (HashMap<String, String> privilege : getCurrentUserPrivileges()) {
			if (privilege.get("privilege").equals(action)) {
				if (privilege.get("database") == null) {
					return true;				}
				else {
					if (privilege.get("table") == null) {
						return true;					}
					else if (privilege.get("table").equals(table)) {
						if (privilege.get("column") == null) {
							return true;						}
						else if (privilege.get("column").equals(column)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Provides the name of the database 
	 * @return Name of the database
	 */
	protected static String getDatabaseName() {
		return DataSourceFactoryHolder.INSTANCE.dataSource.getDatabaseName();
	}
	
	/**
	 * Provides a connection to the database
	 * @return Connection to the database
	 * @throws SQLException - error when connecting
	 */
	protected static Connection getConnection() throws SQLException{
		return DataSourceFactoryHolder.INSTANCE.dataSource.getConnection();
	}
	
	/**
	 * Provides a connection to the database using an username and a password
	 * @param username - username to use to the connection
	 * @param password - password to use to the connection
	 * @return Connection to the database
	 * @throws SQLException - error when connecting
	 */
	protected static Connection getConnection(String username, String password) throws SQLException {
		return DataSourceFactoryHolder.INSTANCE.dataSource.getConnection(username, password);
	}
	
	/**
	 * Returns the currently authenticated user
	 * @return the currently authenticated user
	 */
	public static ObservableStringValue getCurrentUser() {
		return DataSourceFactoryHolder.INSTANCE.currentUser;
	}
	
	/**
	 * Returns the user's privileges
	 * @return the user's privileges
	 */
	private static ArrayList<HashMap<String, String>> getCurrentUserPrivileges(){
		return DataSourceFactoryHolder.INSTANCE.currentUserPrivileges;
	}
	
	/**
	 * Recording of new user information
	 * @param username - username of new user
	 * @param password - password of new user
	 */
	protected static void setNewUser(String username, String password) {
		DataSourceFactoryHolder.INSTANCE.dataSource.setUser(username);
		DataSourceFactoryHolder.INSTANCE.dataSource.setPassword(password);
    	DataSourceFactoryHolder.INSTANCE.currentUser.set(username);
		DataSourceFactoryHolder.INSTANCE.isNewUser = true;
		Logging.getLogger().log(Level.INFO, "Connection of " + DataSourceFactory.getCurrentUser().getValue());
	}
	
	/**
	 * Privileges owned by the user
	 * @param privileges
	 */
	protected static void setCurrentUserPrivileges(Collection<HashMap<String, String>> privileges) {
		getCurrentUserPrivileges().clear();
		getCurrentUserPrivileges().addAll(privileges);
	}
	
	/**
	 * Returns a custom message according to the connection errors.
	 * @param error - error concerned
	 * @return Custom message
	 */
	protected static String getCustomMessageFailureConnection(SQLException error) {
		Logging.getLogger().log(Level.CONFIG, "Error when connecting to database:\n" + error.getMessage());
		if (error.getErrorCode() == 1044) return "You are not authorized to access this database";
		else if (error.getErrorCode() == 1049) return "Database not founded.\nPlease check the name of the database registered in the configuration file";
		else if (error.getSQLState().equals("28000")) return "Incorrect login information";
		else if (error.getSQLState().equals("08S01")) return "Unable to connect to the database. Please try again";
		else return "Unexpected error. Please try again";
	}
	
	/**
	 * Shows a custom alert if the current user has privilege problems and proposes to change it
	 * @return True if a new user have been authenticated, false else
	 */
	protected static Boolean showAlertProblemPrivileges() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.initOwner(StageService.getSecondaryStage());
		alert.setHeaderText("You don't have the required privileges to perform this action");
		alert.setContentText("Do you want to change the user?");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> option = alert.showAndWait();
		StageService.closeSecondaryStage();
		if (option.get() == ButtonType.YES) return DataSourceFactory.goAuthentification();
		return false;
	}
	
	/**
	 * Shows a custom alert if an SQLException is returned by the database
	 * @param headerText - custom message to display in the alert
	 * @return True if the user want to retry the request to the database, false else
	 */
	protected static Boolean showAlertErrorSQL(String headerText) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(StageService.getSecondaryStage());
		alert.setHeaderText(headerText);
		alert.setContentText("An unexpected error occurred in the database.\n" + "Do you want to try again?\n" + "(You can also open the logs to get more details on the cause of the error)");
		ButtonType openLogs = new ButtonType("Open logs");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, openLogs);
		Optional<ButtonType> option = alert.showAndWait();
		while (option.get() == openLogs) {
			Logging.openLoggingFile();
			alert.setResult(ButtonType.NO);
			option = alert.showAndWait();
		}
		StageService.closeSecondaryStage();
		if (option.get() == ButtonType.YES) return true;
		return false;
	}
	
	/**
	 * Make the class static
	 * @author Jean-Florian Tassart
	 */
	private static class DataSourceFactoryHolder {
		private static DataSourceFactory INSTANCE = new DataSourceFactory();
	}
	
}
