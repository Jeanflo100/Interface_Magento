package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.view.LoginScreenController;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Thread attempting a connection with the new user entered and retrieving his privileges if there is one
 * @author Jean-Florian Tassart
 */
public class AuthentificationThread implements Runnable {

	private LoginScreenController loginScreen;
	private String username;
	private String password;
	private SQLException error;
	
    /**
     * Initialization of parameters
     * @param loginScreen - view performing the connection attempt in order to perform the appropriate actions on it according to the authentication result
     * @param username - username with which the connection is made
     * @param password - password with which the connection is made
     */
    public AuthentificationThread(LoginScreenController loginScreen, String username, String password) {
    	this.loginScreen = loginScreen;
    	this.username = username;
    	this.password = password;
    	this.error = null;
    }
 
    /**
     * Action to be performed for authentication
     */
    public void run() {
    	authentification();
    	if (error == null) Platform.runLater(() -> DataSourceFactory.setNewUser(this.username, this.password));
    	updateInterface();
    }
    
    /**
     * Attempt to connect and recover privileges if successful
     */
    private void authentification() {
    	try {
			Connection connection = DataSourceFactory.getConnection(this.username, this.password);
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
					+ "AS privilegeTable WHERE (privilegeTable.database IS NULL OR privilegeTable.database = '" + DataSourceFactory.getDatabaseName() + "')"
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