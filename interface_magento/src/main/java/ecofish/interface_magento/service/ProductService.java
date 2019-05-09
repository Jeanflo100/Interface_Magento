package ecofish.interface_magento.service;

import ecofish.interface_magento.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductService{
	
	private ObservableList<Product> products;
	
	private ProductService() {
		products = FXCollections.observableArrayList();
		/*for (int i = 0; i<1; i++) {
			products.add(new Product("Orange Navelina", "2", "I", 0.53));
			products.add(new Product("Orange Navelina", "2", "II", 1.04));
			products.add(new Product("Orange Washington", "2", "I", 0.83));
		}*/
	}
	
	public static ObservableList<Product> getProducts(String category, String family) {
		// requête SQL afin d'avoir les différents produits pour la famille et la catégorie sélectionée
		// Supprimer ancien produits lorsque changements
		
		ProductServiceHolder.INSTANCE.products.clear();
		if (category == "Agrumes") {
			if (family == "Orange") {
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "2", "I", 0.53));
			}
			else if (family == "Citron") {
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "2", "II", 1.04));
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Washington", "2", "I", 0.83));
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "3", "II", 1.54));
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "4", "II", 2.16));
			}
		}
		else if (category == "Fruits à noyau") {
			if (family == "Mandarine") {
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "2", "II", 1.04));
			}
		}
		return ProductServiceHolder.INSTANCE.products;
	}
	
	private static class ProductServiceHolder {
		private static ProductService INSTANCE = new ProductService();
	}
	
}