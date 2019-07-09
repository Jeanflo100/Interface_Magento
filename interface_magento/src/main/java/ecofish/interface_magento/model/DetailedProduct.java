package ecofish.interface_magento.model;

import java.util.ArrayList;

public class DetailedProduct extends Product {
	
	public enum season{high, medium, low, unspecified, current;};
	
	private ArrayList<String> country_of_manufacture;
	private String description;
	private String short_description;
	private String ean_code;
	private String ec_sales_code;
	private String latin_name;
	private String production_type;
	private ArrayList<String> alergens;
	private ArrayList<String> brand;
	private ArrayList<String> label;
	private season[] seasons;
	
	private void initDetails() {
		seasons = new season[12];
		seasons[0] = seasons[1] = seasons[2] = seasons[10] = seasons[11] = season.low;
		seasons[3] = seasons[4] = seasons[9] = season.medium;
		seasons[5] = seasons[6] = seasons[7] = seasons[8] = season.high;
		
		production_type = "Sauvage";
	}
	
	private DetailedProduct(Product product , String short_description) {
		super(product);
		this.short_description = short_description;
		
		initDetails();
	}
	
	public void setLabel(String short_description) {
		DetailedProductHolder.INSTANCE.short_description = short_description;
	}
	
	public void setSeasons(season[] seasons) {
		System.arraycopy(seasons, 0, DetailedProductHolder.INSTANCE.seasons, 0, DetailedProductHolder.INSTANCE.seasons.length);
	}
	
	public String getShortDescription() {
		return DetailedProductHolder.INSTANCE.short_description;
	}
	
	public String getProductionType() {
		return production_type;
	}
	
	public String getSeason(Integer month) {
		return DetailedProductHolder.INSTANCE.seasons[month].name();
	}
	
	public season[] getSeasons() {
		season[] seasons = new season[12];
		return getSeasons(seasons);
	}
	
	public season[] getSeasons(season[] seasons) {
		System.arraycopy(DetailedProductHolder.INSTANCE.seasons, 0, seasons, 0, DetailedProductHolder.INSTANCE.seasons.length);
		return seasons;
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
		private static DetailedProduct INSTANCE = new DetailedProduct(new Product(103, "Agrumes", "Orange", "Orange New Land", "3", "II", 1.58, true), "");
	}
	
}