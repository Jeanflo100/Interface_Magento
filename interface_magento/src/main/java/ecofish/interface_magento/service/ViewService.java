package ecofish.interface_magento.service;

import java.io.IOException;
import java.util.Hashtable;

import ecofish.interface_magento.InterfaceMagento;
import javafx.fxml.FXMLLoader;

/**
 * Load FXML files to get application views
 * @author Jean-Florian Tassart
 */
public class ViewService {
	
	public static String actualView;
	private static Boolean newView = false;
	private static Hashtable<String, FXMLLoader> views = new Hashtable<>();

	/**
	 * Load an instance of the view or provide the stored one if there is
	 * @param id - name of view to show
	 * @return Instance of the view
	 */
	public static <T> T getView(String id) {
		actualView = id;
		FXMLLoader loader;
		if (views.containsKey(id)) {
			loader = views.get(id);
			newView = false;
		}
		else {
			loader = getLoader(id);
			views.put(id, loader);
			newView = true;
		}
		return loader.getRoot();
	}

	/**
	 * Load the FXML file from the requested view
	 * @param id - name of view to retrieve
	 * @return the file loaded of the view
	 */
	private static FXMLLoader getLoader(String id) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(InterfaceMagento.class.getResource("view/" + id + ".fxml"));
			loader.load();
			return loader;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * Clear instances of stored views
	 */
	public static void clearViews() {
		views.clear();
	}
	
	/**
	 * Indicates whether the last returned view is a new one loaded or an old one which was stored
	 * @return true if it's a new view, false else
	 */
	public static Boolean isNewView() {
		return newView;
	}

}
