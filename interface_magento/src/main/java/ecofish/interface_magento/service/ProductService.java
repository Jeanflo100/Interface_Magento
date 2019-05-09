package ecofish.interface_magento.service;

import ecofish.interface_magento.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductService{
	
	private ObservableList<Product> products;
	private ObservableList<Product> activeProducts;
	private ObservableList<Product> inactiveProducts;
	
	private ProductService() {
		products = FXCollections.observableArrayList();
		activeProducts = FXCollections.observableArrayList();
		inactiveProducts = FXCollections.observableArrayList();
	}
	
	public static ObservableList<Product> getProducts(String category, String family) {
		// requête SQL afin d'avoir les différents produits pour la famille et la catégorie sélectionée
		// Supprimer ancien produits lorsque changements
		
		ProductServiceHolder.INSTANCE.products.clear();
		if (category == "Agrumes") {
			if (family == "Orange") {
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "2", "I", 0.53, true));
			}
			else if (family == "Citron") {
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "2", "II", 1.04, true));
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Washington", "2", "I", 0.83, true));
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "3", "II", 1.54, false));
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "4", "II", 2.16, true));
			}
		}
		else if (category == "Fruits à noyau") {
			if (family == "Mandarine") {
				ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "2", "II", 1.04, false));
			}
		}
		return ProductServiceHolder.INSTANCE.products;
	}
	
	public static ObservableList<Product> getActiveProducts(String category, String family){
		// requête SQL afin d'avoir les produicts actifs avec filtrage possible sur catégorie et famille
		ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Navelina", "4", "II", 2.16, true));
		ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Washington", "3", "III", 2.02, true));
		ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Navelina", "4", "I", 1.84, true));
		ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Washington", "5", "II", 2.48, true));
		return ProductServiceHolder.INSTANCE.activeProducts;
	}
	
	public static ObservableList<Product> getInactiveProducts(String category, String family){
		// requête SQL afin d'avoir les produicts inactifs avec filtrage possible sur catégorie et famille
		ProductServiceHolder.INSTANCE.inactiveProducts.add(new Product("Orange Navelina", "2", "II", 1.42, false));
		ProductServiceHolder.INSTANCE.inactiveProducts.add(new Product("Orange Washington", "1", "I", 1.23, false));
		return ProductServiceHolder.INSTANCE.inactiveProducts;
	}
	
	public static void updateStatusProduct(Product product) {
		product.setActive(!product.getActive());
		if (product.getActive()) {
			ProductServiceHolder.INSTANCE.inactiveProducts.remove(product);
			ProductServiceHolder.INSTANCE.activeProducts.add(product);
		}
		else {
			ProductServiceHolder.INSTANCE.inactiveProducts.add(product);
			ProductServiceHolder.INSTANCE.activeProducts.remove(product);
		}
	}
	
	private static class ProductServiceHolder {
		private static ProductService INSTANCE = new ProductService();
	}
	
}