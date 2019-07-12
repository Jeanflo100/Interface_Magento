package ecofish.interface_magento.daos;

import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import ecofish.interface_magento.view.LoadingProductController;
import javafx.application.Platform;

public class GettingDetailedProductThread implements Runnable {
	
	private final DetailedProduct detailedProduct;
	
	public GettingDetailedProductThread() {
		detailedProduct = DetailedProduct.getProduct();
		
    	StageService.showView(Views.viewsSecondaryStage.LoadingProduct, false);
    	LoadingProductController.updateLoadingProductProgressBar(0.0);
    	LoadingProductController.updateLoadingProductText("Retrieving product details...");
	}
	
	public void run() {
		if (privilegeChecking()) {
			retrieveDetailsProduct();
			updateInterface();
		}
		else {
			problemPrivileges();
		}
	}
	
    /**
     * Check each necessary privilege
     * @return True if the user has all necessary privileges, false else
     */
    private Boolean privilegeChecking() {
    	return true;
    }
    
    private void retrieveDetailsProduct() {
    	
    }
    
    private void updateInterface() {
    	
    }
    
    private void problemPrivileges() {
    	Platform.runLater(() -> {
    		if (DataSourceFactory.showAlertProblemPrivileges()) {
    			GettingDetailedProductThread gettingDetailedProductThread = new GettingDetailedProductThread();
    			new Thread(gettingDetailedProductThread).start();
    		}
    	});
    }
    
}