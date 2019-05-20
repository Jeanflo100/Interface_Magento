package ecofish.interface_magento.service;

import java.io.IOException;
import java.util.Hashtable;

import ecofish.interface_magento.InterfaceMagento;
import javafx.fxml.FXMLLoader;

public class ViewService {
	
	public static String actualView;
	private static Boolean newView = false;
	private static Hashtable<String, FXMLLoader> views = new Hashtable<>(); 		// Possiblement réutiliser avec un parametre "nouveau" pour si recreer une instance ou reprendre la precedente

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
		//return getLoader(id).getRoot();
	}

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
	
	public static void clearViews() {
		views.clear();
	}
	
	public static Boolean isNewView() {
		return newView;
	}

}
