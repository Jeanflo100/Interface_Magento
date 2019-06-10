package ecofish.interface_magento.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
	private void getInformationConnection() {
		if (usernameTextField.getText() != null && passwordPasswordField != null) {
			
		}
	}
	
	@FXML
	private void closeWindow() {
		
	}
	
}