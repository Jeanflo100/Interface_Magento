package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.SQLException;
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
	
	private Boolean isNewUser;
	
	/**
	 * Initialization of variables
	 */
	private DataSourceFactory() {
		dataSource = new MysqlDataSource();
		currentUser = new SimpleStringProperty();
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
	
	/**
	 * Returns the currently authenticated user
	 * @return the currently authenticated user
	 */
	public static SimpleStringProperty getCurrentUser() {
		return DataSourceFactoryHolder.INSTANCE.currentUser;
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
