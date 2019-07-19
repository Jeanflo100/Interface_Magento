package ecofish.interface_magento.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import ecofish.interface_magento.daos.GettingDetailedProductThread;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;

public class DetailedProduct extends Product {
	
	public enum season{high, medium, low, unspecified, current;};
	
	public static final Integer nb_month = 12;

	private String ean_code;
	private String ec_sales_code;
	private ArrayList<String> alergens;
	private ArrayList<String> brands;
	private ArrayList<String> labels;
	private String short_description;
	private String url_image;
	private String description;
	private String latin_name;
	private String production_type;
	private season[] seasons;
	private ArrayList<String> countries_of_manufacture;
	private String basicPack;
	private TreeMap<Calendar, Double> priceHistory;
	private Hashtable<String, Double> second_pack;
	private Hashtable<String, Double> third_pack;
	private Hashtable<String, Double> fourth_pack;
	
	private String new_ean_code;
	private String new_ec_sales_code;
	private ArrayList<String> new_alergens;
	private ArrayList<String> new_brands;
	private ArrayList<String> new_labels;
	private String new_short_description;
	private String new_url_image;
	private String new_description;
	private String new_latin_name;
	private String new_production_type;
	private season[] new_seasons;
	private ArrayList<String> new_countries_of_manufacture;
	private String new_basicPack;
	private Hashtable<String, Double> new_second_pack;
	private Hashtable<String, Double> new_third_pack;
	private Hashtable<String, Double> new_fourth_pack;
	
	private DetailedProduct(Product product) {
		super(product);
		alergens = new ArrayList<String>();
		brands = new ArrayList<String>();
		labels = new ArrayList<String>();
		seasons = new season[nb_month];
		countries_of_manufacture = new ArrayList<String>();
		second_pack = new Hashtable<String, Double>(1);
		third_pack = new Hashtable<String, Double>(1);
		fourth_pack = new Hashtable<String, Double>(1);
		priceHistory = new TreeMap<Calendar, Double>();
	}
	
	////////////// Set actual details //////////////
	
	public void setEanCode (String ean_code) {
		DetailedProductHolder.INSTANCE.ean_code = ean_code;
	}
	
	public void setEcSalesCode (String ec_sales_code) {
		DetailedProductHolder.INSTANCE.ec_sales_code = ec_sales_code;
	}
	
	public void setAlergens (Collection<String> alergens) {
		DetailedProductHolder.INSTANCE.alergens.clear();
		if (alergens != null) DetailedProductHolder.INSTANCE.alergens.addAll(alergens);
	}
	
	public void setBrands (Collection<String> brands) {
		DetailedProductHolder.INSTANCE.brands.clear();
		if (brands != null) DetailedProductHolder.INSTANCE.brands.addAll(brands);
	}
	
	public void setLabels (Collection<String> labels) {
		DetailedProductHolder.INSTANCE.labels.clear();
		if (labels != null) DetailedProductHolder.INSTANCE.labels.addAll(labels);
	}
	
	public void setShortDescription(String short_description) {
		DetailedProductHolder.INSTANCE.short_description = short_description;
	}
	
	public void setUrlImage(String url_image) {
		DetailedProductHolder.INSTANCE.url_image = url_image;
	}
	
	public void setDescription(String description) {
		DetailedProductHolder.INSTANCE.description = description;
	}
	
	public void setLatinName(String latin_name) {
		DetailedProductHolder.INSTANCE.latin_name = latin_name;
	}
	
	public void setProductionType(String production_type) {
		DetailedProductHolder.INSTANCE.production_type = production_type;
	}
	
	public void setSeasons(season[] seasons) {
		if (seasons != null && !DetailedProductHolder.INSTANCE.seasons.equals(seasons)) {
			DetailedProductHolder.INSTANCE.seasons = new season[nb_month];
			System.arraycopy(seasons, 0, DetailedProductHolder.INSTANCE.seasons, 0, DetailedProductHolder.INSTANCE.seasons.length);
		}
		else DetailedProductHolder.INSTANCE.seasons = null;
	}
	
