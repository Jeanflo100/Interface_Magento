package ecofish.interface_magento.view;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;

public class MainLayoutController {
	
	public void showStatusProductView(){
		StageService.showView(ViewService.getView("StatusProductOverview"));
	}
	
	public void showPriceProductView(){
		StageService.showView(ViewService.getView("PriceProductOverview"));
	}

	public void closeApplication() {
		StageService.closeStage();
	}

	public void openLogsFile() {
		Logging.openLoggingFile();
	}
	
}