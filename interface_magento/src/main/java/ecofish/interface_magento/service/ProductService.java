package ecofish.interface_magento.service;

import ecofish.interface_magento.model.Product;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeSet;

import ecofish.interface_magento.daos.GettingProductThread;
import ecofish.interface_magento.daos.UpdatingProductThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Supplies the different products
 * @author Jean-Florian Tassart
 */
public class ProductService {

	private final ObservableList<Product> updatingProducts;
	private final ObservableList<Product> activeProducts;
	private final ObservableList<Product> inactiveProducts;
	
	/**
	 * Initialization of parameters
	 */
	private ProductService() {
		updatingProducts = FXCollections.observableArrayList();
		activeProducts = FXCollections.observableArrayList();
		inactiveProducts = FXCollections.observableArrayList();
	}
	
	/**
	 * Launching the product loading thread
	 */
	public static void loadProduct() {
		GettingProductThread loadingProduct = new GettingProductThread();
		new Thread(loadingProduct).start();
	}
	
	/**
	 * Launching the product updating thread after checking the presence of the product to be updated
	 */
	public static void updateProduct() {
		if (!ProductServiceHolder.INSTANCE.updatingProducts.isEmpty()) {
			UpdatingProductThread updatingProduct = new UpdatingProductThread();
			new Thread(updatingProduct).start();			
		}
		else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initOwner(StageService.getPrimaryStage());
			alert.setHeaderText("No product to update");
			alert.setContentText("No product have been changed");
			alert.showAndWait();
		}
	}
	
	/**
	 * Set the list of active products
	 * @param activeProducts - the list of active products
	 */
	public static void setActiveProducts(ArrayList<Product> activeProducts){
		ProductServiceHolder.INSTANCE.activeProducts.addAll(activeProducts);
	}
	
	/**
	 * Set the list of inactive products
	 * @param - inactiveProducts - the list of inactive products
	 */
	public static void setInactiveProducts(ArrayList<Product> inactiveProducts){
		ProductServiceHolder.INSTANCE.inactiveProducts.addAll(inactiveProducts);
	}
	
	/**
	 * Initialize the list of products to be updated
	 * @param updatingProducts - list of products to be updated
	 */
	public static void setUpdatingProducts(TreeSet<Product> updatingProducts) {
		ProductServiceHolder.INSTANCE.updatingProducts.clear();
		ProductServiceHolder.INSTANCE.updatingProducts.addAll(updatingProducts);
	}
	
	/**
	 * Updates the list of products to be updated
	 * @param product - adds, leaves or removes the product from the list if updates are still present or not
	 */
	public static void updateUpdatingProducts(Product product) {
		ProductServiceHolder.INSTANCE.updatingProducts.remove(product);
		if (product.getChangeActive() == true || product.getNewPrice() != null) ProductServiceHolder.INSTANCE.updatingProducts.add(product);
	}
	
	/**
	 * Provides a filtered observable list of products that have had a status change
	 * @return A filtered observable list of products that have had a status change
	 */
	public static FilteredList<Product> getUpdatingProductsOnStatus() {
		return new FilteredList<Product>(ProductServiceHolder.INSTANCE.updatingProducts, product ->  {
			if (product.getChangeActive()) return true;
			return false;
		});
	}
	
	/**
	 * Provides a filtered observable list of products that have had a price change
	 * @return A filtered observable list of products that have had a price change
	 */
	public static FilteredList<Product> getUpdatingProductsOnPrice() {
		return new FilteredList<Product>(ProductServiceHolder.INSTANCE.updatingProducts, product ->  {
			if (product.getNewPrice() != null) return true;
			return false;
		});
	}
	
	/**
	 * Returns a copy of the list of products to be updated
	 * @return Copy of the list of products to be updated
	 */
	public static TreeSet<Product> getUpdatingProducts() {
		return new TreeSet<Product>(ProductServiceHolder.INSTANCE.updatingProducts);
	}
	
	/**
	 * Returns a copy of the list of active products which can be sorted
	 * @return Copy of the list of active products
	 */
	public static SortedList<Product> getActiveProducts() {
		return new SortedList<Product>(ProductServiceHolder.INSTANCE.activeProducts);
	}
	
	/**
	 * Returns a copy of the list of inactive products which can be sorted
	 * @return Copy of the list of inactive products
	 */
	public static SortedList<Product> getInactiveProducts() {
		return new SortedList<Product>(ProductServiceHolder.INSTANCE.inactiveProducts);
	}
	
	/**
	 * Changes the status of the product passed as a parameter
	 * If the product had a new price, a confirmation will be displayed to confirm the deletion or not of the price and therefore the change of status
	 * @param product - product whose status needs to be changed
	 */
	public static Boolean changeStatusProduct(Product product) {
		if (product.getNewPrice() != null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.initOwner(StageService.getPrimaryStage());
			alert.setHeaderText("This product has a new price.\r\n" + 
								"If the product is disabled, the new price will be deleted.\r\n" + 
								"Continue?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() != ButtonType.OK) {
				return false;
	    	}
			product.setNewPrice(null);
		}		
		product.setActive(!product.getActive());
		product.setChangeActive(!product.getChangeActive());
		if (product.getActive()) {
			ProductServiceHolder.INSTANCE.inactiveProducts.remove(product);
			ProductServiceHolder.INSTANCE.activeProducts.add(product);
		}
		else {
			ProductServiceHolder.INSTANCE.inactiveProducts.add(product);
			ProductServiceHolder.INSTANCE.activeProducts.remove(product);
		}
		ProductService.updateUpdatingProducts(product);
		return true;
	}
	
	/**
	 * Obtain a single instance managing all products
	 * @author Jean-Florian Tassart
	 */
	private static class ProductServiceHolder {
		private static ProductService INSTANCE = new ProductService();
	}
	
}