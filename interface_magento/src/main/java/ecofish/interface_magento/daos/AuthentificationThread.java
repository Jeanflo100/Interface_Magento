package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.view.LoginScreenController;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Thread retrieving products from the database
 * @author Jean-Florian Tassart
 */
public class AuthentificationThread implements Runnable {

	private LoginScreenController loginScreen;
	private String username;
	private String password;
	private SQLException error;
	
    /**
     * Initialization of parameters
     * @param loginScreen 
     * @param username 
     * @param password 
     */
    public AuthentificationThread(LoginScreenController loginScreen, String username, String password) {
    	this.loginScreen = loginScreen;
    	this.username = username;
    	this.password = password;
    	this.error = null;
    }
 
    /**
     * Checks if the user has the necessary privileges and then get the products if he has them, suggests changing users otherwise
     */
    public void run() {
    	authentification();
    	if (error == null) updateCurrentUser();
    	updateInterface();
    }
    
    /**
     * Getting produts
     */
    private void authentification() {
    	try {
			Connection connection = DataSourceFactory.getDataSource().getConnection(this.username, this.password);
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
					+ "AS privilegeTable WHERE (privilegeTable.database IS NULL OR privilegeTable.database = '" + DataSourceFactory.getDataSource().getDatabaseName() + "')"
					);
			ArrayList<HashMap<String, String>> privileges = new ArrayList<HashMap<String, String>>();
			while (resultSet.next()) {
				HashMap<String, String> privilege = new HashMap<String, String>();
				privilege.put("database", resultSet.getString("database"));
				privilege.put("table", resultSet.getString("table"));
				privilege.put("column", resultSet.getString("column"));
				privilege.put("privilege", resultSet.getString("privilege"));
				privileges.add(privilege);
			}
			DataSourceFactory.changeCurrentUserPrivileges(privileges);
			statement.close();
			connection.close();
		} catch (SQLException e) {
			this.error = e;
		}
    }
    
    private void updateCurrentUser() {
    	DataSourceFactory.getDataSource().setUser(username);
    	DataSourceFactory.getDataSource().setPassword(password);
    	DataSourceFactory.setIsNewUser(true);
    	Platform.runLater(() -> {
        	DataSourceFactory.getCurrentUser().set(username);
    		Logging.LOGGER.log(Level.INFO, "Connection of " + DataSourceFactory.getCurrentUser().getValue());
    	});
    }
    
    /**
     * Update of the interface according to the result of the authentification
     */
    private void updateInterface() {
    	Platform.runLater(() -> {
    		if (error != null) {
    			Alert alert = new Alert(Alert.AlertType.WARNING);
    			alert.initOwner(StageService.getSecondaryStage());
    			alert.setHeaderText("Error when connecting to database");
    			alert.setContentText(DataSourceFactory.getCustomMessageFailureConnection(error));
    			alert.showAndWait();
        		this.loginScreen.resultAuthentification(false);
    		}
    		else {
    			this.loginScreen.resultAuthentification(true);
    		}
    	});
    }
    
}