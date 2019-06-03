package ecofish.interface_magento.view;

import ecofish.interface_magento.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

/**
 * View controller associated with exchanges with the database
 * @author Jean-Florian Tassart
 */
public class LoadingProductController {
	
	@FXML
	public ProgressBar loadingProductProgressBar;
	
	@FXML
	public Text loadingProductText;

	/**
	 * Links the view monitoring components to the ProductService class
	 */
	@FXML
	public void initialize() {
		this.loadingProductProgressBar.progressProperty().bind(ProductService.getLoadingProductProgressBar());
		this.loadingProductText.textProperty().bind(ProductService.getLoadingProductText());
	}
	
}