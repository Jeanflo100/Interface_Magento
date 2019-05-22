package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class LoadingProductThread implements Runnable {

	private ObservableList<Product> products;
    private DoubleProperty loadingProductProgressBar;
    private StringProperty loadingProductText;
    
    private Boolean error;
 
    public LoadingProductThread() {
    	this.products = ProductService.getProducts();
    	this.loadingProductProgressBar = ProductService.getLoadingProductProgressBar();
    	this.loadingProductText = ProductService.getLoadingProductText();
    	
    	this.loadingProductProgressBar.set(0.0);
    	this.loadingProductText.set("Loading Products...");

    	this.error = false;
    	
		StageService.showSecondaryStage(true);
    }
 
    public void run() {

    	try {
    		Connection connection = DataSourceFactory.getDataSource().getConnection();
			String sqlQuery = "SELECT * FROM product";
			Statement statement = connection.createStatement();
			ResultSet retour = statement.executeQuery("SELECT COUNT(*) AS nb_products FROM product");
			retour.next();
			Integer nb_products = retour.getInt("nb_products");
			Integer nb_loading_products = 0;
			ResultSet resultSet = statement.executeQuery(sqlQuery);
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
				nb_loading_products += 1;
				loadingProductProgressBar.set((double)nb_loading_products/nb_products);
				/*try {
					Thread.sleep(30);
					System.out.println(loadingProductProgressBar);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
		catch (SQLException e){
			System.out.println("Error when getting products list");
			error = true;
		}

		Platform.runLater(() -> {
			if (error == true) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setTitle("FAILURE");
				alert.setHeaderText("Loading failure during product recovery");
				alert.showAndWait();
			}
			StageService.showView(ViewService.getView("StatusProductOverview"));
			StageService.showSecondaryStage(false);
        });
		
    }
 
}