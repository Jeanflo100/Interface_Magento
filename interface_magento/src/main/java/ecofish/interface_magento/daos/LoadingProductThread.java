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
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

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
    	
    	StageService.showView(Views.viewsSecondaryStage.LoadingProduct, false);
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
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_catalog_product_entity", "sku")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_catalog_product_entity_varchar", "value")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_catalog_product_entity_decimal", "value")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_catalog_product_entity_int", "value")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_catalog_category_entity_varchar", "value")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_eav_attribute_option_value", "value")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_eav_attribute", "attribute_id")) return false;
    	if (!DataSourceFactory.checkPrivilege("SELECT", "mg_catalog_product_entity_int", "attribute_id")) return false;
    	return true;
    }
    
    /**
     * Getting produts
     */
    private void loadingProducts() {
    	try {
    		Connection connection = DataSourceFactory.getDataSource().getConnection();
			Statement statement = connection.createStatement();
			
			ResultSet nb_elements = statement.executeQuery("SELECT COUNT(productTable.sku) AS nb_products FROM mg_catalog_product_entity AS productTable");
			nb_elements.next();
			Integer nb_products = nb_elements.getInt("nb_products");
			Integer nb_loading_products = 0;
			
			TreeSet<String> familySet;

			ResultSet resultSet = statement.executeQuery(
					"SELECT DISTINCT productTable.sku AS sku, nameTable.value AS name, sizeTable.value AS size, qualityTable.value AS quality, priceTable.value AS price, statusTable.value AS status, categoryTable.value AS category, familyTable.value AS family\n"	
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS nameTable USING (entity_id)\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS sizeTable using (entity_id)\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS qualityTable using (entity_id)\n"
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
					+ "	AND sizeTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "										FROM mg_eav_attribute AS attributeTable\n"
					+ "										WHERE attributeTable.attribute_code = 'product_size')\n"
					+ "	AND qualityTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "										FROM mg_eav_attribute AS attributeTable\n"
					+ "										WHERE attributeTable.attribute_code = 'product_quality')\n"
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
				String family = resultSet.getString("family") == null ? FilterService.getDefaultFamily() : resultSet.getString("family");
				Product product = new Product(
						resultSet.getString("sku"),
						category,
						family,
						resultSet.getString("name"),
						resultSet.getString("size"),
						resultSet.getString("quality"),
						resultSet.getDouble("price"),
						resultSet.getBoolean("status"));
				if (product.getActive()) this.activeProducts.add(product);
				else this.inactiveProducts.add(product);

				familySet = groups.containsKey(category) ? groups.get(category) : new TreeSet<>();
				familySet.add(family);
				groups.put(category, familySet);
				
				nb_loading_products += 1;
				loadingProductProgressBar.set((double)nb_loading_products/nb_products);
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