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
import ecofish.interface_magento.service.ViewService;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;

public class LoadingProductThread implements Runnable {

	private ArrayList<Product> activeProducts;
	private ArrayList<Product> inactiveProducts;
	private TreeMap<String, TreeSet<String>> groups;
    private DoubleProperty loadingProductProgressBar;
    private StringProperty loadingProductText;   
    private Boolean error;
 
    public LoadingProductThread() {
    	this.activeProducts = ProductService.getActiveProducts();
    	this.inactiveProducts = ProductService.getInactiveProducts();
    	this.groups = FilterService.getGroups();
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
    		System.out.println("connexion successful");
			Statement statement = connection.createStatement();
			
			ResultSet retour = statement.executeQuery("SELECT COUNT(DISTINCT mg_catalog_product_entity_int.entity_id) AS nb_products FROM mg_catalog_product_entity_int");
			//ResultSet retour = statement.executeQuery("SELECT COUNT(mg_catalog_product_entity.entity_id) AS nb_products FROM mg_catalog_product_entity");
			//ResultSet retour = statement.executeQuery("SELECT COUNT(mg_catalog_product_entity.sku) AS nb_products FROM mg_catalog_product_entity");
			retour.next();
			Integer nb_products = retour.getInt("nb_products");
			System.out.println(nb_products);
			Integer nb_loading_products = 0;
			
			// Sku
			/*ResultSet result = statement.executeQuery("SELECT sku AS result FROM mg_catalog_product_entity");
			while(result.next()) {
				System.out.println(result.getString("result"));
				System.out.println("--------");
			}*/
			
			// Name
			/*ResultSet result = statement.executeQuery("SELECT value AS result FROM mg_catalog_product_entity_varchar WHERE attribute_id = 63");
			while(result.next()) {
				System.out.println(result.getString("result"));
				System.out.println("--------");
			}*/
			
			// Category
			/*ResultSet result = statement.executeQuery("SELECT value AS result FROM mg_catalog_category_entity_varchar where attribute_id = 33");
			while(result.next()) {
				System.out.println(result.getString("result"));
				System.out.println("--------");
			}*/
			
			// Price
			/*ResultSet result = statement.executeQuery("SELECT value AS result FROM mg_catalog_product_entity_decimal WHERE attribute_id = 67");
			while(result.next()) {
				System.out.println(result.getString("result"));
				System.out.println("--------");
			}*/
			
			// Status
			/*ResultSet result = statement.executeQuery("SELECT value AS result FROM mg_catalog_product_entity_int WHERE attribute_id = 87");
			while(result.next()) {
				System.out.println(result.getString("result"));
				System.out.println("--------");
			}*/
			
			// Family
			/*ResultSet result = statement.executeQuery("SELECT DISTINCT value AS result FROM mg_eav_attribute_option_value WHERE option_id IN (SELECT value FROM mg_catalog_product_entity_int WHERE attribute_id = 149)");
			while(result.next()) {
				System.out.println(result.getString("result"));
				System.out.println("--------");
			}*/
			
			// 
			/*ResultSet result = statement.executeQuery("SELECT value AS result FROM mg_eav_attribute_option_value");
			while(result.next()) {
				System.out.println(result.getString("result"));
				System.out.println("--------");
			}*/
			
			/*ResultSet result = statement.executeQuery("SELECT DISTINCT mg_catalog_product_entity.sku AS idProduct, mg_catalog_product_entity_varchar.value AS name, mg_catalog_product_entity_decimal.value AS price, mg_catalog_product_entity_int.value AS status, mg_catalog_category_entity_varchar.value AS category, mg_eav_attribute_option_value.value AS family \r\n" + 
					"FROM mg_catalog_product_entity\r\n" + 
					"LEFT JOIN mg_catalog_product_entity_varchar USING (entity_id)\r\n" + 
					"LEFT JOIN mg_catalog_product_entity_decimal USING (entity_id) \r\n" + 
					"LEFT JOIN mg_catalog_product_entity_int USING (entity_id) \r\n" + 
					"LEFT JOIN mg_catalog_category_product \r\n" +
					"	ON mg_catalog_product_entity.entity_id = mg_catalog_category_product.product_id \r\n" + 
					"LEFT JOIN mg_catalog_category_entity_varchar \r\n" +
					"	ON mg_catalog_category_product.category_id = mg_catalog_category_entity_varchar.entity_id \r\n" + 
					"LEFT JOIN mg_catalog_product_entity_int AS second_table \r\n" +
					"	ON mg_catalog_product_entity.entity_id = second_table.entity_id \r\n" + 
					"LEFT JOIN mg_eav_attribute_option_value \r\n" +
					"	ON second_table.value = mg_eav_attribute_option_value.option_id \r\n" +
					"		AND second_table.attribute_id = 149 " + 
					"WHERE mg_catalog_product_entity_varchar.attribute_id = 63 " +
					"	AND mg_catalog_product_entity_decimal.attribute_id = 67 " +
					"	AND mg_catalog_product_entity_int.attribute_id = 87 " +
					"	AND mg_catalog_category_entity_varchar.attribute_id = 33 " +
					"	AND (( second_table.attribute_id = 149 AND mg_eav_attribute_option_value.store_id = 0 ) " +
					"		OR 149 NOT IN (select attribute_id FROM mg_catalog_product_entity_int WHERE entity_id=second_table.entity_id))"
			);
			while(result.next()) {
				System.out.println(result.getString("idProduct") + " " + result.getString("name") + " " + result.getString("price") + " " + result.getString("status") + " " + result.getString("category") + " " + result.getString("family"));
				System.out.println("--------");
			}
			
			ResultSet retour2 = statement.executeQuery("SELECT COUNT(DISTINCT mg_catalog_product_entity.sku) AS nb_products \r\n" +
					"FROM mg_catalog_product_entity\r\n" + 
					"LEFT JOIN mg_catalog_product_entity_varchar USING (entity_id)\r\n" + 
					"LEFT JOIN mg_catalog_product_entity_decimal USING (entity_id) \r\n" + 
					"LEFT JOIN mg_catalog_product_entity_int USING (entity_id) \r\n" + 
					"LEFT JOIN mg_catalog_category_product \r\n" +
					"	ON mg_catalog_product_entity.entity_id = mg_catalog_category_product.product_id \r\n" + 
					"LEFT JOIN mg_catalog_category_entity_varchar \r\n" +
					"	ON mg_catalog_category_product.category_id = mg_catalog_category_entity_varchar.entity_id \r\n" + 
					"LEFT JOIN mg_catalog_product_entity_int AS second_table \r\n" +
					"	ON mg_catalog_product_entity.entity_id = second_table.entity_id \r\n" + 
					"LEFT JOIN mg_eav_attribute_option_value \r\n" +
					"	ON second_table.value = mg_eav_attribute_option_value.option_id \r\n" +
					"		AND second_table.attribute_id = 149 " + 
					"WHERE mg_catalog_product_entity_varchar.attribute_id = 63 " +
					"	AND mg_catalog_product_entity_decimal.attribute_id = 67 " +
					"	AND mg_catalog_product_entity_int.attribute_id = 87 " +
					"	AND mg_catalog_category_entity_varchar.attribute_id = 33 " +
					"	AND (( second_table.attribute_id = 149 AND mg_eav_attribute_option_value.store_id = 0 ) " +
					"		OR 149 NOT IN (select attribute_id FROM mg_catalog_product_entity_int WHERE entity_id=second_table.entity_id))"
			);
			retour2.next();
			Integer nb_products2 = retour2.getInt("nb_products");
			System.out.println(nb_products2);*/
			
			/*ResultSet result = statement.executeQuery("SELECT DISTINCT mg_catalog_product_entity.sku AS idProduct, mg_eav_attribute_option_value.value AS family " + 
					"FROM mg_catalog_product_entity " + 
					"LEFT JOIN mg_catalog_product_entity_int AS second_table USING (entity_id) " +
					"LEFT JOIN mg_eav_attribute_option_value " +
					"	ON second_table.value = mg_eav_attribute_option_value.option_id " +
					"		AND second_table.attribute_id = 149 " + 
					"WHERE ( second_table.attribute_id = 149 AND mg_eav_attribute_option_value.store_id = 0 ) " +
					"	OR 149 NOT IN (select attribute_id FROM mg_catalog_product_entity_int WHERE entity_id=second_table.entity_id)"
			);
			while(result.next()) {
				System.out.println(result.getString("idProduct") + " " + result.getString("family"));
				System.out.println("--------");
			}
			
			ResultSet retour2 = statement.executeQuery("SELECT entity_id AS nb_products \r\n" +
					"FROM mg_catalog_product_entity " +
					//"LEFT JOIN mg_catalog_product_entity_int AS second_table" +
					//"	ON mg_catalog_product_entity.entity_id = second_table.entity_id " + 
					//"LEFT JOIN mg_eav_attribute_option_value" +
					//"	ON second_table.value = mg_eav_attribute_option_value.option_id " +
					"WHERE sku is LG01009 "
			);
			retour2.next();
			//Integer nb_products2 = retour2.getInt("nb_products");
			System.out.println(retour2.getString("nb_products") + " " + retour2.getString("nb_products2"));*/
			
			TreeSet<String> familySet;

			ResultSet resultSet = statement.executeQuery(
					"SELECT DISTINCT mg_catalog_product_entity.sku AS sku, mg_catalog_product_entity_varchar.value AS name, mg_catalog_product_entity_decimal.value AS price, mg_catalog_product_entity_int.value AS status, mg_catalog_category_entity_varchar.value AS category, mg_eav_attribute_option_value.value AS family " + 
					"FROM mg_catalog_product_entity " + 
					"LEFT JOIN mg_catalog_product_entity_varchar USING (entity_id) " + 
					"LEFT JOIN mg_catalog_product_entity_decimal USING (entity_id) " + 
					"LEFT JOIN mg_catalog_product_entity_int USING (entity_id) " + 
					"LEFT JOIN mg_catalog_category_product " +
					"	ON mg_catalog_product_entity.entity_id = mg_catalog_category_product.product_id " + 
					"LEFT JOIN mg_catalog_category_entity_varchar " +
					"	ON mg_catalog_category_product.category_id = mg_catalog_category_entity_varchar.entity_id " + 
					"LEFT JOIN mg_catalog_product_entity_int AS second_table " +
					"	ON mg_catalog_product_entity.entity_id = second_table.entity_id " + 
					"LEFT JOIN mg_eav_attribute_option_value " +
					"	ON second_table.value = mg_eav_attribute_option_value.option_id " +
					"		AND second_table.attribute_id = 149 " + 
					"WHERE mg_catalog_product_entity_varchar.attribute_id = 63 " +
					"	AND mg_catalog_product_entity_decimal.attribute_id = 67 " +
					"	AND mg_catalog_product_entity_int.attribute_id = 87 " +
					"	AND mg_catalog_category_entity_varchar.attribute_id = 33 " +
					"	AND (( second_table.attribute_id = 149 AND mg_eav_attribute_option_value.store_id = 0 ) " +
					"		OR 149 NOT IN (select attribute_id FROM mg_catalog_product_entity_int WHERE entity_id=second_table.entity_id)) "
			);
			
			while(resultSet.next()) {
				String family_tmp = resultSet.getString("family") == null ? "[No family]" : resultSet.getString("family");
				Product product = new Product(
						resultSet.getString("sku"),
						resultSet.getString("name"),
						resultSet.getString("category"),
						family_tmp,
						null,	/*resultSet.getString("size"),*/
						null,	/*resultSet.getString("quality"),*/
						resultSet.getDouble("price"),
						resultSet.getBoolean("status"));
				if (product.getActive()) {
					this.activeProducts.add(product);
				}
				else {
					this.inactiveProducts.add(product);
				}

				String category = resultSet.getString("category");
				String family = family_tmp;
				familySet = groups.containsKey(category) ? groups.get(category) : new TreeSet<>();
				familySet.add(family);
				groups.put(category, familySet);
				
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
			Logging.LOGGER.log(Level.WARNING, "Error when getting products list:\n" + e.getMessage());
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