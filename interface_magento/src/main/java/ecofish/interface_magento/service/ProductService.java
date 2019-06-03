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

/**
 * Supplies the different products
 * @author Jean-Florian Tassart
 */
public class ProductService {

	private TreeSet<Product> updatingProducts;
	private ArrayList<Product> activeProducts;
	private ArrayList<Product> inactiveProducts;
	private ObservableList<Product> activeProductsFiltered;
	private ObservableList<Product> inactiveProductsFiltered;
	
	private DoubleProperty loadingProductProgressBar;
	private StringProperty loadingProductText;
	
	/**
	 * Provide the instance of the loading bar
	 * @return Instance of the loading bar
	 */
	public static DoubleProperty getLoadingProductProgressBar() {
		return ProductServiceHolder.INSTANCE.loadingProductProgressBar;
	}
	
	/**
	 * Provide the instance of the text accompanying the loading bar
	 * @return Instance of the text accompanying the loading bar
	 */
	public static StringProperty getLoadingProductText() {
		return ProductServiceHolder.INSTANCE.loadingProductText;
	}
	
	/**
	 * Initialization of parameters
	 */
	private ProductService() {
		updatingProducts = new TreeSet<Product>();
		activeProducts = new ArrayList<Product>();
		inactiveProducts = new ArrayList<Product>();
		activeProductsFiltered = FXCollections.observableArrayList();
		inactiveProductsFiltered = FXCollections.observableArrayList();
		loadingProductProgressBar = new SimpleDoubleProperty(0.0);
		loadingProductText = new SimpleStringProperty("");
	}
	
	/**
	 * Launching the product loading thread
	 */
	public static void loadProduct() {
		LoadingProductThread loadingProduct = new LoadingProductThread();
		new Thread(loadingProduct).start();
	}
	
	/**
	 * Launching the product updating thread
	 */
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
	
	/**
	 * Updates the list of products to be updated for the product passed as parameter
	 * @param product - product to be updated in the list
	 */
	public static void updateUpdatingProducts(Product product) {
		if (product.getChangeActive() == false && product.getNewPrice() == null) {
			ProductServiceHolder.INSTANCE.updatingProducts.remove(product);
		}
		else {
			ProductServiceHolder.INSTANCE.updatingProducts.add(product);
		}
	}
	
	/**
	 * Returns the list of products to be updated
	 * @return List of products to be updated
	 */
	public static TreeSet<Product> getUpdatingProducts() {
		return ProductServiceHolder.INSTANCE.updatingProducts;
	}
	
	/**
	 * Returns the list of products with active status
	 * @return List of products with active status
	 */
	public static ArrayList<Product> getActiveProducts(){
		return ProductServiceHolder.INSTANCE.activeProducts;
	}
	
	/**
	 * Returns the list of products with inactive status
	 * @return List of products with inactive status
	 */
	public static ArrayList<Product> getInactiveProducts(){
		return ProductServiceHolder.INSTANCE.inactiveProducts;
	}
	
	/**
	 * Filters the list of active products by category and family then returns the result
	 * @param category - filtering by category
	 * @param family - filtering by family
	 * @return The list after filtering
	 */
	public static ObservableList<Product> getActiveProductsFiltered(String category, String family){
		return getProductsFiltered(ProductServiceHolder.INSTANCE.activeProductsFiltered, ProductServiceHolder.INSTANCE.activeProducts, category, family);
	}
	
	/**
	 * Filters the list of inactive products by category and family then returns the result
	 * @param category - filtering by category
	 * @param family - filtering by family
	 * @return The list after filtering
	 */
	public static ObservableList<Product> getInactiveProductsFiltered(String category, String family){
		return getProductsFiltered(ProductServiceHolder.INSTANCE.inactiveProductsFiltered, ProductServiceHolder.INSTANCE.inactiveProducts, category, family);
	}
	
	/**
	 * Filters a given list of products by category and family
	 * @param productsFiltered - list containing products filtred
	 * @param products - list of products to be filtered
	 * @param category - filtering by category
	 * @param family - filtering by family
	 * @return List filtred
	 */
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
	
	/**
	 * Changes the status of the product passed as a parameter
	 * @param product - product whose status needs to be changed
	 */
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
	
	/**
	 * Get a static class
	 * @author Jean-Florian Tassart
	 */
	private static class ProductServiceHolder {
		private static ProductService INSTANCE = new ProductService();
	}
	
}