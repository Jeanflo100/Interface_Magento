package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.logging.Level;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import ecofish.interface_magento.view.LoadingProductController;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Updating products to the database
 * @author Jean-Florian Tassart
 */
public class UpdatingProductThread implements Runnable {

	private TreeSet<Product> updatingProducts;
	private TreeSet<Product> updatedProducts;
    private Integer nb_products;
    private Integer nb_update_products;
    private String updatedProductsLog;
	private String separatorLog;
    private Boolean error;
    
    /**
     * Initialization of parameters
     */
    public UpdatingProductThread() {    	
    	StageService.showView(Views.viewsSecondaryStage.LoadingProduct, false);
    	LoadingProductController.updateLoadingProductProgressBar(0.0);
    	LoadingProductController.updateLoadingProductText("Updating Products...");

    	this.updatingProducts = ProductService.getUpdatingProducts();
    	this.updatedProducts = new TreeSet<Product>();
    	
    	this.nb_products = this.updatingProducts.size();
    	this.nb_update_products = 0;
    	this.updatedProductsLog = "";
    	this.separatorLog = " | ";
    	this.error = false;
    }
 
    /**
     * Checks if the user has the necessary privileges and then updates the products if he has them, suggests changing users otherwise
     */
    public void run() {
    	if (privilegeChecking()) {
    		updatingProducts();
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
    	if (!DataSourceFactory.checkPrivilege("UPDATE", "products", "actual_price")) return false;
    	if (!DataSourceFactory.checkPrivilege("UPDATE", "products", "active")) return false;
    	return true;
    }
    
    /**
     * Updating products
     */
    private void updatingProducts() {
    	try {
			//Connection connection = DataSourceFactory.getDataSource().getConnection();
    		Connection connection = DataSourceFactory.getConnection();
			Statement stmt = connection.createStatement();
			for (Product product : updatingProducts) {
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
					this.updatedProductsLog += "\n" + product.getIdProduct() + " (" + product.getCategory() + " - " + product.getFamily() + " - " + product.getName()
												+ " - " + product.getSize() + " - " + product.getQuality() + "): ";
					if (product.getChangeActive() == true) {
						this.updatedProductsLog += "Status = " + (product.getActive() == true ? "not active" : "active") + " -> " + (product.getActive() == true ? "active" : "not active") + this.separatorLog;
						product.setChangeActive(false);
					}
					if (product.getNewPrice() != null) {
						this.updatedProductsLog += "Price = " + product.getActualPrice() + "£ -> " + product.getNewPrice() + "£" + this.separatorLog;
						product.setActualPrice(product.getNewPrice());
						product.setNewPrice(null);
					}
					updatedProductsLog = updatedProductsLog.substring(0, updatedProductsLog.lastIndexOf(separatorLog));
					this.updatedProducts.add(product);
				}
				this.nb_update_products += 1;
				LoadingProductController.updateLoadingProductProgressBar((double)this.nb_update_products/this.nb_products);
			}
			stmt.close();
			connection.close();
		}
		catch (SQLException e) {
			Logging.getLogger().log(Level.SEVERE, "Error when updating products list:\n" + e.getMessage());
			error = true;
		}
    	finally {
    		if (!this.updatedProducts.isEmpty()) {
    			for (Product product : this.updatedProducts) {
    				this.updatingProducts.remove(product);
    			}
    			ProductService.setUpdatingProducts(updatingProducts);
    			Logging.getLogger().log(Level.INFO, this.nb_update_products + "/" + this.nb_products + " products have been updated: " + updatedProductsLog);
    		}
    	}
    }
 
    /**
     * Update of the interface according to the result of the product updating
     */
    private void updateInterface() {
    	Platform.runLater(() -> {
			if (error) {
				if (DataSourceFactory.showAlertErrorSQL("Error when updating products list:\n" + this.nb_update_products + "/" + this.nb_products + " products have been updated")) ProductService.updateProduct();
				else {
					if (this.nb_update_products != 0) StageService.clearViewPrimaryStage();
					StageService.showView(Views.viewsPrimaryStage.PriceProductOverview);
				}
			}
			else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setHeaderText("Success in updating products");
				alert.setContentText(this.nb_update_products + "/" + this.nb_products + " products have been updated");
				alert.showAndWait();
				StageService.closeSecondaryStage();
				StageService.clearViewPrimaryStage();
				StageService.showView(Views.viewsPrimaryStage.StatusProductOverview);
			}
        });
    }
    
    /**
     * Shows the custom alert associated with a privilege problem and relaunch the updating of products if the user has been successfully changed
     */
    private void problemPrivileges() {
    	Platform.runLater(() -> {
	    	if (DataSourceFactory.showAlertProblemPrivileges()) ProductService.updateProduct();
    	});
    }
    
}