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
    private Boolean error;
 
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

    	this.error = false;
    	
		StageService.showSecondaryStage(true);
    }
 
    /**
     * Loads products then display a window of success or failure
     */
    public void run() {

    	try {
    		
    		Connection connection = DataSourceFactory.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			
			/*ResultSet retour = statement.executeQuery("SELECT COUNT(*) AS retour FROM mg_admin_user");
			while(retour.next()) {
				System.out.println(retour.getString("retour"));
			}*/
			
			ResultSet nb_elements = statement.executeQuery("SELECT COUNT(productTable.sku) AS nb_products FROM mg_catalog_product_entity AS productTable");
			nb_elements.next();
			Integer nb_products = nb_elements.getInt("nb_products");
			Integer nb_loading_products = 0;
			
			TreeSet<String> familySet;

			ResultSet resultSet = statement.executeQuery(
					"SELECT DISTINCT productTable.sku AS sku, nameTable.value AS name, priceTable.value AS price, statusTable.value AS status, categoryTable.value AS category, familyTable.value AS family\n"	
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS nameTable USING (entity_id)\n"
					+ "LEFT JOIN mg_catalog_product_entity_decimal AS priceTable USING (entity_id)\n"
					+ "LEFT JOIN mg_catalog_product_entity_int AS statusTable USING (entity_id)\n"
					+ "LEFT JOIN mg_catalog_category_product AS matchProductCategoryTable ON productTable.entity_id = matchProductCategoryTable.product_id\n"
					+ "LEFT JOIN mg_catalog_category_entity_varchar AS categoryTable ON matchProductCategoryTable.category_id = categoryTable.entity_id\n"
					+ "LEFT JOIN mg_catalog_product_entity_int AS matchProductFamilyTable ON productTable.entity_id = matchProductFamilyTable.entity_id\n"
					+ "LEFT JOIN mg_eav_attribute_option_value AS familyTable ON (matchProductFamilyTable.value = familyTable.option_id\n"
					+ "																AND matchProductFamilyTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "																											FROM mg_eav_attribute AS attributeTable\n"
					+ "																											WHERE attributeTable.attribute_code = 'product_family'))\n"
					+ "WHERE nameTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "										FROM mg_eav_attribute AS attributeTable\n"
					+ "										LEFT JOIN mg_eav_entity_type AS attributeTypeTable USING (entity_type_id)\n"
					+ "										WHERE (attributeTable.attribute_code = 'name' AND attributeTypeTable.entity_type_code = 'catalog_product'))\n"
					+ "	AND priceTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "										FROM mg_eav_attribute AS attributeTable\n"
					+ "										WHERE attributeTable.attribute_code = 'price')\n"
					+ "	AND statusTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "										FROM mg_eav_attribute AS attributeTable\n"
					+ "										WHERE attributeTable.attribute_code = 'status')\n"
					+ "	AND categoryTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "										FROM mg_eav_attribute AS attributeTable\n"
					+ "										LEFT JOIN mg_eav_entity_type AS attributeTypeTable USING (entity_type_id)\n"
					+ "										WHERE (attributeTable.attribute_code = 'name' AND attributeTypeTable.entity_type_code = 'catalog_category'))\n"
					+ "	AND ((matchProductFamilyTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "													FROM mg_eav_attribute AS attributeTable\n"
					+ "													WHERE attributeTable.attribute_code = 'product_family')\n"
					+ "			AND familyTable.store_id = false)\n"
					+ "		OR (SELECT attributeTable.attribute_id\n"
					+ " 		FROM mg_eav_attribute AS attributeTable\n"
					+ "			WHERE attributeTable.attribute_code = 'product_family')\n"
					+ "				NOT IN (SELECT matchProductFamilyTable_tmp.attribute_id\n"
					+ "						FROM mg_catalog_product_entity_int AS matchProductFamilyTable_tmp\n"
					+ "						WHERE matchProductFamilyTable_tmp.entity_id = matchProductFamilyTable.entity_id))\n"
					);
			
			while(resultSet.next()) {
				String category = resultSet.getString("category");
				String family = resultSet.getString("family") == null ? "[No family]" : resultSet.getString("family");
				Product product = new Product(
						resultSet.getString("sku"),
						category,
						family,
						resultSet.getString("name"),
						"",	/*resultSet.getString("size"),*/
						"",	/*resultSet.getString("quality"),*/
						resultSet.getDouble("price"),
						resultSet.getBoolean("status"));
				if (product.getActive()) {
					this.activeProducts.add(product);
				}
				else {
					this.inactiveProducts.add(product);
				}

				familySet = groups.containsKey(category) ? groups.get(category) : new TreeSet<>();
				familySet.add(family);
				groups.put(category, familySet);
				
				nb_loading_products += 1;
				loadingProductProgressBar.set((double)nb_loading_products/nb_products);
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