package ecofish.interface_magento.daos;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import org.ini4j.Wini;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.scene.control.Alert;

/**
 * Setting up the connection to the database
 * @author Jean-Florian Tassart
 */
public class DataSourceFactory {

	private static MysqlDataSource dataSource;
	
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	public static String getUser() {
		return dataSource.getUser();
	}
	
	public static void initDatabase() {
		dataSource = new MysqlDataSource();
		try {
			Wini ini = new Wini(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "config.ini"));
			String server = ini.get("database_test", "server");
			String port = ini.get("database_test", "port");
			String name = ini.get("database_test", "name");
			dataSource.setURL("jdbc:mysql://" + server + ":" + port + "/" + name);
			goAuthentification();
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
		dataSource.setUser(username);
		dataSource.setPassword(password);
		return true;
	}	
	
	public static void goAuthentification() {
		StageService.showOnSecondaryStage(Views.LoginScreen, true);
	}
	
	public static String getCustomMessageSQLException(SQLException error) {
		//System.out.println(error.getErrorCode());
		if (error.getErrorCode() == 0) return "Connection to the database is not possible";
		if (error.getErrorCode() == 1044) return "You are not authorized to access this database";
		if (error.getErrorCode() == 1045) return "Incorrect login information";
		return error.getMessage();
	}
	
}
