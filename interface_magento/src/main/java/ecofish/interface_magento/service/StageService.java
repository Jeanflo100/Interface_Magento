package ecofish.interface_magento.service;

import java.util.Hashtable;
import java.util.Optional;

import ecofish.interface_magento.InterfaceMagento;
import ecofish.interface_magento.service.Views.viewsPrimaryStage;
import ecofish.interface_magento.service.Views.viewsSecondaryStage;
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
	
	private static Stage stageApplication;
	
	private final Stage primaryStage;
	private final Stage secondaryStage;
	
	private final BorderPane mainLayout;
	private final Double baseHeightMainLayout;
	
	private final Hashtable<viewsPrimaryStage, Pane> viewPrimaryStage;
	private final Hashtable<viewsSecondaryStage, Scene> viewSecondaryStage;

	/**
	 * Set the main stage provided by the application
	 * @param stageProvidesByApplication - main stage provided by the application
	 */
	public static void setStageProvidesByApplication(Stage stageProvidesByApplication) {
		stageApplication = stageProvidesByApplication;
	}
	
	/**
	 * Initializes the parameters
	 */
	private StageService() {
		this.mainLayout = Views.getView(Views.viewsPrimaryStage.MainLayout);
		this.baseHeightMainLayout = mainLayout.getPrefHeight();
		
		primaryStage = stageApplication == null ? new Stage() : stageApplication;
		initPrimaryStage(stageApplication);
		secondaryStage = new Stage();
		initSecondaryStage(secondaryStage);
		
		this.viewPrimaryStage = new Hashtable<viewsPrimaryStage, Pane>();
		this.viewSecondaryStage = new Hashtable<viewsSecondaryStage, Scene>();
	}

	/**
	 * Provides the instance of main layout
	 * @return Instance of main layout
	 */
	/*public static BorderPane getMainLayoutBorderPane() {
		return StageServiceHolder.INSTANCE.mainLayout;
	}*/
	
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
	private void initPrimaryStage(Stage primaryStage) {
		primaryStage.setTitle("Ecofish Products");
		primaryStage.getIcons().setAll(new Image(InterfaceMagento.class.getResource("image/ecofish-logo.png").toExternalForm()));
		primaryStage.setScene(new Scene(this.mainLayout));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			if (!ProductService.getUpdatingProducts().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initOwner(StageService.getPrimaryStage());
				alert.setHeaderText("Changes have not been updated in the database, continue?");
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() == ButtonType.CANCEL) {
					event.consume();
					return;
		    	}
			}
		});
	}
	
	/**
	 * Creation and initialization of secondary window
	 */
	private void initSecondaryStage(Stage secondaryStage) {
		secondaryStage.getIcons().setAll(new Image(InterfaceMagento.class.getResource("image/ecofish-logo.png").toExternalForm()));
		secondaryStage.initOwner(this.primaryStage);
		secondaryStage.initModality(Modality.WINDOW_MODAL);
		secondaryStage.initStyle(StageStyle.UNDECORATED);
	}
	
	/**
	 * Change view of primary window
	 * @param view - view to show in primary stage
	 */
	public static void showView(viewsPrimaryStage view) {
		Pane loadedView;
		if (StageServiceHolder.INSTANCE.viewPrimaryStage.containsKey(view)) {
			loadedView = StageServiceHolder.INSTANCE.viewPrimaryStage.get(view);
		}
		else {
			loadedView = Views.getView(view);
			StageServiceHolder.INSTANCE.viewPrimaryStage.put(view, loadedView);
			StageServiceHolder.INSTANCE.primaryStage.hide();
		}
		StageServiceHolder.INSTANCE.mainLayout.setPrefSize(loadedView.getPrefWidth(), StageServiceHolder.INSTANCE.baseHeightMainLayout + loadedView.getPrefHeight());
		StageServiceHolder.INSTANCE.mainLayout.setCenter(loadedView);
		StageServiceHolder.INSTANCE.primaryStage.sizeToScene();
		StageServiceHolder.INSTANCE.primaryStage.centerOnScreen();
		StageServiceHolder.INSTANCE.primaryStage.show();
	}
	
	/**
	 * Change view of secondary window
	 * @param view - view to show in secondary stage
	 * @param waitAfterShow - True if the program must wait for an action from the window user, false else
	 */
	public static void showView(viewsSecondaryStage view, Boolean waitAfterShow) {
		Scene loadedView;
		if (StageServiceHolder.INSTANCE.viewSecondaryStage.containsKey(view)) {
			loadedView = StageServiceHolder.INSTANCE.viewSecondaryStage.get(view);
		}
		else {
			loadedView = new Scene(Views.getView(view));
			// The "LoadingProduct" view is not kept in memory otherwise, when reused, the cursor might be reset by default
			if (view != Views.viewsSecondaryStage.LoadingProduct) StageServiceHolder.INSTANCE.viewSecondaryStage.put(view, loadedView);
		}
		StageServiceHolder.INSTANCE.secondaryStage.setScene(loadedView);
		StageServiceHolder.INSTANCE.secondaryStage.sizeToScene();
		StageServiceHolder.INSTANCE.secondaryStage.centerOnScreen();
		if (waitAfterShow) StageServiceHolder.INSTANCE.secondaryStage.showAndWait();
		else StageServiceHolder.INSTANCE.secondaryStage.show();
	}
	
	/**
	 * Clear views of primary stage
	 */
	public static void clearViewPrimaryStage() {
		StageServiceHolder.INSTANCE.viewPrimaryStage.clear();
	}
	
	/**
	 * Triggers the closing of the application
	 */
	public static void closePrimaryStage() {
		StageServiceHolder.INSTANCE.primaryStage.fireEvent(new WindowEvent(StageServiceHolder.INSTANCE.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	/**
	 * Close the secondary stage
	 */
	public static void closeSecondaryStage() {
		StageServiceHolder.INSTANCE.secondaryStage.close();
	}

	/**
	 * Make the class static
	 * @author Jean-Florian Tassart
	 */
	private static class StageServiceHolder {
		private static final StageService INSTANCE = new StageService();
	}

}
