package ecofish.interface_magento;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import ecofish.interface_magento.daos.DataSourceFactory;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import javafx.application.Application;
import javafx.stage.Stage;

public class InterfaceMagento extends Application {

	public InterfaceMagento() {

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//initDatabase(); 	// enlever exception si delete
		StageService.initPrimaryStage(primaryStage);
		StageService.createSecondaryStage();
		ProductService.loadProduct();
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}
	
	public void initDatabase() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM product");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Citron Esota', 'Agrumes', 'Citron', '1', 'II', 2.3, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Agrumes', 'Pomelos', '2', 'II', 2.8, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Agrumes', 'Orange', '2', 'III', 1.20, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Fruits à noyau', 'Citron', '1', 'II', 2.3, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Agrumes', 'Orange', '2', 'II', 2.8, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Agrumes', 'Orange', '3/4', 'III', 1.20, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Fruits à noyau', 'Citron', '1', 'II', 2.3, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Agrumes', 'Orange', '2', 'II', 2.8, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Citron Esota', 'Agrumes', 'Orange', '2', 'III', 1.20, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Fruits à noyau', 'Citron', '1', 'II', 2.3, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Agrumes', 'Pomelos', '5/6', 'II', 2.8, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Citron Esota', 'Fruits à noyau', 'Pomelos', '2', 'III', 1.20, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Citron Esota', 'Agrumes', 'Citron', '1', 'II', 2.3, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Agrumes', 'Pomelos', '2', 'II', 2.8, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Agrumes', 'Orange', '2', 'III', 1.20, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Fruits à noyau', 'Citron', '1', 'II', 2.33, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Agrumes', 'Orange', '2', 'II', 2.86, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Agrumes', 'Orange', '3/4', 'II', 1.21, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Fruits à noyau', 'Citron', '2', 'II', 2.26, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Agrumes', 'Orange', '4', 'II', 2.8, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Citron Esota', 'Agrumes', 'Orange', '3', 'IV', 1.20, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange Navel Lane Late', 'Fruits à noyau', 'Citron', '3', 'I', 2.26, true)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Orange New Hall', 'Agrumes', 'Pomelos', '4/5', 'II', 2.03, false)");
		stmt.executeUpdate("INSERT INTO product(name, category, family, size, quality, actual_price, active)" + "VALUES ('Citron Esota', 'Fruits à noyau', 'Pomelos', '3', 'III', 1.23, true)");
		stmt.close();
		connection.close();
	}
	
}
