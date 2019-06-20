package ecofish.interface_magento.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

/**
 * View controller associated on view of the exchanges with the database
 * @author Jean-Florian Tassart
 */
public class LoadingProductController {
	
	@FXML
	ProgressBar loadingProductProgressBar;
	
	@FXML
	Text loadingProductText;
	
	private static DoubleProperty valueLoadingProductProgressBar;
	
	private static StringProperty valueLoadingProductText;

	/**
	 * Links the view components to the ProductService class
	 */
	@FXML
	public void initialize() {
		valueLoadingProductProgressBar = new SimpleDoubleProperty(0.0);
		valueLoadingProductText = new SimpleStringProperty("");
		this.loadingProductProgressBar.progressProperty().bind(valueLoadingProductProgressBar);
		this.loadingProductText.textProperty().bind(valueLoadingProductText);
	}
	
	/**
	 * Update the advance of progress bar
	 * @param value - current advancement
	 */
	public static void updateLoadingProductProgressBar(Double value) {
		valueLoadingProductProgressBar.set(value);
	}
	
	/**
	 * Update the action in progress
	 * @param value - action in progress
	 */
	public static void updateLoadingProductText(String value) {
		valueLoadingProductText.set(value);
	}
	
}