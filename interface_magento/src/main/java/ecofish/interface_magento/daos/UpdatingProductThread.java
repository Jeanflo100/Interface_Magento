package ecofish.interface_magento.daos;

import java.sql.Connection;
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

public class UpdatingProductThread implements Runnable {

	private ObservableList<Product> products;
    private DoubleProperty loadingProductProgressBar;
    private StringProperty loadingProductText;
 
    public UpdatingProductThread() {
    	this.products = ProductService.getProducts();
    	this.loadingProductProgressBar = ProductService.getLoadingProductProgressBar();
    	this.loadingProductText = ProductService.getLoadingProductText();
    	
    	this.loadingProductProgressBar.set(0.0);
    	this.loadingProductText.set("Update Products...");

		StageService.showSecondaryStage(true);
    }
 
    public void run() {

		try {
			Connection connection = DataSourceFactory.getDataSource().getConnection();
			Statement stmt = connection.createStatement();
			Integer nb_products = products.size();
			Integer nb_update_products = 0;
			for (Product product : products) {
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
				loadingProductProgressBar.set((double)nb_update_products/nb_products);
	
				try {
					Thread.sleep(10);
					System.out.println("passage SQL");
					System.out.println(loadingProductProgressBar);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stmt.close();
			connection.close();
		} catch (SQLException e) {}

		Platform.runLater(() -> {
			ViewService.clearViews();
			StageService.showSecondaryStage(false);
			StageService.showView(ViewService.getView("StatusProductOverview"));
        });
		
    }
 
}