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
import ecofish.interface_magento.service.ViewService;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

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
    	
		StageService.showSecondaryStage(true);		
    }
 
    public void run() {

		try {
			Connection connection = DataSourceFactory.getDataSource().getConnection();
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
				this.loadingProductProgressBar.set((double)this.nb_update_products/this.nb_products);
				/*try {
					Thread.sleep(30);
					System.out.println(loadingProductProgressBar);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
			stmt.close();
			connection.close();
		}
		catch (SQLException e) {
			Logging.LOGGER.log(Level.WARNING, "Error when updating products list:\n" + e.getMessage());
			error = true;
		}
		
		if (!this.updatedProducts.isEmpty()) {
			for (Product product : this.updatedProducts) {
				this.updatingProducts.remove(product);
			}
			Logging.LOGGER.log(Level.INFO, this.nb_update_products + "/" + this.nb_products + " products have been updated: " + updatedProductsLog);
		}
			
		Platform.runLater(() -> {
			ViewService.clearViews();
			if (error == true) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setTitle("FAILURE");
				alert.setHeaderText("Error when updating products");
				alert.setContentText(this.nb_update_products + "/" + this.nb_products + " products have been updated");
				alert.showAndWait();
				StageService.showView(ViewService.getView("PriceProductOverview"));
			}
			else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.initOwner(StageService.getSecondaryStage());
				alert.setTitle("SUCCESS");
				alert.setHeaderText("Success in updating products");
				alert.setContentText(this.nb_update_products + "/" + this.nb_products + " products have been updated");
				alert.showAndWait();
				StageService.showView(ViewService.getView("StatusProductOverview"));
			}
			StageService.showSecondaryStage(false);
        });
		
    }
 
}