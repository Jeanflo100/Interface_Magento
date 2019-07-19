package ecofish.interface_magento.daos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;

import javax.swing.event.ListSelectionEvent;

import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.model.DetailedProduct.season;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import ecofish.interface_magento.view.LoadingProductController;
import javafx.application.Platform;

public class GettingDetailedProductThread implements Runnable {
	
	private final DetailedProduct detailedProduct;
	
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
    	this.detailedProduct.setEanCode("41225659535");
    	this.detailedProduct.setEcSalesCode("FR5F5FGG4HH2245D");
    	this.detailedProduct.setAlergens(Arrays.asList("alerge1", "alergen4", "alergen5"));
    	this.detailedProduct.setBrands(Arrays.asList("brand3", "brand7", "brand9"));
    	this.detailedProduct.setLabels(Arrays.asList("label4", "label5", "label7"));
    	this.detailedProduct.setShortDescription("Un concentré d’énergie et de bien-être à croquer ou à boire dans nos oranges Bio !");
    	this.detailedProduct.setUrlImage(null);
    	this.detailedProduct.setDescription("Descriptif : L'orange, un des fruits préféré des français ! \r\n" + 
				" \r\n" + 
				"Avec la pomme et la banane, l’orange fait partie des fruits les plus consommés en France. En effet, c’est le troisième fruit le plus cultivé au monde. \r\n" + 
				"\r\n" + 
				"Présente sur notre site tout l’hiver et au printemps, elles vous apporteront les vitamines nécessaires pour lutter contre le froid et la fatigue, ainsi qu’une quantité importante de minéraux (calcium et magnésium), indispensables au bon équilibre de l’organisme. \r\n" + 
				"\r\n" + 
				"Gorgée en vitamine C, l’orange est idéale pour couvrir tous vos besoins quotidiens ! Son goût sucré légèrement acidulé excitera vos papilles...\r\n" + 
				"\r\n" + 
				"Tout comme le citron, la bergamote et le pamplemousse, l'orange appartient au groupe des agrumes. Elle compte de nombreuses variétés, toutes aussi bonnes les unes que les autres ! \r\n" + 
				"On retrouve principalement trois grandes variétés d'oranges : les oranges blondes à chair ou à jus, les oranges amères et les oranges sanguines. \r\n" + 
				"\r\n" + 
				"*Les oranges blondes à chair possèdent généralement une belle peau orange brillante, rugueuse, avec une petite excroissance plus ou moins prononcée. Les principales variétés d'oranges blondes à chair sont la Navel, la Naveline, la Navelate ou encore la Late Lane. \r\n" + 
				"On distingue les oranges blondes à jus des blondes à chair car elles sont légèrement aplaties avec une peau fine et grenue. \r\n" + 
				"*Les oranges sanguines tirent leur nom de la couleur rouge de leur chair. Cette coloration est due à la présence d'anthocyanes, pigment produit chez certaines espèces quand celles-ci sont exposées au froid. Parmi les oranges sanguines, on compte les variétés Moro, Sanguinello et Tarocco, par exemple.\r\n" + 
				"*Les oranges amères sont plus petites que les oranges douces. Leur peau orange est rugueuse, épaisse et teintée de vert. Sa chair est peu juteuse, acide et contient beaucoup de pépins. Elles sont quant à elles, issus du bigaradier contrairement à l'orange douce.\r\n" + 
				"\r\n" + 
				"Bien cuisiner…: Zeste, tranches, jus ou rondelle… Vous avez de multiples possibilités pour l'ajouter à vos plats. Vous pourrez cuisiner du canard à l'orange, assaisonner vos crustacés et poissons ou la rajouter dans une semoule. Sa seule limite est votre imagination ! \r\n" + 
				"\r\n" + 
				"Pour une conservation optimale…: L’orange se conserve très facilement et assez longtemps. Vous pouvez la garder à l’air ambiant pendant une semaine. Dans le bac à légumes du réfrigérateur, vous stocker pendant 10 jours, tout en préservant son délicieux jus !\r\n");
    	this.detailedProduct.setLatinName("Muriculous suvitius");
    	this.detailedProduct.setProductionType("Sauvage");
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