package ecofish.interface_magento.service;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GlobalDetails {
	
	ObservableList<String> productionTypes;
	
	private GlobalDetails() {
		productionTypes = FXCollections.observableArrayList();
	}
	
	public static void setProductionTypes(ArrayList<String> productionTypes) {
		GlobalDetailsHolder.INSTANCE.productionTypes.addAll(productionTypes);
	}
	
	public static ObservableList<String> getProductionTypes() {
		return GlobalDetailsHolder.INSTANCE.productionTypes;
	}
	
	private static class GlobalDetailsHolder {
		private static GlobalDetails INSTANCE = new GlobalDetails();
	}
	
}