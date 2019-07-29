package ecofish.interface_magento;

import java.io.IOException;

import ecofish.interface_magento.daos.DataSourceFactory;
import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views.viewsPrimaryStage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class of application launch
 * @author Jean-Florian Tassart
 */
public class InterfaceMagento extends Application {

	/**
	 * Initialization of the application
	 */
	@Override
	public void start(Stage primaryStage) {
		StageService.setStageProvidesByApplication(primaryStage);
		//if (DataSourceFactory.goAuthentification()) ProductService.loadProduct();

		DataSourceFactory.loadConnectionInformation();
		DataSourceFactory.setNewUser("a941109_ecofishg", "@;B!U0{YOpPK");
		//DetailedProduct.newDetailedProduct(new Product("1", "2", "3", "4", "5", "6", 2.48, false));
		ProductService.loadProduct();
		
		//StageService.showView(viewsPrimaryStage.DetailsProductOverview);
		//StageService.showView(viewsPrimaryStage.test);
	}

	/**
	 * Calling the main method javafx start to launch the application
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		launch(args);
	}
	
}
