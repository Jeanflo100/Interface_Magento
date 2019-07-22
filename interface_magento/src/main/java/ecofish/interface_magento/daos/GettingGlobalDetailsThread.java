package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.service.GlobalDetails;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.view.LoadingProductController;
import javafx.application.Platform;

public class GettingGlobalDetailsThread implements Runnable {
	
	private Boolean error;
	
	public GettingGlobalDetailsThread() {
    	LoadingProductController.updateLoadingProductProgressBar(0.0);
    	LoadingProductController.updateLoadingProductText("Retrieving global details...");
	}
	
	public void run() {
		if (privilegeChecking()) {
			retrieveDetailsProduct();
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
    	return true;
    }
    
    private void retrieveDetailsProduct() {
		try {
			
			Connection connection = DataSourceFactory.getConnection();
			Statement statement = connection.createStatement();
			
			ArrayList<String> globalDetailsList = new ArrayList<String>();
			ResultSet globalDetailsResultSet;
			
			globalDetailsList.clear();
	    	globalDetailsResultSet = statement.executeQuery("SELECT DISTINCT alergenTable.value AS alergen FROM mg_catalog_product_entity_text AS alergenTable\n"
	    													+ "WHERE alergenTable.attribute_id = (SELECT attributeTable.attribute_id\n"
	    													+ "										FROM mg_eav_attribute AS attributeTable\n"
	    													+ "										WHERE attributeTable.attribute_code = 'product_alergen')");
			while (globalDetailsResultSet.next()) {
				globalDetailsList.add(globalDetailsResultSet.getString("alergen"));
			}
			GlobalDetails.setAlergens(globalDetailsList);
			
			globalDetailsList.clear();
	    	globalDetailsResultSet = statement.executeQuery("SELECT DISTINCT brandTable.value AS brand FROM mg_eav_attribute_option_value AS brandTable\n"
	    													+ "WHERE brandTable.option_id IN (SELECT matchBrandAttributeTable.option_id\n"
	    													+ "									FROM mg_eav_attribute_option AS matchBrandAttributeTable\n"
	    													+ "									WHERE matchBrandAttributeTable.attribute_id IN (SELECT attributeTable.attribute_id"
	    													+ "																						FROM mg_eav_attribute AS attributeTable"
	    													+ "																						WHERE attributeTable.attribute_code = 'product_brand'))");
			while (globalDetailsResultSet.next()) {
				globalDetailsList.add(globalDetailsResultSet.getString("brand"));
			}
			GlobalDetails.setBrands(globalDetailsList);
			
			globalDetailsList.clear();
	    	globalDetailsResultSet = statement.executeQuery("SELECT DISTINCT labelTable.value AS label FROM mg_eav_attribute_option_value AS labelTable\n"
	    													+ "WHERE labelTable.option_id IN (SELECT matchLabelAttributeTable.option_id\n"
	    													+ "									FROM mg_eav_attribute_option AS matchLabelAttributeTable\n"
	    													+ "									WHERE matchLabelAttributeTable.attribute_id IN (SELECT attributeTable.attribute_id"
	    													+ "																						FROM mg_eav_attribute AS attributeTable"
	    													+ "																						WHERE attributeTable.attribute_code = 'product_label'))");
			while (globalDetailsResultSet.next()) {
				globalDetailsList.add(globalDetailsResultSet.getString("label"));
			}
			GlobalDetails.setLabels(globalDetailsList);
			
			globalDetailsList.clear();
	    	globalDetailsResultSet = statement.executeQuery("SELECT DISTINCT productionTypeTable.value AS productionType FROM mg_catalog_product_entity_varchar AS productionTypeTable\n"
	    													+ "WHERE productionTypeTable.attribute_id = (SELECT attributeTable.attribute_id\n"
	    													+ "										FROM mg_eav_attribute AS attributeTable\n"
	    													+ "										WHERE attributeTable.attribute_code = 'production_type')");
			while (globalDetailsResultSet.next()) {
				String productionType = globalDetailsResultSet.getString("productionType") == null ? "[No production type]" : globalDetailsResultSet.getString("productionType");
				globalDetailsList.add(productionType);
			}
			GlobalDetails.setProductionTypes(globalDetailsList);
			
			globalDetailsList.clear();
	    	globalDetailsResultSet = statement.executeQuery("SELECT DISTINCT countryOfManufactureTable.value AS countryOfManufacture FROM mg_catalog_product_entity_varchar AS countryOfManufactureTable\n"
	    													+ "WHERE countryOfManufactureTable.attribute_id = (SELECT attributeTable.attribute_id\n"
	    													+ "										FROM mg_eav_attribute AS attributeTable\n"
	    													+ "										WHERE attributeTable.attribute_code = 'country_of_manufacture')");
			while (globalDetailsResultSet.next()) {
				globalDetailsList.add(globalDetailsResultSet.getString("countryOfManufacture"));
			}
			GlobalDetails.setCountriesOfManufacture(globalDetailsList);
			
		} catch (SQLException e) {
			Logging.getLogger().log(Level.SEVERE, "Error when getting details product:\n" + e.getMessage());
			error = true;
		}
    }
    
    private void updateInterface() {
    	Platform.runLater(() -> {
    		StageService.closeSecondaryStage();
    	});
    }
    
    private void problemPrivileges() {
    	Platform.runLater(() -> {
    		if (DataSourceFactory.showAlertProblemPrivileges()) {
    			GettingGlobalDetailsThread gettingGlobalDetailsThread = new GettingGlobalDetailsThread();
    			new Thread(gettingGlobalDetailsThread).start();
    		}
    	});
    }
	
}