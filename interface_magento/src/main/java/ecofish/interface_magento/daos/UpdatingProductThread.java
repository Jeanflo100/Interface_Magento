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
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

/**
 * Updating products to the database
 * @author Jean-Florian Tassart
 */
public class UpdatingProductThread implements Runnable {

	private TreeSet<Product> updatingProducts;
	private TreeSet<Product> updatedProducts;
    private DoubleProperty loadingProductProgressBar;
    private StringProperty loadingProductText;
    private Integer nb_products;
    private Integer nb_update_products;
    private String updatedProductsLog;
	private String separatorLog;
    private Boolean error;
    
    /**
     * Initialization of parameters
     */
    public UpdatingProductThread() {
    	this.updatingProducts = ProductService.getUpdatingProducts();
    	this.updatedProducts = new TreeSet<Product>();
    	this.loadingProductProgressBar = ProductService.getLoadingProductProgressBar();
    	this.loadingProductText = ProductService.getLoadingProductText();
    	
    	this.loadingProductProgressBar.set(0.0);
    	this.loadingProductText.set("Update Products...");

    	this.nb_products = this.updatingProducts.size();
    	this.nb_update_products = 0;
    	this.updatedProductsLog = "";
    	this.separatorLog = " | ";
    	this.error = false;
    	
    	StageService.showView(Views.viewsSecondaryStage.LoadingProduct, false);	
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
    	if (!DataSourceFactory.checkPrivilege("UPDATE", "ecofish products", "products", "actual_price")) return false;
    	if (!DataSourceFactory.checkPrivilege("UPDATE", "ecofish products", "products", "active")) return false;
    	return true;
    }
    
    /**
     * Updating products
     */
    private void updatingProducts() {
    	try {
			Connection connection = DataSourceFactory.getDataSource().getConnection();
			Statement stmt = connection.createStatement();
			for (Product product : updatingProducts) {
				if (product.getChangeActive() == true || product.getNewPrice() != null) {
					if (product.getChangeActive() == true) {
						stmt.executeUpdate(
								"UPDATE mg_catalog_product_entity_int AS statusTable\n"
								+ "SET statusTable.value = " + product.getActive() + " \n"
								+ "WHERE statusTable.attribute_id = (SELECT attributeTable.attribute_id\n"
								+ "										FROM mg_eav_attribute AS attributeTable\n" 
								+ "										WHERE attributeTable.attribute_code = 'status')\n"
								+ "	AND statusTable.entity_id = (SELECT productTable.entity_id\n"
								+ "									FROM mg_catalog_product_entity AS productTable\n"
								+ "									WHERE productTable.sku = '" + product.getSku() + "')\n"
								);
					}
					if (product.getActive() == true && product.getNewPrice() != null) {
						stmt.executeUpdate(
								"UPDATE mg_catalog_product_entity_decimal AS priceTable\n"
								+ "SET priceTable.value = " + product.getNewPrice() + " \n"
								+ "WHERE priceTable.attribute_id = (SELECT attributeTable.attribute_id\n"
								+ "										FROM mg_eav_attribute AS attributeTable\n" 
								+ "										WHERE attributeTable.attribute_code = 'price')\n"
								+ "	AND priceTable.entity_id = (SELECT productTable.entity_id\n"
								+ "									FROM mg_catalog_product_entity AS productTable\n"
								+ "									WHERE productTable.sku = '" + product.getSku() + "')\n"
								);
					}
					
					this.updatedProductsLog += "\n" + product.getSku() + " (" + product.getCategory() + " - " + product.getFamily() + " - " + product.getName()
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
				this.loadingProductProgressBar.set((double)this.nb_update_products/this.nb_products);
			}
			stmt.close();
			connection.close();
		}
		catch (SQLException e) {
			Logging.LOGGER.log(Level.SEVERE, "Error when updating products list:\n" + e.getMessage());
			error = true;
		}
    	finally {
    		if (!this.updatedProducts.isEmpty()) {
    			for (Product product : this.updatedProducts) {
    				this.updatingProducts.remove(product);
    			}
    			Logging.LOGGER.log(Level.INFO, this.nb_update_products + "/" + this.nb_products + " products have been updated: " + updatedProductsLog);
    		}
    	}
    }
 
    /**
     * Update of the interface according to the result of the product updating
     */
    private void updateInterface() {
    	Platform.runLater(() -> {
			if (error) {
				if (DataSourceFactory.showAlertErrorSQL("Error when updating products list.\n" + this.nb_update_products + "/" + this.nb_products + " products have been updated")) ProductService.updateProduct();
				else {
					StageService.clearViewPrimaryStage();
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