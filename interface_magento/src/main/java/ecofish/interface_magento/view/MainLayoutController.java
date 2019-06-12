package ecofish.interface_magento.view;

import ecofish.interface_magento.daos.DataSourceFactory;
import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * View controller associated with the menu bar
 * @author Jean-Florian Tassart
 */
public class MainLayoutController {
	
	@FXML
	Label currentUserLabel;
	
	@FXML
	private void initialize() {
		currentUserLabel.textProperty().bind(DataSourceFactory.getCurrentUser());
	}
	
	public void changeUser(){
		DataSourceFactory.goAuthentification();
	}
	
	/**
	 * Shows the view with products status
	 */
	public void showStatusProductView(){
		StageService.showView(Views.viewsPrimaryStage.StatusProductOverview);
	}
	
	/**
	 * Shows the view with products price 
	 */
	public void showPriceProductView(){
		StageService.showView(Views.viewsPrimaryStage.PriceProductOverview);
	}

	/**
	 * Closes the application
	 */
	public void closeApplication() {
		StageService.closePrimaryStage();
	}

	/**
	 * Open the logs file
	 */
	public void openLogsFile() {
		Logging.openLoggingFile();
	}
	
}