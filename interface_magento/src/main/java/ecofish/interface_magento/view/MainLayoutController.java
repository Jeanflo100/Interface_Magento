package ecofish.interface_magento.view;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;

/**
 * View controller associated with the menu bar
 * @author Jean-Florian Tassart
 */
public class MainLayoutController {
	
	/**
	 * Shows the view with products status
	 */
	public void showStatusProductView(){
		StageService.showOnPrimaryStage(Views.StatusProductOverview);
	}
	
	/**
	 * Shows the view with products price 
	 */
	public void showPriceProductView(){
		StageService.showOnPrimaryStage(Views.PriceProductOverview);
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