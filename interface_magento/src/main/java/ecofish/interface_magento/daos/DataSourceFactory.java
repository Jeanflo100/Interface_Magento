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
	
	private DataSourceFactory() {
		dataSource = new MysqlDataSource();
		currentUser = new SimpleStringProperty();
	}
	
	public static MysqlDataSource getDataSource() {
		return DataSourceFactoryHolder.INSTANCE.dataSource;
	}
	
	public static SimpleStringProperty getCurrentUser() {
		return DataSourceFactoryHolder.INSTANCE.currentUser;
	}
	
	private static Boolean getIsNewUser() {
		return DataSourceFactoryHolder.INSTANCE.isNewUser;
	}
	
	private static void setIsNewUser(Boolean isNewUser) {
		DataSourceFactoryHolder.INSTANCE.isNewUser = isNewUser;
	}
	
	public static void initDatabase() {
		DatabaseAccess.getInformationConnection(getDataSource());	
	}
	
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
	
	public static Boolean goAuthentification() {
		setIsNewUser(false);
		StageService.showOnSecondaryStage(Views.LoginScreen, true);
		return getIsNewUser();
	}
	
	public static Boolean showAlertSQLException(SQLException error, String message) {
		Alert alert;
		if (error.getErrorCode() == 0) alert = new Alert(Alert.AlertType.WARNING, getCustomMessageSQLException(error));
		else alert = new Alert(Alert.AlertType.WARNING, getCustomMessageSQLException(error) + "\nWould you like to change the user?", ButtonType.YES, ButtonType.NO);
		alert.initOwner(StageService.getSecondaryStage());
		alert.setTitle("FAILURE");
		alert.setHeaderText(message);
		Optional<ButtonType> option = alert.showAndWait();
		StageService.closeSecondaryStage();
		if (option.get() == ButtonType.YES) return true;
		else return false;
	}
	
	public static String getCustomMessageSQLException(SQLException error) {
		//System.out.println(error.getErrorCode());
		Logging.LOGGER.log(Level.CONFIG, error.getMessage());
		if (error.getErrorCode() == 0) return "Connection to the database is not possible";
		if (error.getErrorCode() == 1044) return "You are not authorized to access this database";
		if (error.getErrorCode() == 1045) return "Incorrect login information";
		if (error.getErrorCode() == 1142 || error.getErrorCode() == 1143) return "You are not authorized to perform this action";
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
