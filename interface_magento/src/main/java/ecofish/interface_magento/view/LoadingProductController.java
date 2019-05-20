package ecofish.interface_magento.view;


import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;


public class LoadingProductController {
	
	@FXML
	ProgressBar loadingProductProgressBar;
	
	@FXML
	Text loadingProductText;
	
	public void showStatusProductView(){
		StageService.showView(ViewService.getView("StatusProductOverview"));
	}
	
	public void showPriceProductView(){
		StageService.showView(ViewService.getView("PriceProductOverview"));
	}

	public void closeApplication() {
		StageService.closeStage();
	}

}