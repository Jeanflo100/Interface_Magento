package ecofish.interface_magento.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

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
	
	private void initDetails() {
		seasons = new season[nb_month];
		seasons[0] = seasons[1] = seasons[2] = seasons[10] = seasons[11] = season.low;
		seasons[3] = seasons[4] = seasons[9] = season.medium;
		seasons[5] = seasons[6] = seasons[7] = seasons[8] = season.high;
		production_type = "Sauvage";
		short_description = "Un concentré d’énergie et de bien-être à croquer ou à boire dans nos oranges Bio !";
		description = "Descriptif : L'orange, un des fruits préféré des français ! \r\n" + 
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
				"Pour une conservation optimale…: L’orange se conserve très facilement et assez longtemps. Vous pouvez la garder à l’air ambiant pendant une semaine. Dans le bac à légumes du réfrigérateur, vous stocker pendant 10 jours, tout en préservant son délicieux jus !\r\n";
		latin_name = "Muriculous suvitius";
		url_image = null;
		ean_code = "41225659535";
		ec_sales_code = "FR5F5FGG4HH2245D";
		alergens = new ArrayList<String>();
		alergens.add("alergen1");
		alergens.add("alergen4");
		alergens.add("alergen5");
		brands = new ArrayList<String>();
		brands.add("brand3");
		brands.add("brand7");
		brands.add("brand9");
		labels = new ArrayList<String>();
		labels.add("label4");
		labels.add("label5");
		labels.add("label7");
		countries_of_manufacture = new ArrayList<String>();
		countries_of_manufacture.add("country_of_manufacture_4");
		countries_of_manufacture.add("country_of_manufacture_5");
		countries_of_manufacture.add("country_of_manufacture_7");
		basicPack = "Cageot - 10kg";
		second_pack = new Hashtable<String, Double>(1);
		second_pack.put("Cageot - 5kg", 0.1);
		third_pack = new Hashtable<String, Double>(1);
		third_pack.put("1kg", 0.35);
		fourth_pack = new Hashtable<String, Double>(1);
		fourth_pack.put("Pièce", 0.5);
		priceHistory = new TreeMap<Calendar, Double>();
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
		/*priceHistory.forEach((key, value) -> {
			System.out.println(key.getTime() + " " + value);
		});*/
	}
	
	private DetailedProduct(Product product , String short_description) {
		super(product);
		this.short_description = short_description;
		
		initDetails();
	}
	
	////////////// Set actual details //////////////
	
	public void setEanCode (String ean_code) {
		DetailedProductHolder.INSTANCE.ean_code = ean_code;
	}
	
	public void setEcSalesCode (String ec_sales_code) {
		DetailedProductHolder.INSTANCE.ec_sales_code = ec_sales_code;
	}
	
	public void setAlergens (ArrayList<String> alergens) {
		DetailedProductHolder.INSTANCE.alergens.clear();
		if (alergens != null) DetailedProductHolder.INSTANCE.alergens.addAll(alergens);
	}
	
	public void setBrands (ArrayList<String> brands) {
		DetailedProductHolder.INSTANCE.brands.clear();
		if (brands != null) DetailedProductHolder.INSTANCE.brands.addAll(brands);
	}
	
	public void setLabels (ArrayList<String> labels) {
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
	
	public void setCountriesOfManufacture(ArrayList<String> countries_of_manufacture) {
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
	
	public void setNewAlergens (ArrayList<String> alergens) {
		if (alergens != null && !DetailedProductHolder.INSTANCE.alergens.equals(alergens)) DetailedProductHolder.INSTANCE.new_alergens = new ArrayList<String>(alergens);
		else DetailedProductHolder.INSTANCE.new_alergens = null;
	}
	
	public void setNewBrands (ArrayList<String> brands) {
		if (brands != null && !DetailedProductHolder.INSTANCE.brands.equals(brands)) DetailedProductHolder.INSTANCE.new_brands = new ArrayList<String>(brands);
		else DetailedProductHolder.INSTANCE.new_brands = null;
	}
	
	public void setNewLabels (ArrayList<String> labels) {
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
	
	public void setNewCountriesOfManufacture(ArrayList<String> countries_of_manufacture) {
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
	
	public static void change(Product product, String short_description) {
		Product product_tmp = new Product(103, "Agrumes", "Orange", "Orange New Land", "3", "II", 1.58, true);
		String short_description_tmp = "";
		DetailedProductHolder.INSTANCE = new DetailedProduct(product_tmp, short_description_tmp);
	}
	
	public static DetailedProduct getProduct() {
		return DetailedProductHolder.INSTANCE;
	}
	
	private static class DetailedProductHolder {
		private static DetailedProduct INSTANCE = new DetailedProduct(new Product(103, "Agrumes", "Orange", "Orange New Land", "3", "II", 1.58, false), "");
	}
	
}