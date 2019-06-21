package ecofish.interface_magento.view;

import ecofish.interface_magento.daos.AuthentificationThread;
import ecofish.interface_magento.daos.DatabaseAccess;
import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

/**
 * View controller associated to user authentication
 * @author Jean-Florian Tassart
 */
public class LoginScreenController {
	
	@FXML
	AnchorPane backgroundAnchorPane;
	
	@FXML
	AnchorPane componentsAnchorPane;
	
	@FXML
	TextField usernameTextField;
	
	@FXML
	PasswordField passwordPasswordField;
	
	/**
	 * Added faster navigation using the keys
	 */
	@FXML
	private void initialize() {
		this.componentsAnchorPane.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ESCAPE) {
				closeWindow();
			}
		});
		this.usernameTextField.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				getInformationConnection();
			}
		});
		this.passwordPasswordField.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				getInformationConnection();
			}
		});
	}
	
	/**
	 * Retrieve the information entered by the user to authenticate himself
	 */
	@FXML
	private void getInformationConnection() {
		if (!this.usernameTextField.getText().isEmpty()) {
			this.backgroundAnchorPane.setCursor(Cursor.WAIT);
			this.componentsAnchorPane.setDisable(true);
			AuthentificationThread authentification = new AuthentificationThread(this, this.usernameTextField.getText(), this.passwordPasswordField.getText());
			new Thread(authentification).start();
		}
	}
	
	/**
	 * Action to be done on the view according to the result of the authentication
	 * @param success - authentication result. True if success, false else
	 */
	public void resultAuthentification(Boolean success) {
		this.componentsAnchorPane.setDisable(false);
		this.backgroundAnchorPane.setCursor(Cursor.DEFAULT);
		if (success) {
			closeWindow();				
		}
		else {
			this.passwordPasswordField.selectAll();
			this.passwordPasswordField.requestFocus();
		}
	}
	
	/**
	 * Open the log file
	 */
	@FXML
	private void openLogFile() {
		Logging.openLoggingFile();
	}
	
	/**
	 * Open the configuration file
	 */
	@FXML
	private void openConfigurationFile() {
		DatabaseAccess.openConfigurationFile();
	}
	
	/**
	 * Clear the view before to close it for the next authentications
	 */
	@FXML
	private void closeWindow() {
		this.passwordPasswordField.clear();
		this.usernameTextField.clear();
		this.usernameTextField.requestFocus();
		StageService.closeSecondaryStage();
	}
	
}