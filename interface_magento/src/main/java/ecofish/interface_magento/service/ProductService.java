package ecofish.interface_magento.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.daos.DataSourceFactory;
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
		activeProducts = FXCollections.observableArrayList();
		inactiveProducts = FXCollections.observableArrayList();
		getProducts();
	}
	
	public void getProducts() {
		try(Connection connection = DataSourceFactory.getDataSource().getConnection()){
			String sqlQuery = "SELECT * FROM product";
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
						products.add(product);
					}
				}
			}
		}
		catch (SQLException e){
			System.out.println("Error when getting products list");
		}
	}
	
	public static void updateProducts() throws SQLException {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		for (Product product : ProductServiceHolder.INSTANCE.products) {
			if (product.getChangeActive() == true) {
				stmt.executeUpdate("UPDATE product SET product.active = " + product.getActive() +" WHERE product.idproduct = " + product.getIdProduct());
			}
		}
		stmt.close();
		connection.close();
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