	public void setCountriesOfManufacture(Collection<String> countries_of_manufacture) {
		DetailedProductHolder.INSTANCE.countries_of_manufacture.clear();
		if (countries_of_manufacture != null) DetailedProductHolder.INSTANCE.countries_of_manufacture.addAll(countries_of_manufacture);
	}
	
	public void setBasicPack(String basicPack) {
		DetailedProductHolder.INSTANCE.basicPack = basicPack;
	}
	
	public void setPriceHistory(Map<Calendar, Double> priceHistory) {
		DetailedProductHolder.INSTANCE.priceHistory.clear();
		DetailedProductHolder.INSTANCE.priceHistory.putAll(priceHistory);
	}
	
	public void setSecondPack(Map<String, Double> second_pack) {
		DetailedProductHolder.INSTANCE.second_pack.clear();
		if (second_pack != null) DetailedProductHolder.INSTANCE.second_pack.putAll(second_pack);
	}
	
	public void setThirdPack(Map<String, Double> third_pack) {
		DetailedProductHolder.INSTANCE.third_pack.clear();
		if (third_pack != null) DetailedProductHolder.INSTANCE.third_pack.putAll(third_pack);
	}
	
	public void setFourthPack(Map<String, Double> fourth_pack) {
		DetailedProductHolder.INSTANCE.fourth_pack.clear();
		if (fourth_pack != null) DetailedProductHolder.INSTANCE.fourth_pack.putAll(fourth_pack);
	}
	
	////////////// Set new details //////////////
	
	public void setNewEanCode (String ean_code) {
		DetailedProductHolder.INSTANCE.new_ean_code = ean_code;
	}
	
	public void setNewEcSalesCode (String ec_sales_code) {
		DetailedProductHolder.INSTANCE.new_ec_sales_code = ec_sales_code;
	}
	
	public void setNewAlergens (Collection<String> alergens) {
		if (alergens != null && !DetailedProductHolder.INSTANCE.alergens.equals(alergens)) DetailedProductHolder.INSTANCE.new_alergens = new ArrayList<String>(alergens);
		else DetailedProductHolder.INSTANCE.new_alergens = null;
	}
	
	public void setNewBrands (Collection<String> brands) {
		if (brands != null && !DetailedProductHolder.INSTANCE.brands.equals(brands)) DetailedProductHolder.INSTANCE.new_brands = new ArrayList<String>(brands);
		else DetailedProductHolder.INSTANCE.new_brands = null;
	}
	
	public void setNewLabels (Collection<String> labels) {
		if (labels != null && !DetailedProductHolder.INSTANCE.labels.equals(labels)) DetailedProductHolder.INSTANCE.new_labels = new ArrayList<String>(labels);
		else DetailedProductHolder.INSTANCE.new_labels = null;
	}
	
	public void setNewShortDescription(String short_description) {
		DetailedProductHolder.INSTANCE.new_short_description = short_description;
	}
	
	public void setNewUrlImage(String url_image) {
		DetailedProductHolder.INSTANCE.new_url_image = url_image;
	}
	
	public void setNewDescription(String description) {
		DetailedProductHolder.INSTANCE.new_description = description;
	}
	
	public void setNewLatinName(String latin_name) {
		DetailedProductHolder.INSTANCE.new_latin_name = latin_name;
	}
	
	public void setNewProductionType(String production_type) {
		DetailedProductHolder.INSTANCE.new_production_type = production_type;
	}
	
	public void setNewSeasons(season[] seasons) {
		if (seasons != null && !DetailedProductHolder.INSTANCE.seasons.equals(seasons)) {
			DetailedProductHolder.INSTANCE.new_seasons = new season[nb_month];
			System.arraycopy(seasons, 0, DetailedProductHolder.INSTANCE.new_seasons, 0, DetailedProductHolder.INSTANCE.new_seasons.length);
		}
		else DetailedProductHolder.INSTANCE.new_seasons = null;
	}
	
