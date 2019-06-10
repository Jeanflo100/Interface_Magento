package ecofish.interface_magento.service;

import java.io.IOException;

import ecofish.interface_magento.InterfaceMagento;
import javafx.fxml.FXMLLoader;

/**
 * Load FXML files to get application views
 * @author Jean-Florian Tassart
 */
public enum Views {
	LoginScreen,
	LoadingProduct,
	MainLayout,
	StatusProductOverview,
	PriceProductOverview;

	/**
	 * Load an instance of the view or provide the stored one if there is
	 * @param id - name of view to show
	 * @return Instance of the view
	 */
	public static <T> T getView(Views view) {
		FXMLLoader loader;
		loader = getLoader(view);
		return loader.getRoot();
	}
	
	/**
	 * Load the FXML file from the requested view
	 * @param id - name of view to retrieve
	 * @return the file loaded of the view
	 */
	private static FXMLLoader getLoader(Views view) {
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