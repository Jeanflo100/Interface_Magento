package ecofish.interface_magento.service;

import ecofish.interface_magento.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductService{
	
	private ObservableList<Product> products;
	private ObservableList<Product> activeProducts;
	private ObservableList<Product> inactiveProducts;
	
	/*private static String currentCategory;
	private static String currentFamily;*/
	// + faire getters et setters --> generaliser cela
	
	private ProductService() {
		products = FXCollections.observableArrayList();
		products.add(new Product("Orange Navelina", "Agrumes", "Orange", "2", "I", 0.53, true));
		products.add(new Product("Orange Navelina", "Agrumes", "Orange", "2", "II", 1.04, true));
		products.add(new Product("Orange Washington", "Agrumes", "Orange", "2", "I", 0.83, false));
		products.add(new Product("Orange Navelina", "Agrumes", "Citron", "3", "II", 1.54, false));
		products.add(new Product("Orange Navelina", "Agrumes", "Citron", "4", "II", 2.16, true));
		products.add(new Product("Orange Navelina", "Fruits à noyau", "Mandarine", "2", "II", 1.04, false));
		activeProducts = FXCollections.observableArrayList();
		inactiveProducts = FXCollections.observableArrayList();
	}
	
	/*public static ObservableList<Product> getProducts(String category, String family) {
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
	}*/
	
	/*private static void setProducts() {
		
		ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "Agrumes", "Orange", "2", "I", 0.53, true));
		ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "Agrumes", "Orange", "2", "II", 1.04, true));
		ProductServiceHolder.INSTANCE.products.add(new Product("Orange Washington", "Agrumes", "Orange", "2", "I", 0.83, false));
		ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "Agrumes", "Citron", "3", "II", 1.54, false));
		ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "Agrumes", "Citron", "4", "II", 2.16, true));
		ProductServiceHolder.INSTANCE.products.add(new Product("Orange Navelina", "Fruits à noyau", "Mandarine", "2", "II", 1.04, false));
		//return ProductServiceHolder.INSTANCE.products;
	}*/
	
	public static ObservableList<Product> getActiveProducts(String category, String family){
		// requête SQL afin d'avoir les produicts actifs avec filtrage possible sur catégorie et famille
		/*ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Navelina", "4", "II", 2.16, true));
		ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Washington", "3", "III", 2.02, true));
		ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Navelina", "4", "I", 1.84, true));
		ProductServiceHolder.INSTANCE.activeProducts.add(new Product("Orange Washington", "5", "II", 2.48, true));
		return ProductServiceHolder.INSTANCE.activeProducts;*/
		ProductServiceHolder.INSTANCE.activeProducts.clear();
		for (Product product : ProductServiceHolder.INSTANCE.products) {
			if (product.getActive().equals(true)) {
				if ((category != null) && (family != null)){
					if ((product.getCategory().contentEquals(category)) && (product.getFamily().contentEquals(family))) {
						ProductServiceHolder.INSTANCE.activeProducts.add(product);
					}
				}
				else if (category != null) {
					if (product.getCategory().equals(category)) {
						ProductServiceHolder.INSTANCE.activeProducts.add(product);
					}
				}
				else {
					ProductServiceHolder.INSTANCE.activeProducts.add(product);
				}
			}
		}
		return ProductServiceHolder.INSTANCE.activeProducts;
	}
	
	public static ObservableList<Product> getInactiveProducts(String category, String family){
		// requête SQL afin d'avoir les produicts inactifs avec filtrage possible sur catégorie et famille
		/*ProductServiceHolder.INSTANCE.inactiveProducts.add(new Product("Orange Navelina", "2", "II", 1.42, false));
		ProductServiceHolder.INSTANCE.inactiveProducts.add(new Product("Orange Washington", "1", "I", 1.23, false));
		return ProductServiceHolder.INSTANCE.inactiveProducts;*/
		ProductServiceHolder.INSTANCE.inactiveProducts.clear();
		for (Product product : ProductServiceHolder.INSTANCE.products) {
			if (product.getActive().equals(false)) {
				if ((category != null) && (family != null)){
					if ((product.getCategory().contentEquals(category)) && (product.getFamily().contentEquals(family))) {
						ProductServiceHolder.INSTANCE.inactiveProducts.add(product);
					}
				}
				else if (category != null) {
					if (product.getCategory().equals(category)) {
						ProductServiceHolder.INSTANCE.inactiveProducts.add(product);
					}
				}
				else {
					ProductServiceHolder.INSTANCE.inactiveProducts.add(product);
				}
			}
		}
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