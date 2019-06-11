package ecofish.interface_magento.daos;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import org.ini4j.Wini;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

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
		try {
			Wini ini = new Wini(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "config.ini"));
			String server = ini.get("database_test", "server");
			String port = ini.get("database_test", "port");
			String name = ini.get("database_test", "name");
			DataSourceFactoryHolder.INSTANCE.dataSource.setURL("jdbc:mysql://" + server + ":" + port + "/" + name);
		}
		catch (IOException e){
			Logging.LOGGER.log(Level.WARNING, "Error when recovering database connection data:\n" + e.getMessage());
			// Afficher un message d'erreur indiquant le non-accès aux informations de connexion (et donc ne peut pas aller plus loin)
		}
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
	
	public static String getCustomMessageSQLException(SQLException error) {
		//System.out.println(error.getErrorCode());
		if (error.getErrorCode() == 0) return "Connection to the database is not possible.";
		if (error.getErrorCode() == 1044) return "You are not authorized to access this database.";
		if (error.getErrorCode() == 1045) return "Incorrect login information.";
		if (error.getErrorCode() == 1142) return "You are not authorized to perform this action.";
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
