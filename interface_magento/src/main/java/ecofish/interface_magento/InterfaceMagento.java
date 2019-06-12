package ecofish.interface_magento;

import java.io.IOException;

import ecofish.interface_magento.daos.DataSourceFactory;
import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
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
		Logging.setLogging();
		StageService.initPrimaryStage(primaryStage);
		StageService.createSecondaryStage();
		DataSourceFactory.initDatabase();
		if (DataSourceFactory.goAuthentification()) ProductService.loadProduct();
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
