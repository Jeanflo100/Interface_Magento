package ecofish.interface_magento.service;

import ecofish.interface_magento.model.Product;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeSet;

import ecofish.interface_magento.daos.LoadingProductThread;
import ecofish.interface_magento.daos.UpdatingProductThread;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ProductService {

	private TreeSet<Product> updatingProducts;
	private ArrayList<Product> activeProducts;
	private ArrayList<Product> inactiveProducts;
	private ObservableList<Product> activeProductsFiltered;
	private ObservableList<Product> inactiveProductsFiltered;
	
	private DoubleProperty loadingProductProgressBar;
	private StringProperty loadingProductText;
	
	public static DoubleProperty getLoadingProductProgressBar() {
		return ProductServiceHolder.INSTANCE.loadingProductProgressBar;
	}
	
	public static StringProperty getLoadingProductText() {
		return ProductServiceHolder.INSTANCE.loadingProductText;
	}
	
	protected ProductService() {
		updatingProducts = new TreeSet<Product>();
		activeProducts = new ArrayList<Product>();
		inactiveProducts = new ArrayList<Product>();
		activeProductsFiltered = FXCollections.observableArrayList();
		inactiveProductsFiltered = FXCollections.observableArrayList();
		loadingProductProgressBar = new SimpleDoubleProperty(0.0);
		loadingProductText = new SimpleStringProperty("");
	}
	
	public static void loadProduct() {
		LoadingProductThread loadingProduct = new LoadingProductThread();
		new Thread(loadingProduct).start();
	}
	
	public static void updateProduct() {
		if (!ProductServiceHolder.INSTANCE.updatingProducts.isEmpty()) {
			UpdatingProductThread updatingProduct = new UpdatingProductThread();
			new Thread(updatingProduct).start();			
		}
		else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initOwner(StageService.getPrimaryStage());
			alert.setTitle("INFORMATION");
			alert.setHeaderText("No product to update");
			alert.setContentText("No product have been changed");
			alert.showAndWait();
		}
	}
	
	public static void updateUpdatingProducts(Product product) {
		if (product.getChangeActive() == false && product.getNewPrice() == null) {
			ProductServiceHolder.INSTANCE.updatingProducts.remove(product);
		}
		else {
			ProductServiceHolder.INSTANCE.updatingProducts.add(product);
		}
	}
	
	public static TreeSet<Product> getUpdatingProducts() {
		return ProductServiceHolder.INSTANCE.updatingProducts;
	}
	
	/*public static ObservableList<Product> getActiveProducts(String category, String family){
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
	}*/
	
	public static ArrayList<Product> getActiveProducts(){
		return ProductServiceHolder.INSTANCE.activeProducts;
	}
	
	public static ArrayList<Product> getInactiveProducts(){
		return ProductServiceHolder.INSTANCE.inactiveProducts;
	}
	
	public static ObservableList<Product> getActiveProductsFiltered(String category, String family){
		return getProductsFiltered(ProductServiceHolder.INSTANCE.activeProductsFiltered, ProductServiceHolder.INSTANCE.activeProducts, category, family);
	}
	
	public static ObservableList<Product> getInactiveProductsFiltered(String category, String family){
		return getProductsFiltered(ProductServiceHolder.INSTANCE.inactiveProductsFiltered, ProductServiceHolder.INSTANCE.inactiveProducts, category, family);
	}
	
	private static ObservableList<Product> getProductsFiltered(ObservableList<Product> productsFiltered, ArrayList<Product> products, String category, String family) {
		productsFiltered.clear();
		for (Product product : products) {
			if ((category != null) && (family != null)){
				if ((product.getCategory().contentEquals(category)) && (product.getFamily().contentEquals(family))) {
					productsFiltered.add(product);
				}
			}
			else if (category != null) {
				if (product.getCategory().equals(category)) {
					productsFiltered.add(product);
				}
			}
			else {
				productsFiltered.add(product);
			}
		}
		return productsFiltered;
	}
	
	public static void changeStatusProduct(Product product) {
		if (product.getNewPrice() != null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.initOwner(StageService.getPrimaryStage());
			alert.setTitle("WARNING");
			alert.setHeaderText("This product has a new price.\r\n" + 
								"If the product is disabled, the new price will be deleted.\r\n" + 
								"Continue?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() != ButtonType.OK) {
				return;
	    	}
			product.setNewPrice(null);
		}
		product.setActive(!product.getActive());
		product.setChangeActive(!product.getChangeActive());
		if (product.getActive()) {
			ProductServiceHolder.INSTANCE.inactiveProducts.remove(product);
			ProductServiceHolder.INSTANCE.inactiveProductsFiltered.remove(product);
			ProductServiceHolder.INSTANCE.activeProducts.add(product);
			ProductServiceHolder.INSTANCE.activeProductsFiltered.add(product);
		}
		else {
			ProductServiceHolder.INSTANCE.inactiveProducts.add(product);
			ProductServiceHolder.INSTANCE.inactiveProductsFiltered.add(product);
			ProductServiceHolder.INSTANCE.activeProducts.remove(product);
			ProductServiceHolder.INSTANCE.activeProductsFiltered.remove(product);
		}
		ProductService.updateUpdatingProducts(product);
	}
	
	private static class ProductServiceHolder {
		private static ProductService INSTANCE = new ProductService();
	}
	
}