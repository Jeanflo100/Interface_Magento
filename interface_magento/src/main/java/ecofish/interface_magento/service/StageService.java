package ecofish.interface_magento.service;

import java.util.Hashtable;
import java.util.Optional;

import ecofish.interface_magento.InterfaceMagento;
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
		mainLayout = Views.getView(Views.MainLayout);
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
	
	private BorderPane mainLayout;
	
	private Hashtable<Views, Pane> viewPrimaryStage = new Hashtable<>();
	private Hashtable<Views, Scene> viewSecondaryStage = new Hashtable<>();

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
		StageServiceHolder.INSTANCE.secondaryStage = secondaryStage;
	}

	/**
	 * Change view of primary window
	 * @param rootElementPane - new view to show in window
	 */
	public static void showOnPrimaryStage(Views view) {
		Pane loadedView;
		if (StageServiceHolder.INSTANCE.viewPrimaryStage.containsKey(view)) {
			loadedView = StageServiceHolder.INSTANCE.viewPrimaryStage.get(view);
		}
		else {
			loadedView = Views.getView(view);
			StageServiceHolder.INSTANCE.viewPrimaryStage.put(view, loadedView);
			StageServiceHolder.INSTANCE.primaryStage.hide();
		}
		StageServiceHolder.INSTANCE.mainLayout.setPrefSize(loadedView.getPrefWidth(), StageServiceHolder.INSTANCE.mainLayout.getTop().getBoundsInParent().getHeight() + loadedView.getPrefHeight());
		StageServiceHolder.INSTANCE.mainLayout.setCenter(loadedView);
		StageServiceHolder.INSTANCE.primaryStage.sizeToScene();
		StageServiceHolder.INSTANCE.primaryStage.centerOnScreen();
		StageServiceHolder.INSTANCE.primaryStage.show();
	}
	
	/**
	 * Change view of secondary window
	 * @param rootElementPane - new view to show in window
	 */
	public static void showOnSecondaryStage(Views view, Boolean waitAfterShow) {
		Scene loadedView;
		if (StageServiceHolder.INSTANCE.viewSecondaryStage.containsKey(view)) {
			loadedView = StageServiceHolder.INSTANCE.viewSecondaryStage.get(view);
		}
		else {
			loadedView = new Scene(Views.getView(view));
			StageServiceHolder.INSTANCE.viewSecondaryStage.put(view, loadedView);
		}
		StageServiceHolder.INSTANCE.secondaryStage.setScene(loadedView);
		StageServiceHolder.INSTANCE.secondaryStage.sizeToScene();
		StageServiceHolder.INSTANCE.secondaryStage.centerOnScreen();
		if (waitAfterShow) StageServiceHolder.INSTANCE.secondaryStage.showAndWait();
		else StageServiceHolder.INSTANCE.secondaryStage.show();
	}
	
	public static void clearViewPrimaryStage() {
		StageServiceHolder.INSTANCE.viewPrimaryStage.clear();
	}
	
	public static void closeSecondaryStage() {
		StageServiceHolder.INSTANCE.secondaryStage.close();
	}
	
	/**
	 * Triggers the closing of the application
	 */
	public static void closePrimaryStage() {
		StageServiceHolder.INSTANCE.primaryStage.fireEvent(new WindowEvent(StageServiceHolder.INSTANCE.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

}
