package ecofish.interface_magento.service;

import java.io.IOException;
import java.util.Hashtable;

import ecofish.interface_magento.InterfaceMagento;
import javafx.fxml.FXMLLoader;

/**
 * Retrieve FXML files from application views
 * @author Jean-Florian Tassart
 */
public class ViewService {
	
	public static String actualView;
	private static Boolean newView = false;
	private static Hashtable<String, FXMLLoader> views = new Hashtable<>();

	/**
	 * Provides an instance of the view
	 * @param id - name of view to show
	 * @return An instance of the view
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
	 * Load the view required
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
	 * Indicates whether the returned view is a new loaded or an old one that was stored
	 * @return true if it's a new view, false else
	 */
	public static Boolean isNewView() {
		return newView;
	}

}
