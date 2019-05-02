package ecofish.interface_magento.service;

import java.io.IOException;

import ecofish.interface_magento.InterfaceMagento;
import javafx.fxml.FXMLLoader;

public class ViewService {
	
	public static String actualView;

	public static <T> T getView(String id) {
		actualView = id;
		return getLoader(id).getRoot();
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

}
