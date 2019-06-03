package ecofish.interface_magento.view;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;

/**
 * View controller associated with the menu bar
 * @author Jean-Florian Tassart
 */
public class MainLayoutController {
	
	/**
	 * Shows the view with the status of the products 
	 */
	public void showStatusProductView(){
		StageService.showView(ViewService.getView("StatusProductOverview"));
	}
	
	/**
	 * Shows the view with the price of the products 
	 */
	public void showPriceProductView(){
		StageService.showView(ViewService.getView("PriceProductOverview"));
	}

	/**
	 * Closes the application
	 */
	public void closeApplication() {
		StageService.closeStage();
	}

	/**
	 * Open the logs file
	 */
	public void openLogsFile() {
		Logging.openLoggingFile();
	}
	
}