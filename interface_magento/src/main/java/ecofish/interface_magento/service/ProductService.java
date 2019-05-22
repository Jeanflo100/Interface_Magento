package ecofish.interface_magento.service;

import ecofish.interface_magento.model.Product;

import java.util.ArrayList;
import java.util.Optional;

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
	
	private ArrayList<Product> products;
	private ObservableList<Product> activeProducts;
	private ObservableList<Product> inactiveProducts;
	
	private DoubleProperty loadingProductProgressBar;
	private StringProperty loadingProductText;
	
	/*private static String currentCategory;
	private static String currentFamily;*/
	// + faire getters et setters --> generaliser cela
	
	public static ArrayList<Product> getProducts() {
		return ProductServiceHolder.INSTANCE.products;
	}
	
	public static DoubleProperty getLoadingProductProgressBar() {
		return ProductServiceHolder.INSTANCE.loadingProductProgressBar;
	}
	
	public static StringProperty getLoadingProductText() {
		return ProductServiceHolder.INSTANCE.loadingProductText;
	}
	
	private ProductService() {
		products = new ArrayList<Product>();
		activeProducts = FXCollections.observableArrayList();
		inactiveProducts = FXCollections.observableArrayList();
		loadingProductProgressBar = new SimpleDoubleProperty(0.0);
		loadingProductText = new SimpleStringProperty("");
	}
	
	public static void loadProduct() {
		LoadingProductThread loadingProduct = new LoadingProductThread();
		new Thread(loadingProduct).start();
	}
	
	public static void updateProduct() {
		UpdatingProductThread updatingProduct = new UpdatingProductThread();
		new Thread(updatingProduct).start();
	}
	
	public static ObservableList<Product> getActiveProducts(String category, String family){
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
	
	/*public static ObservableList<Product> getActiveProducts(String category, String family){
		// requête SQL afin d'avoir les produicts actifs avec filtrage possible sur catégorie et famille
		ProductServiceHolder.INSTANCE.activeProducts.clear();
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			String sqlQuery = "SELECT * FROM product WHERE product.active = true";
			if (category != null) {
				sqlQuery += " and product.category = \"" + category + "\"";
				if (family != null) {
					sqlQuery += " and product.family = \"" + family + "\"";
				}
			}
			System.out.println(sqlQuery);
			try(Statement statement = connection.createStatement()){
				try(ResultSet resultSet = statement.executeQuery(sqlQuery)){
					while(resultSet.next()) {
						Product product = new Product(
								resultSet.getInt("idproduct"),
								resultSet.getString("name"),
								resultSet.getString("category"),
								resultSet.getString("family"),
								resultSet.getString("quality"),
								resultSet.getString("size"),
								resultSet.getDouble("actual_price"),
								resultSet.getBoolean("active"));
						ProductServiceHolder.INSTANCE.activeProducts.add(product);
					}
				}
			}
		}
		catch (SQLException e){
			System.out.println("Error when getting active products list");
		}
		return ProductServiceHolder.INSTANCE.activeProducts;
	}
	
	public static ObservableList<Product> getInactiveProducts(String category, String family){
		// requête SQL afin d'avoir les produicts actifs avec filtrage possible sur catégorie et famille
		ProductServiceHolder.INSTANCE.inactiveProducts.clear();
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			String sqlQuery = "SELECT * FROM product WHERE product.active = false";
			if (category != null) {
				sqlQuery += " and product.category = \"" + category + "\"";
				if (family != null) {
					sqlQuery += " and product.family = \"" + family + "\"";
				}
			}
			System.out.println(sqlQuery);
			try(Statement statement = connection.createStatement()){
				try(ResultSet resultSet = statement.executeQuery(sqlQuery)){
					while(resultSet.next()) {
						Product product = new Product(
								resultSet.getInt("idproduct"),
								resultSet.getString("name"),
								resultSet.getString("category"),
								resultSet.getString("family"),
								resultSet.getString("quality"),
								resultSet.getString("size"),
								resultSet.getDouble("actual_price"),
								resultSet.getBoolean("active"));
						ProductServiceHolder.INSTANCE.inactiveProducts.add(product);
					}
				}
			}
		}
		catch (SQLException e){
			System.out.println("Error when getting inactive products list");
		}
		return ProductServiceHolder.INSTANCE.inactiveProducts;
	}*/
	
}