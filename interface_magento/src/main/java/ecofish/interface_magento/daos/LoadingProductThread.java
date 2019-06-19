package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.FilterService;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import ecofish.interface_magento.view.LoadingProductController;
import javafx.application.Platform;

/**
 * Thread retrieving products from the database
 * @author Jean-Florian Tassart
 */
public class LoadingProductThread implements Runnable {

	private ArrayList<Product> activeProducts;
	private ArrayList<Product> inactiveProducts;
	private TreeMap<String, TreeSet<String>> groups;
    private Boolean error;
 
    /**
     * Initialization of parameters
     */
    public LoadingProductThread() {
    	this.activeProducts = ProductService.getActiveProducts();
    	this.inactiveProducts = ProductService.getInactiveProducts();
    	this.groups = FilterService.getGroups();

    	StageService.showView(Views.viewsSecondaryStage.LoadingProduct, false);
    	LoadingProductController.updateLoadingProductProgressBar(0.0);
    	LoadingProductController.updateLoadingProductText("Loading Products...");

    	this.error = false;
    }
 
    /**
     * Checks if the user has the necessary privileges and then get the products if he has them, suggests changing users otherwise
     */
    public void run() {
		if (privilegeChecking()) {
	    	loadingProducts();
			updateInterface();
		}
		else {
			problemPrivileges();
		}
    }
    
    /**
     * Check each necessary privilege
     * @return True if the user has all necessary privileges, false else
     */
    private Boolean privilegeChecking() {
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "idproduct")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "name")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "category")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "family")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "quality")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "size")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "actual_price")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "products", "active")) return false;
    	return true;
    }
    
    /**
     * Getting produts
     */
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
				LoadingProductController.updateLoadingProductProgressBar((double)nb_loading_products/nb_products);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		catch (SQLException e){
			Logging.LOGGER.log(Level.SEVERE, "Error when getting products list:\n" + e.getMessage());
			error = true;
		}
    }
    
    /**
     * Update of the interface according to the result of the product getting
     */
    private void updateInterface() {
    	Platform.runLater(() -> {
			if (error) {
				if (DataSourceFactory.showAlertErrorSQL("Error when getting products list")) ProductService.loadProduct();
			}
			else {
				StageService.showView(Views.viewsPrimaryStage.StatusProductOverview);
				StageService.closeSecondaryStage();				
			}
        });
    }
    
    /**
     * Shows the custom alert associated with a privilege problem and relaunch the getting of products if the user has been successfully changed
     */
    private void problemPrivileges() {
    	Platform.runLater(() -> {
    		if (DataSourceFactory.showAlertProblemPrivileges()) ProductService.loadProduct();
    	});
    }
    
}