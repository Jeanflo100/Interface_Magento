package ecofish.interface_magento.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.TreeMap;
import java.util.logging.Level;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.model.DetailedProduct.season;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import ecofish.interface_magento.view.LoadingProductController;
import javafx.application.Platform;

public class GettingDetailedProductThread implements Runnable {
	
	private final DetailedProduct detailedProduct;
	
	private Boolean error;
	
	public GettingDetailedProductThread(DetailedProduct detailedProduct) {
		this.detailedProduct = detailedProduct;
		
		StageService.showView(Views.viewsSecondaryStage.LoadingProduct, false);
    	LoadingProductController.updateLoadingProductProgressBar(0.0);
    	LoadingProductController.updateLoadingProductText("Retrieving product details...");
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
			ResultSet detailProduct;
			ArrayList<String> detailProductList = new ArrayList<String>();
			
			detailProduct = statement.executeQuery("SELECT eanCodeTable.value AS eanCode\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS eanCodeTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND eanCodeTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'ean_code')\n"
				);
			if (detailProduct.next()) this.detailedProduct.setEanCode(detailProduct.getString("eanCode"));
			
			detailProduct = statement.executeQuery("SELECT ecSalesCodeTable.value AS ecSalesCode\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS ecSalesCodeTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND ecSalesCodeTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'ec_sales_code')\n"
				);
			if (detailProduct.next()) this.detailedProduct.setEcSalesCode(detailProduct.getString("ecSalesCode"));
			
			detailProduct = statement.executeQuery("SELECT alergenTable.value AS alergen\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_text AS alergenTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND alergenTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'product_alergen')\n"
				);

			detailProductList.clear();
			while (detailProduct.next()) {
				/*String[] alergens = detailProduct.getString("alergen").split("-");
				System.out.println(alergens);
				for (String alergen : alergens) {
					if (!detailProductList.contains(alergen)) detailProductList.add(alergen);
				}*/
				detailProductList.add(detailProduct.getString("alergen"));
			}
			this.detailedProduct.setAlergens(detailProductList);
			
			detailProduct = statement.executeQuery("SELECT brandTable.value AS brand\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_int AS matchProductBrandTable USING (entity_id)\n"
					+ "LEFT JOIN mg_eav_attribute_option_value AS brandTable ON matchProductBrandTable.value = brandTable.option_id\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND matchProductBrandTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "												FROM mg_eav_attribute AS attributeTable\n" 
					+ "												WHERE attributeTable.attribute_code = 'product_brand')\n"
					+ "AND brandTable.store_id = false"
				);

			detailProductList.clear();
			while (detailProduct.next()) {
				detailProductList.add(detailProduct.getString("brand"));
			}
			this.detailedProduct.setBrands(detailProductList);
			
			detailProduct = statement.executeQuery("SELECT labelTable.value AS label\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_int AS matchProductLabelTable USING (entity_id)\n"
					+ "LEFT JOIN mg_eav_attribute_option_value AS labelTable ON matchProductLabelTable.value = labelTable.option_id\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND matchProductLabelTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "												FROM mg_eav_attribute AS attributeTable\n" 
					+ "												WHERE attributeTable.attribute_code = 'product_label')\n"
					+ "AND labelTable.store_id = false"
				);

			detailProductList.clear();
			while (detailProduct.next()) {
				detailProductList.add(detailProduct.getString("label"));
			}
			this.detailedProduct.setLabels(detailProductList);
			
			detailProduct = statement.executeQuery("SELECT shortDescriptionTable.value AS shortDescription\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_text AS shortDescriptionTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND shortDescriptionTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'short_description')\n"
				);
			if (detailProduct.next()) this.detailedProduct.setShortDescription(detailProduct.getString("shortDescription"));
			
			// Obtenir le début du lien afin d'avoir une url conforme
			/*detailProduct = statement.executeQuery("SELECT urlImageTable.value AS urlImage\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS urlImageTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND urlImageTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'image' AND attributeTable.frontend_input = 'media_image')\n"
				);
			if (detailProduct.next()) {
				this.detailedProduct.setUrlImage(detailProduct.getString("urlImage"));
			}*/
			
			detailProduct = statement.executeQuery("SELECT descriptionTable.value AS description\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_text AS descriptionTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND descriptionTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'description' AND attributeTable.is_required = true)\n"
				);
			if (detailProduct.next()) this.detailedProduct.setDescription(detailProduct.getString("description"));
			
			detailProduct = statement.executeQuery("SELECT latinNameTable.value AS latinName\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS latinNameTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND latinNameTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'latin_name')\n"
				);
			if (detailProduct.next()) this.detailedProduct.setLatinName(detailProduct.getString("latinName"));
			
			detailProduct = statement.executeQuery("SELECT latinNameTable.value AS latinName\n"
					+ "FROM mg_catalog_product_entity AS productTable\n"
					+ "LEFT JOIN mg_catalog_product_entity_varchar AS latinNameTable USING (entity_id)\n"
					+ "WHERE productTable.sku = '" + this.detailedProduct.getSku() + "'\n"
					+ "AND latinNameTable.attribute_id = (SELECT attributeTable.attribute_id\n"
					+ "											FROM mg_eav_attribute AS attributeTable\n"
					+ "											WHERE attributeTable.attribute_code = 'latin_name')\n"
				);
			if (detailProduct.next()) this.detailedProduct.setLatinName(detailProduct.getString("latinName"));
			
			/*ResultSet production_type = statement.executeQuery("SELECT value FROM mg_catalog_product_entity_varchar WHERE attribute_id = 145");
			while (production_type.next()) {
				System.out.println(production_type.getString("value"));
			}*/
			
			/*ResultSet high_season = statement.executeQuery("SELECT value FROM mg_catalog_product_entity_text WHERE attribute_id = 157");
			while (high_season.next()) {
				System.out.println(high_season.getString("value"));
			}*/
			
			/*ResultSet med_season = statement.executeQuery("SELECT value FROM mg_catalog_product_entity_text WHERE attribute_id = 158");
			while (med_season.next()) {
				System.out.println(med_season.getString("value"));
			}*/
			
			/*ResultSet low_season = statement.executeQuery("SELECT value FROM mg_catalog_product_entity_text WHERE attribute_id = 159");
			while (low_season.next()) {
				System.out.println(low_season.getString("value"));
			}*/
			
			/*ResultSet country_of_manufacture = statement.executeQuery("SELECT value FROM mg_catalog_product_entity_int WHERE attribute_id = 108");
			while (country_of_manufacture.next()) {
				System.out.println(country_of_manufacture.getString("value"));
			}*/
			
			/*ResultSet country_of_manufacture = statement.executeQuery("SELECT value FROM mg_catalog_product_entity_int WHERE attribute_id = 108");
			while (country_of_manufacture.next()) {
				System.out.println(country_of_manufacture.getString("value"));
			}*/
			
			/*ResultSet priceHistory = statement.executeQuery("SELECT * FROM mg_catalogrule_oldprice");
			while (priceHistory.next()) {
				System.out.println(priceHistory.getString(1) + " " + priceHistory.getString(2) + " " + priceHistory.getString(3));
			}*/
			
			/*ResultSet resultSet = statement.executeQuery("SELECT * FROM product");
			while(resultSet.next()) {
			}*/
			
    	}
		catch (SQLException e){
			Logging.getLogger().log(Level.SEVERE, "Error when getting details product:\n" + e.getMessage());
			error = true;
		}
    	season[] seasons = new season[12];
		seasons[0] = seasons[1] = seasons[2] = seasons[10] = seasons[11] = season.low;
		seasons[3] = seasons[4] = seasons[9] = season.medium;
		seasons[5] = seasons[6] = seasons[7] = seasons[8] = season.high;
    	this.detailedProduct.setSeasons(seasons);
    	this.detailedProduct.setCountriesOfManufacture(Arrays.asList("country_of_manufacture_4", "country_of_manufacture_5", "country_of_manufacture_7"));
    	this.detailedProduct.setBasicPack("Cageot - 10kg");
    	this.detailedProduct.setSecondPack(Collections.singletonMap("Cageot - 5kg", 0.1));
    	this.detailedProduct.setThirdPack(Collections.singletonMap("1kg", 0.35));
    	this.detailedProduct.setFourthPack(Collections.singletonMap("Pièce", 0.5));
    	TreeMap<Calendar, Double> priceHistory = new TreeMap<Calendar, Double>();
    	Calendar date4 = Calendar.getInstance();
		date4.set(2019, 3, 12);
		priceHistory.put(date4, 2.37);
		Calendar date1 = Calendar.getInstance();
		date1.set(2018, 9, 20);
		priceHistory.put(date1, 2.34);
		Calendar date2 = Calendar.getInstance();
		date2.set(2018, 11, 4);
		priceHistory.put(date2, 2.48);
		Calendar date3 = Calendar.getInstance();
		date3.set(2019, 0, 27);
		priceHistory.put(date3, 2.51);
		Calendar date5 = Calendar.getInstance();
		date5.set(2019, 6, 7);
		priceHistory.put(date5, 2.41);
    	this.detailedProduct.setPriceHistory(priceHistory);
    }
    
    private void updateInterface() {
    	Platform.runLater(() -> {
    		StageService.clearViewPrimaryStage(Views.viewsPrimaryStage.DetailsProductOverview);
    		StageService.closeSecondaryStage();
        	StageService.showView(Views.viewsPrimaryStage.DetailsProductOverview);
    	});
    }
    
    private void problemPrivileges() {
    	Platform.runLater(() -> {
    		if (DataSourceFactory.showAlertProblemPrivileges()) {
    			GettingDetailedProductThread gettingDetailedProductThread = new GettingDetailedProductThread(this.detailedProduct);
    			new Thread(gettingDetailedProductThread).start();
    		}
    	});
    }
    
}