	public void setNewCountriesOfManufacture(Collection<String> countries_of_manufacture) {
		if (countries_of_manufacture != null && !DetailedProductHolder.INSTANCE.countries_of_manufacture.equals(countries_of_manufacture)) DetailedProductHolder.INSTANCE.new_countries_of_manufacture = new ArrayList<String>(countries_of_manufacture);
		else DetailedProductHolder.INSTANCE.new_countries_of_manufacture = null;
	}
	
	public void setNewBasicPack(String basicPack) {
		DetailedProductHolder.INSTANCE.new_basicPack = basicPack;
	}
	
	public void setNewSecondPack(Map<String, Double> second_pack) {
		if (second_pack != null && !DetailedProductHolder.INSTANCE.second_pack.equals(second_pack)) DetailedProductHolder.INSTANCE.new_second_pack = new Hashtable<String, Double>(second_pack);
		else DetailedProductHolder.INSTANCE.new_second_pack = null;
	}
	
	public void setNewThirdPack(Map<String, Double> third_pack) {
		if (third_pack != null && !DetailedProductHolder.INSTANCE.third_pack.equals(third_pack)) DetailedProductHolder.INSTANCE.new_third_pack = new Hashtable<String, Double>(third_pack);
		else DetailedProductHolder.INSTANCE.new_third_pack = null;
	}
	
	public void setNewFourthPack(Map<String, Double> fourth_pack) {
		if (fourth_pack != null && !DetailedProductHolder.INSTANCE.fourth_pack.equals(fourth_pack)) DetailedProductHolder.INSTANCE.new_fourth_pack = new Hashtable<String, Double>(fourth_pack);
		else DetailedProductHolder.INSTANCE.new_fourth_pack = null;
	}
	
	////////////// Get actual details //////////////
	
	public String getEanCode() {
		return DetailedProductHolder.INSTANCE.ean_code;
	}
	
	public String getEcSalesCode() {
		return DetailedProductHolder.INSTANCE.ec_sales_code;
	}
	
	public ArrayList<String> getAlergens() {
		return DetailedProductHolder.INSTANCE.alergens;
	}
	
	public ArrayList<String> getBrands() {
		return DetailedProductHolder.INSTANCE.brands;
	}
	
	public ArrayList<String> getLabels() {
		return DetailedProductHolder.INSTANCE.labels;
	}
	
	public String getShortDescription() {
		return DetailedProductHolder.INSTANCE.short_description;
	}
	
	public String getUrlImage() {
		return DetailedProductHolder.INSTANCE.url_image;
	}
	
	public String getDescription() {
		return DetailedProductHolder.INSTANCE.description;
	}
	
	public String getLatinName() {
		return DetailedProductHolder.INSTANCE.latin_name;
	}
	
	public String getProductionType() {
		return DetailedProductHolder.INSTANCE.production_type;
	}
	
	public season getSeason(Integer month) {
		return DetailedProductHolder.INSTANCE.seasons[month];
	}
	
	public ArrayList<String> getCountriesOfManufacture() {
		return DetailedProductHolder.INSTANCE.countries_of_manufacture;
	}
	
	public String getBasicPack() {
		return DetailedProductHolder.INSTANCE.basicPack;
	}
	
	public TreeMap<Calendar, Double> getPricehistory() {
		return DetailedProductHolder.INSTANCE.priceHistory;
	}
	
	public String getNameSecondPack() {
		return DetailedProductHolder.INSTANCE.second_pack.keys().nextElement();
	}
	
	public Double getPriceSecondPack() {
		return DetailedProductHolder.INSTANCE.second_pack.get(DetailedProductHolder.INSTANCE.second_pack.keys().nextElement());
	}
	
	public String getNameThirdPack() {
		return DetailedProductHolder.INSTANCE.third_pack.keys().nextElement();
	}
	
	public Double getPriceThirdPack() {
		return DetailedProductHolder.INSTANCE.third_pack.get(DetailedProductHolder.INSTANCE.third_pack.keys().nextElement());
	}
	
