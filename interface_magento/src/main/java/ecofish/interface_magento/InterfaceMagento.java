package ecofish.interface_magento;

import java.io.IOException;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;
import javafx.application.Application;
import javafx.stage.Stage;

public class InterfaceMagento extends Application {

	public InterfaceMagento() {

	}

	@Override
	public void start(Stage primaryStage) {
		StageService.initPrimaryStage(primaryStage);
		StageService.showView(ViewService.getView("HomeScreen"));
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}
}
