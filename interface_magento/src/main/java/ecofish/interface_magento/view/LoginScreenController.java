package ecofish.interface_magento.view;

import ecofish.interface_magento.daos.DataSourceFactory;
import ecofish.interface_magento.service.StageService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * 
 * @author Jean-Florian Tassart
 */
public class LoginScreenController {
	
	@FXML
	TextField usernameTextField;
	
	@FXML
	PasswordField passwordPasswordField;
	
	@FXML
	private void initialize() {
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
				this.usernameTextField.clear();
				this.passwordPasswordField.clear();
				closeWindow();				
			}
		}
	}
	
	@FXML
	private void closeWindow() {
		StageService.closeSecondaryStage();
	}
	
}