package ecofish.interface_magento.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryService {

	private ObservableList<String> category;
	
	private CategoryService() {
		category = FXCollections.observableArrayList();
		category.add("Agrumes");
		category.add("Fruits à noyau");
	}
	
	public static ObservableList<String> getCategory(){
		// Requête SQL afin d'obtenir les différentes catégories
		return CategoryServiceHolder.INSTANCE.category;
	}
	
	private static class CategoryServiceHolder {
		private static CategoryService INSTANCE = new CategoryService();
	}
	
}
