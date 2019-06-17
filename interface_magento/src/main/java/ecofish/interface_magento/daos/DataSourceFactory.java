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
	 * Updates the connection data to the database concerning the user after authentication of this one
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
					"SELECT null AS 'database', null AS 'table', null AS 'column', privilege_type AS 'privilege' FROM information_schema.USER_PRIVILEGES\n" +
					"UNION\n" +
					"SELECT table_schema AS 'database', null AS 'table', null AS 'column', privilege_type AS 'privilege' FROM information_schema.SCHEMA_PRIVILEGES\n" +
					"UNION\n" +
					"SELECT table_schema AS 'database', table_name AS 'table', null AS 'column', privilege_type AS 'privilege' FROM information_schema.TABLE_PRIVILEGES\n" +
					"UNION\n" +
					"SELECT table_schema AS 'database', table_name AS 'table', column_name AS 'column', privilege_type AS 'privilege' FROM information_schema.COLUMN_PRIVILEGES\n"
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
			alert.setHeaderText("Error when connection to database");
			alert.setContentText(getCustomMessageSQLException(e));
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
	
	protected static Boolean checkPrivilege(String privilege_searched, String database, String table, String column) {
		for (HashMap<String, String> privilege : getPrivileges()) {
			if (privilege.get("privilege").equals(privilege_searched)) {
				if (privilege.get("database") == null) {
					return true;				}
				else if (privilege.get("database").equals(database)) {
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
	 * Shows a custom alert for SQL errors and requests the user change if the error is due to an access problem
	 * @param error - error concerned
	 * @param headerText - custom header text to display in the alert
	 * @return True if a new user must be authenticated, false else
	 */
	protected static Boolean showAlertSQLException(SQLException error, String headerText) {
		Alert alert;
		if (error.getSQLState().equals("42000")) alert = new Alert(Alert.AlertType.WARNING, getCustomMessageSQLException(error) + ".\nWould you like to change the user?", ButtonType.YES, ButtonType.NO);
		else alert = new Alert(Alert.AlertType.WARNING, getCustomMessageSQLException(error));
		alert.initOwner(StageService.getSecondaryStage());
		alert.setTitle("FAILURE");
		alert.setHeaderText(headerText);
		Optional<ButtonType> option = alert.showAndWait();
		StageService.closeSecondaryStage();
		if (option.get() == ButtonType.YES) return true;
		else return false;
	}
	
	/**
	 * Returns a custom message according to the SQL error code in first, then to the SQL state else. If none custom message exists, the default one is returned.
	 * @param error - error concerned
	 * @return Custom message
	 */
	protected static String getCustomMessageSQLException(SQLException error) {
		System.out.println(error.getErrorCode());
		System.out.println(error.getSQLState());
		System.out.println(error.getMessage());
		Logging.LOGGER.log(Level.CONFIG, error.getMessage());
		if (error.getErrorCode() == 0) return "Unable to connect to the database";
		if (error.getErrorCode() == 1044) return "You are not authorized to access this database";
		if (error.getSQLState().equals("28000")) return "Incorrect login information";
		if (error.getSQLState().equals("42000")) return "You are not authorized to perform this action";
		return error.getMessage();
	}
	
	/**
	 * Make the class static
	 * @author Jean-Florian Tassart
	 */
	private static class DataSourceFactoryHolder {
		private static DataSourceFactory INSTANCE = new DataSourceFactory();
	}
	
}
