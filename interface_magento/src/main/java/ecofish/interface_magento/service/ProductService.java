package ecofish.interface_magento.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.daos.DataSourceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

public class ProductService {
	
	private ObservableList<Product> products;
	private ObservableList<Product> activeProducts;
	private ObservableList<Product> inactiveProducts;
	
	private ProgressBar loadingProductProgressBar;
	private Text loadingProductText;
	
	/*private static String currentCategory;
	private static String currentFamily;*/
	// + faire getters et setters --> generaliser cela
	
	public static void setLoadingComponents(ProgressBar progressBar, Text text) {
		ProductServiceHolder.INSTANCE.loadingProductProgressBar = progressBar;
		ProductServiceHolder.INSTANCE.loadingProductText = text;
	}
	
	private ProductService() {
		products = FXCollections.observableArrayList();
		activeProducts = FXCollections.observableArrayList();
		inactiveProducts = FXCollections.observableArrayList();
	}
	
	public static void getProducts() {
		if (ProductServiceHolder.INSTANCE.loadingProductProgressBar != null) ProductServiceHolder.INSTANCE.loadingProductProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		if (ProductServiceHolder.INSTANCE.loadingProductText != null) ProductServiceHolder.INSTANCE.loadingProductText.setText("Loading Products...");
		StageService.showSecondaryStage(true);
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			String sqlQuery = "SELECT * FROM product";
			try(Statement statement = connection.createStatement()){
				try(ResultSet retour = statement.executeQuery("SELECT COUNT(*) AS nb_products FROM product")) {
					retour.next();
					Integer nb_products = retour.getInt("nb_products");
					Integer nb_loading_products = 0;
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
							ProductServiceHolder.INSTANCE.products.add(product);
							nb_loading_products += 1;
							if (ProductServiceHolder.INSTANCE.loadingProductProgressBar != null) {
								ProductServiceHolder.INSTANCE.loadingProductProgressBar.setProgress((double)nb_loading_products/nb_products);
							}
						}
						
					}
				}
			}
		}
		catch (SQLException e){
			System.out.println("Error when getting products list");
		}
		StageService.showSecondaryStage(false);
	}
	
	public static void updateDatabase() throws SQLException {
		if (ProductServiceHolder.INSTANCE.loadingProductProgressBar != null) ProductServiceHolder.INSTANCE.loadingProductProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		if (ProductServiceHolder.INSTANCE.loadingProductText != null) ProductServiceHolder.INSTANCE.loadingProductText.setText("Update Products...");
		StageService.showSecondaryStage(true);
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		Integer nb_products = ProductServiceHolder.INSTANCE.products.size();
		Integer nb_update_products = 0;
		for (Product product : ProductServiceHolder.INSTANCE.products) {
			if (product.getChangeActive() == true || product.getNewPrice() != null) {
				String SQLquery = "UPDATE product SET";
				if (product.getChangeActive() == true) {
					SQLquery += " product.active = " + product.getActive() + ",";
				}
				if (product.getNewPrice() != null) {
					SQLquery +=  " product.actual_price = " + product.getNewPrice() + ",";
				}
				SQLquery = SQLquery.substring(0, SQLquery.length()-1) +  " WHERE product.idproduct = " + product.getIdProduct();
				stmt.executeUpdate(SQLquery);
				if (product.getChangeActive() == true) {
					product.setChangeActive(false);
				}
				if (product.getNewPrice() != null) {
					product.setActualPrice(product.getNewPrice());
					product.setNewPrice(null);
				}
			}
			nb_update_products += 1;
			if (ProductServiceHolder.INSTANCE.loadingProductProgressBar != null) {
				ProductServiceHolder.INSTANCE.loadingProductProgressBar.setProgress((double)nb_update_products/nb_products);
			}
		}
		stmt.close();
		connection.close();
		StageService.showSecondaryStage(false);
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