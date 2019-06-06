package ecofish.interface_magento.service;

import java.util.Optional;
import java.util.logging.Level;

import ecofish.interface_magento.InterfaceMagento;
import ecofish.interface_magento.log.Logging;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Set up window management
 * @author Jean-Florian Tassart
 */
public class StageService {

	/**
	 * Initializes the main layout
	 */
	private StageService() {
		mainLayout = ViewService.getView("MainLayout");
	}

	/**
	 * Make the class static
	 * @author Jean-Florian Tassart
	 */
	private static class StageServiceHolder {
		private static final StageService INSTANCE = new StageService();
	}
	
	private Stage primaryStage;
	private Stage secondaryStage;
	public BorderPane mainLayout;

	/**
	 * Provides the instance of main layout
	 * @return Instance of main layout
	 */
	public static BorderPane getMainLayoutBorderPane() {
		return StageServiceHolder.INSTANCE.mainLayout;
	}
	
	/**
	 * Provides the instance of main window
	 * @return Instance of main window
	 */
	public static Stage getPrimaryStage() {
		return StageServiceHolder.INSTANCE.primaryStage;
	}
	
	/**
	 * Provides the instance of secondary window
	 * @return Instance of secondary window
	 */
	public static Stage getSecondaryStage() {
		return StageServiceHolder.INSTANCE.secondaryStage;
	}

	/**
	 * Initialization of main window
	 * @param primaryStage - stage provided by the mother class
	 */
	public static void initPrimaryStage(Stage primaryStage) {
		primaryStage.setTitle("Ecofish Products");
		primaryStage.getIcons().setAll(new Image(InterfaceMagento.class.getResource("image/ecofish-logo.png").toExternalForm()));
		primaryStage.setScene(new Scene(StageServiceHolder.INSTANCE.mainLayout));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			if (!ProductService.getUpdatingProducts().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initOwner(StageService.getPrimaryStage());
				alert.setTitle("ATTENTION");
				alert.setHeaderText("Changes have not been updated in the database, continue?");
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() == ButtonType.CANCEL) {
					event.consume();
					return;
		    	}
			}
			Logging.LOGGER.log(Level.INFO, "Closing application" + "\n");
		});
		StageServiceHolder.INSTANCE.primaryStage = primaryStage;
	}
	
	/**
	 * Creation and initialization of secondary window
	 */
	public static void createSecondaryStage() {
		Stage secondaryStage = new Stage();
		secondaryStage.getIcons().setAll(new Image(InterfaceMagento.class.getResource("image/ecofish-logo.png").toExternalForm()));
		secondaryStage.initOwner(StageService.getPrimaryStage());
		secondaryStage.initModality(Modality.WINDOW_MODAL);
		secondaryStage.initStyle(StageStyle.UNDECORATED);
		secondaryStage.setScene(new Scene(ViewService.getView("LoadingProduct")));
		StageServiceHolder.INSTANCE.secondaryStage = secondaryStage;
	}

	/**
	 * Change view of primary window
	 * @param rootElementPane - new view to show in window
	 */
	public static void showView(Pane rootElementPane) {
		Node rootElementNode = rootElementPane;
		if (ViewService.isNewView()) StageServiceHolder.INSTANCE.primaryStage.hide();
		StageServiceHolder.INSTANCE.mainLayout.setPrefSize(rootElementPane.getPrefWidth(), StageServiceHolder.INSTANCE.mainLayout.getTop().getBoundsInParent().getHeight() + rootElementPane.getPrefHeight());
		StageServiceHolder.INSTANCE.mainLayout.setCenter(rootElementNode);
		StageServiceHolder.INSTANCE.primaryStage.sizeToScene();
		StageServiceHolder.INSTANCE.primaryStage.centerOnScreen();
		StageServiceHolder.INSTANCE.primaryStage.show();
	}
	
	/**
	 * Display or not the secondary stage
	 * @param show - show if true, close if false
	 */
	public static void showSecondaryStage(Boolean show) {
		if (show) {
			StageServiceHolder.INSTANCE.secondaryStage.show();
		}
		else StageServiceHolder.INSTANCE.secondaryStage.close();
	}
	
	/**
	 * Triggers the closing of the application
	 */
	public static void closeStage() {
		StageServiceHolder.INSTANCE.primaryStage.fireEvent(new WindowEvent(StageServiceHolder.INSTANCE.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

}
