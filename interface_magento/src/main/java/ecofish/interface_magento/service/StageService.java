package ecofish.interface_magento.service;

import ecofish.interface_magento.InterfaceMagento;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class StageService {

	private StageService() {
		mainLayout = ViewService.getView("MainLayout");

	}

	private static class StageServiceHolder {
		private static final StageService INSTANCE = new StageService();
	}
	
	private Stage primaryStage;
	private Stage secondaryStage;
	public BorderPane mainLayout;

	public static BorderPane getMainLayoutBorderPane() {
		return StageServiceHolder.INSTANCE.mainLayout;
	}
	
	public static Stage getPrimaryStage() {
		return StageServiceHolder.INSTANCE.primaryStage;
	}
	
	public static Stage getSecondaryStage() {
		return StageServiceHolder.INSTANCE.secondaryStage;
	}

	public static void initPrimaryStage(Stage primaryStage) {
		primaryStage.setTitle("Ecofish Products");
		primaryStage.getIcons().setAll(new Image(InterfaceMagento.class.getResource("image/ecofish-logo.png").toExternalForm()));
		primaryStage.setScene(new Scene(StageServiceHolder.INSTANCE.mainLayout));
		primaryStage.setResizable(false);
		StageServiceHolder.INSTANCE.primaryStage = primaryStage;
	}
	
	public static void createSecondaryStage() {
		Stage secondaryStage = new Stage();
		secondaryStage.getIcons().setAll(new Image(InterfaceMagento.class.getResource("image/ecofish-logo.png").toExternalForm()));
		secondaryStage.initOwner(StageService.getPrimaryStage());
		secondaryStage.initModality(Modality.WINDOW_MODAL);
		secondaryStage.initStyle(StageStyle.UNDECORATED);
		secondaryStage.setScene(new Scene(ViewService.getView("LoadingProduct")));
		StageServiceHolder.INSTANCE.secondaryStage = secondaryStage;
	}

	public static void showView(Pane rootElementPane) {
		Node rootElementNode = rootElementPane;
		if (ViewService.isNewView()) StageServiceHolder.INSTANCE.primaryStage.hide();
		StageServiceHolder.INSTANCE.mainLayout.setPrefSize(rootElementPane.getPrefWidth(), StageServiceHolder.INSTANCE.mainLayout.getTop().getBoundsInParent().getHeight() + rootElementPane.getPrefHeight());
		StageServiceHolder.INSTANCE.mainLayout.setCenter(rootElementNode);
		StageServiceHolder.INSTANCE.primaryStage.sizeToScene();
		StageServiceHolder.INSTANCE.primaryStage.centerOnScreen();
		StageServiceHolder.INSTANCE.primaryStage.show();
	}
	
	public static void showSecondaryStage(Boolean show) {
		if (show) {
			StageServiceHolder.INSTANCE.secondaryStage.show();
		}
		else StageServiceHolder.INSTANCE.secondaryStage.close();
	}
	
	public static void closeStage() {
		StageServiceHolder.INSTANCE.primaryStage
				.fireEvent(new WindowEvent(StageServiceHolder.INSTANCE.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

}
