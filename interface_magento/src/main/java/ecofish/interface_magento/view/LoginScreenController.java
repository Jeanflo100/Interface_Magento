package ecofish.interface_magento.view;

import ecofish.interface_magento.daos.DataSourceFactory;
import ecofish.interface_magento.daos.DatabaseAccess;
import ecofish.interface_magento.service.StageService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jean-Florian Tassart
 */
public class LoginScreenController {
	
	@FXML
	AnchorPane loginScreenAnchorPane;
	
	@FXML
	TextField usernameTextField;
	
	@FXML
	PasswordField passwordPasswordField;
	
	@FXML
	private void initialize() {
		this.loginScreenAnchorPane.setOnKeyPressed(keyEvent -> {
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
	
	@FXML
	private void getInformationConnection() {
		if (!this.usernameTextField.getText().isEmpty()) {
			Boolean result = DataSourceFactory.setUser(this.usernameTextField.getText(), this.passwordPasswordField.getText());
			if (result) {
				closeWindow();				
			}
			else {
				this.passwordPasswordField.selectAll();
				this.passwordPasswordField.requestFocus();
			}
		}
	}
	
	@FXML
	private void openConfigurationFile() {
		DatabaseAccess.openConfigurationFile();
	}
	
	@FXML
	private void closeWindow() {
		this.passwordPasswordField.clear();
		this.usernameTextField.clear();
		this.usernameTextField.requestFocus();
		StageService.closeSecondaryStage();
	}
	
}