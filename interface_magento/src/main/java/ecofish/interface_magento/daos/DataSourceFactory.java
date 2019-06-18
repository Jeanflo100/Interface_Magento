package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Setting up the connection to the database
 * @author Jean-Florian Tassart
 */
public class DataSourceFactory {

	private MysqlDataSource dataSource;
	private SimpleStringProperty currentUser;
	private ArrayList<HashMap<String, String>> privileges;
	
	private Boolean isNewUser;
	
	/**
	 * Initialization of variables
	 */
	private DataSourceFactory() {
		dataSource = new MysqlDataSource();
		currentUser = new SimpleStringProperty();
		privileges = new ArrayList<HashMap<String, String>>();
	}
	
	/**
	 * Initialization of connection data to the database
	 */
	public static void initDatabase() {
		DatabaseAccess.getInformationConnection(getDataSource());	
	}
	
	/**
	 * Opens the window allowing a new user to authenticate himself
	 * @return True if a new user has been authenticated, false else
	 */
	public static Boolean goAuthentification() {
		setIsNewUser(false);
		StageService.showView(Views.viewsSecondaryStage.LoginScreen, true);
		return getIsNewUser();
	}
	
	/**
	 * Updates the connection data to the database concerning the user after authentication of this one and recovery of its global privileges and those for this database
	 * @param username - username of the user
	 * @param password - password of the user
	 * @return True if he has access to the database, false else
	 */
	public static Boolean setUser(String username, String password) {
		try {
			Connection connection = DataSourceFactory.getDataSource().getConnection(username, password);
			getPrivileges().clear();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM\n"
					+ "(SELECT NULL AS 'database', NULL AS 'table', NULL AS 'column', privilege_type AS 'privilege' FROM information_schema.USER_PRIVILEGES\n"
					+ "UNION\n"
					+ "SELECT REPLACE(table_schema,'\\_','_') AS 'database', NULL AS 'table', NULL AS 'column', privilege_type AS 'privilege' FROM information_schema.SCHEMA_PRIVILEGES\n"
					+ "UNION\n"
					+ "SELECT table_schema AS 'database', table_name AS 'table', NULL AS 'column', privilege_type AS 'privilege' FROM information_schema.TABLE_PRIVILEGES\n"
					+ "UNION\n"
					+ "SELECT table_schema AS 'database', table_name AS 'table', column_name AS 'column', privilege_type AS 'privilege' FROM information_schema.COLUMN_PRIVILEGES)\n"
					+ "AS privilegeTable WHERE (privilegeTable.database IS NULL OR privilegeTable.database = '" + getDataSource().getDatabaseName() + "')"
					);
			while (resultSet.next()) {
				HashMap<String, String> privilege = new HashMap<String, String>();
				privilege.put("database", resultSet.getString("database"));
				privilege.put("table", resultSet.getString("table"));
				privilege.put("column", resultSet.getString("column"));
				privilege.put("privilege", resultSet.getString("privilege"));
				getPrivileges().add(privilege);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(StageService.getSecondaryStage());
			alert.setTitle("WARNING");
			alert.setHeaderText("Error when connecting to database");
			alert.setContentText(getCustomMessageFailureConnection(e));
			alert.showAndWait();
			return false;
		}
		getDataSource().setUser(username);
		getDataSource().setPassword(password);
		getCurrentUser().set(username);
		setIsNewUser(true);
		Logging.LOGGER.log(Level.INFO, "Connection of " + getCurrentUser().getValue());
		return true;
	}
	
	/**
	 * Returns a custom message according to the connection errors.
	 * @param error - error concerned
	 * @return Custom message
	 */
	private static String getCustomMessageFailureConnection(SQLException error) {
		Logging.LOGGER.log(Level.CONFIG, "Error when connecting to database:\n" + error.getMessage());
		if (error.getErrorCode() == 1044) return "You are not authorized to access this database";
		else if (error.getSQLState().equals("28000")) return "Incorrect login information";
		else if (error.getSQLState().equals("08S01")) return "Unable to connect to the database. Please try again";
		else return "Unexpected error. Please try again";
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
		for (HashMap<String, String> privilege : getPrivileges()) {
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
	 * Returns the currently authenticated user
	 * @return the currently authenticated user
	 */
	public static SimpleStringProperty getCurrentUser() {
		return DataSourceFactoryHolder.INSTANCE.currentUser;
	}
	
	/**
	 * Returns the user's privileges
	 * @return the user's privileges
	 */
	private static ArrayList<HashMap<String, String>> getPrivileges(){
		return DataSourceFactoryHolder.INSTANCE.privileges;
	}
	
	/**
	 * Returns the variable used to establish connections to the database
	 * @return the variable used to establish connections to the database
	 */
	protected static MysqlDataSource getDataSource() {
		return DataSourceFactoryHolder.INSTANCE.dataSource;
	}
	
	/**
	 * Returns whether after the authentication window a new user has been authenticated
	 * @return True if it's a new user, false else
	 */
	private static Boolean getIsNewUser() {
		return DataSourceFactoryHolder.INSTANCE.isNewUser;
	}
	
	/**
	 * Updates the authentication of a new user or not
	 * @param isNewUser - true if it's a new user authenticated, false else
	 */
	private static void setIsNewUser(Boolean isNewUser) {
		DataSourceFactoryHolder.INSTANCE.isNewUser = isNewUser;
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
