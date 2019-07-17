package ecofish.interface_magento.model;

import java.util.ArrayList;
import java.util.Hashtable;

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
	private Hashtable<String, Double> secondPack;
	private Hashtable<String, Double> thirdPack;
	private Hashtable<String, Double> fourthPack;
	// tableau historique de prix
	
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
		secondPack = new Hashtable<String, Double>(1);
		secondPack.put("Cageot - 5kg", 0.1);
		thirdPack = new Hashtable<String, Double>(1);
		thirdPack.put("1kg", 0.35);
		fourthPack = new Hashtable<String, Double>(1);
		fourthPack.put("Pièce", 0.5);
	}
	
	private DetailedProduct(Product product , String short_description) {
		super(product);
		this.short_description = short_description;
		
		initDetails();
	}
	
	public void setEanCode (String ean_code) {
		DetailedProductHolder.INSTANCE.ean_code = ean_code;
	}
	
	public void setEcSalesCode (String ec_sales_code) {
		DetailedProductHolder.INSTANCE.ec_sales_code = ec_sales_code;
	}
	
	public void setAlergens (ArrayList<String> alergens) {
		DetailedProductHolder.INSTANCE.alergens = alergens;
	}
	
	public void setBrands (ArrayList<String> brands) {
		DetailedProductHolder.INSTANCE.brands = brands;
	}
	
	public void setLabels (ArrayList<String> labels) {
		DetailedProductHolder.INSTANCE.labels = labels;
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
		System.arraycopy(seasons, 0, DetailedProductHolder.INSTANCE.seasons, 0, DetailedProductHolder.INSTANCE.seasons.length);
	}
	
	public void setCountriesOfManufacture(ArrayList<String> countries_of_manufacture) {
		DetailedProductHolder.INSTANCE.countries_of_manufacture = countries_of_manufacture;
	}
	
	public void setBasicPack(String basicPack) {
		DetailedProductHolder.INSTANCE.basicPack = basicPack;
	}
	
	public void setSecondPack(String nameSecondPack, Double priceSecondPack) {
		DetailedProductHolder.INSTANCE.secondPack.clear();
		DetailedProductHolder.INSTANCE.secondPack.put(nameSecondPack, priceSecondPack);
	}
	
	public void setThirdPack(String nameThirdPack, Double priceThirdPack) {
		DetailedProductHolder.INSTANCE.thirdPack.clear();
		DetailedProductHolder.INSTANCE.thirdPack.put(nameThirdPack, priceThirdPack);
	}
	
	public void setFourthPack(String nameFourthPack, Double priceFourthPack) {
		DetailedProductHolder.INSTANCE.fourthPack.clear();
		DetailedProductHolder.INSTANCE.fourthPack.put(nameFourthPack, priceFourthPack);
	}
	
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
	
	public String getNameSecondPack() {
		return DetailedProductHolder.INSTANCE.secondPack.keys().nextElement();
	}
	
	public Double getPriceSecondPack() {
		return DetailedProductHolder.INSTANCE.secondPack.get(DetailedProductHolder.INSTANCE.secondPack.keys().nextElement());
	}
	
	public String getNameThirdPack() {
		return DetailedProductHolder.INSTANCE.thirdPack.keys().nextElement();
	}
	
	public Double getPriceThirdPack() {
		return DetailedProductHolder.INSTANCE.thirdPack.get(DetailedProductHolder.INSTANCE.thirdPack.keys().nextElement());
	}
	
	public String getNameFourthPack() {
		return DetailedProductHolder.INSTANCE.fourthPack.keys().nextElement();
	}
	
	public Double getPriceFourthPack() {
		return DetailedProductHolder.INSTANCE.fourthPack.get(DetailedProductHolder.INSTANCE.fourthPack.keys().nextElement());
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