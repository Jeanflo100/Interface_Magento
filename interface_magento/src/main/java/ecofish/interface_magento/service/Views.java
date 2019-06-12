package ecofish.interface_magento.service;

import java.io.IOException;

import ecofish.interface_magento.InterfaceMagento;
import javafx.fxml.FXMLLoader;

/**
 * List of FXML files to get application views
 * @author Jean-Florian Tassart
 */
public class Views {
	
	public static enum viewsPrimaryStage{MainLayout, StatusProductOverview, PriceProductOverview;};
	public static enum viewsSecondaryStage{LoginScreen, LoadingProduct;};	

	/**
	 * Load an instance of the view for primary stage
	 * @param view - view to show
	 * @return Instance of the view
	 */
	protected static <T> T getView(viewsPrimaryStage view) {
		FXMLLoader loader = getLoader(view.name());
		return loader.getRoot();
	}
	
	/**
	 * Load an instance of the view for secondary stage
	 * @param view - view to show
	 * @return Instance of the view
	 */
	protected static <T> T getView(viewsSecondaryStage view) {
		FXMLLoader loader = getLoader(view.name());
		return loader.getRoot();
	}
	
	/**
	 * Load the FXML file from the requested view
	 * @param view - name of view to retrieve
	 * @return the file loaded of the view
	 */
	private static FXMLLoader getLoader(String view) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(InterfaceMagento.class.getResource("view/" + view + ".fxml"));
			loader.load();
			return loader;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
}