package ecofish.interface_magento.view;

import javafx.fxml.FXML;

import java.io.IOException;

import ecofish.interface_magento.service.StageService;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class MainLayoutController {
	
	@FXML
	MenuItem importDataMenuItem;
	
	@FXML
	MenuItem exportDataMenuItem;
	
	@FXML
	MenuItem changeUserMenuItem;
	
	@FXML
	SeparatorMenuItem separatorMenuItem;
	
	String pathFolder;
	
	public void closeApplication() {
		StageService.closeStage();
	}

	public void changeUser() {
	}

	public void exportData() {
	}
	
	
	
	
	public void importData() throws IOException{
	}
	
	
	
	
	public void updateMenu() {	
	}
	
	/*private void visibleMenu(boolean visible) {
		importDataMenuItem.setVisible(visible);
		exportDataMenuItem.setVisible(visible);
		changeUserMenuItem.setVisible(visible);
		separatorMenuItem.setVisible(visible);
	}*/

}