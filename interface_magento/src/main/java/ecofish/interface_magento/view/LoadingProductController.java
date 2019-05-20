package ecofish.interface_magento.view;

import ecofish.interface_magento.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;


public class LoadingProductController {
	
	@FXML
	public ProgressBar loadingProductProgressBar;
	
	@FXML
	public Text loadingProductText;

	@FXML
	public void initialize() {
		System.out.println("Loading products");
		ProductService.setLoadingComponents(loadingProductProgressBar, loadingProductText);
	}
	
}