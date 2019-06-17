package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.FilterService;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Thread retrieving products from the database
 * @author Jean-Florian Tassart
 */
public class LoadingProductThread implements Runnable {

	private ArrayList<Product> activeProducts;
	private ArrayList<Product> inactiveProducts;
	private TreeMap<String, TreeSet<String>> groups;
    private DoubleProperty loadingProductProgressBar;
    private StringProperty loadingProductText;   
    private SQLException error;
 
    /**
     * Initialization of parameters
     */
    public LoadingProductThread() {
    	this.activeProducts = ProductService.getActiveProducts();
    	this.inactiveProducts = ProductService.getInactiveProducts();
    	this.groups = FilterService.getGroups();
    	this.loadingProductProgressBar = ProductService.getLoadingProductProgressBar();
    	this.loadingProductText = ProductService.getLoadingProductText();
    	
    	this.loadingProductProgressBar.set(0.0);
    	this.loadingProductText.set("Loading Products...");

    	this.error = null;
    	
    	StageService.showView(Views.viewsSecondaryStage.LoadingProduct, false);
    }
 
    /**
     * Loads products then display a window of success or failure
     */
    public void run() {
		if (privilegeChecking()) {
	    	loadingProducts();
			updateInterface();
		}
		else {
			changeUser();
		}
    }
    
    private Boolean privilegeChecking() {
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "idproduct")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "name")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "category")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "family")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "quality")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "size")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "actual_price")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "ecofish products", "products", "active")) return false;
    	return true;
    }
    
    private void loadingProducts() {
    	try {
    		Connection connection = DataSourceFactory.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			
			ResultSet retour = statement.executeQuery("SELECT COUNT(*) AS nb_products FROM product");
			retour.next();
			Integer nb_products = retour.getInt("nb_products");
			Integer nb_loading_products = 0;
			TreeSet<String> familySet;

			ResultSet resultSet = statement.executeQuery("SELECT * FROM product");
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
				if (product.getActive()) this.activeProducts.add(product);
				else this.inactiveProducts.add(product);

				String category = resultSet.getString("category");
				String family = resultSet.getString("family");
				familySet = groups.containsKey(category) ? groups.get(category) : new TreeSet<>();
				familySet.add(family);
				groups.put(category, familySet);
				
				nb_loading_products += 1;
				loadingProductProgressBar.set((double)nb_loading_products/nb_products);
			}
		}
		catch (SQLException e){
			Logging.LOGGER.log(Level.WARNING, "Error when getting products list:\n" + e.getMessage());
			error = e;
		}
    }
    
    private void updateInterface() {
    	Platform.runLater(() -> {
			if (error != null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setHeaderText("Error when getting products list");
				alert.setContentText("An unexpected error occurred in the database.\n" + "Do you want to try again?\n" + "(You can also open the logs to get more details on the cause of the error)");
				ButtonType openLogs = new ButtonType("Open logs");
				alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, openLogs);
				Optional<ButtonType> option = alert.showAndWait();
				while (option.get() == openLogs) {
					Logging.openLoggingFile();
					option = alert.showAndWait();
				}
				StageService.closeSecondaryStage();
				if (option.get() == ButtonType.YES) ProductService.loadProduct();
			}
			else {
				StageService.showView(Views.viewsPrimaryStage.StatusProductOverview);
				StageService.closeSecondaryStage();				
			}
        });
    }
    
    private void changeUser() {
    	Platform.runLater(() -> {
	    	Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(StageService.getSecondaryStage());
			alert.setHeaderText("You don't have the required privileges to perform this action");
			alert.setContentText("Do you want to change the user?");
			alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
			Optional<ButtonType> option = alert.showAndWait();
			StageService.closeSecondaryStage();
			if (option.get() == ButtonType.YES) {
				if (DataSourceFactory.goAuthentification()) {
					ProductService.loadProduct();
				}
			}
    	});
    }
    
}