	public String getNameFourthPack() {
		return DetailedProductHolder.INSTANCE.fourth_pack.keys().nextElement();
	}
	
	public Double getPriceFourthPack() {
		return DetailedProductHolder.INSTANCE.fourth_pack.get(DetailedProductHolder.INSTANCE.fourth_pack.keys().nextElement());
	}
	
	////////////// Get new details //////////////
	
	public String getNewEanCode() {
		return DetailedProductHolder.INSTANCE.new_ean_code;
	}
	
	public String getNewEcSalesCode() {
		return DetailedProductHolder.INSTANCE.new_ec_sales_code;
	}
	
	public ArrayList<String> getNewAlergens() {
		return DetailedProductHolder.INSTANCE.new_alergens;
	}
	
	public ArrayList<String> getNewBrands() {
		return DetailedProductHolder.INSTANCE.new_brands;
	}
	
	public ArrayList<String> getNewLabels() {
		return DetailedProductHolder.INSTANCE.new_labels;
	}
	
	public String getNewShortDescription() {
		return DetailedProductHolder.INSTANCE.new_short_description;
	}
	
	public String getNewUrlImage() {
		return DetailedProductHolder.INSTANCE.new_url_image;
	}
	
	public String getNewDescription() {
		return DetailedProductHolder.INSTANCE.new_description;
	}
	
	public String getNewLatinName() {
		return DetailedProductHolder.INSTANCE.new_latin_name;
	}
	
	public String getNewProductionType() {
		return DetailedProductHolder.INSTANCE.new_production_type;
	}
	
	public season getNewSeason(Integer month) {
		return DetailedProductHolder.INSTANCE.new_seasons[month];
	}
	
	public ArrayList<String> getNewCountriesOfManufacture() {
		return DetailedProductHolder.INSTANCE.new_countries_of_manufacture;
	}
	
	public String getNewBasicPack() {
		return DetailedProductHolder.INSTANCE.new_basicPack;
	}
	
	public String getNewNameSecondPack() {
		return DetailedProductHolder.INSTANCE.new_second_pack.keys().nextElement();
	}
	
	public Double getNewPriceSecondPack() {
		return DetailedProductHolder.INSTANCE.new_second_pack.get(DetailedProductHolder.INSTANCE.new_second_pack.keys().nextElement());
	}
	
	public String getNewNameThirdPack() {
		return DetailedProductHolder.INSTANCE.new_third_pack.keys().nextElement();
	}
	
	public Double getNewPriceThirdPack() {
		return DetailedProductHolder.INSTANCE.new_third_pack.get(DetailedProductHolder.INSTANCE.new_third_pack.keys().nextElement());
	}
	
	public String getNewNameFourthPack() {
		return DetailedProductHolder.INSTANCE.new_fourth_pack.keys().nextElement();
	}
	
	public Double getNewPriceFourthPack() {
		return DetailedProductHolder.INSTANCE.new_fourth_pack.get(DetailedProductHolder.INSTANCE.new_fourth_pack.keys().nextElement());
	}
	
	public static void newDetailedProduct(Product product) {
		if (DetailedProductHolder.INSTANCE == null || !DetailedProductHolder.INSTANCE.getSku().equals(product.getSku())) {
			DetailedProductHolder.INSTANCE = new DetailedProduct(product);
			GettingDetailedProductThread gettingDetailedProductThread = new GettingDetailedProductThread(DetailedProductHolder.INSTANCE);
			new Thread(gettingDetailedProductThread).start();
		}
		else StageService.showView(Views.viewsPrimaryStage.DetailsProductOverview);
	}
	
	public static DetailedProduct getInstance() {
		return DetailedProductHolder.INSTANCE;
	}
	
	private static class DetailedProductHolder {
		private static DetailedProduct INSTANCE;// = new DetailedProduct(new Product(103, "Agrumes", "Orange", "Orange New Land", "3", "II", 1.58, false), "");
	}
	
}