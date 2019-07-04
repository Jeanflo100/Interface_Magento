package ecofish.interface_magento.model;

import java.util.ArrayList;

import ecofish.interface_magento.view.DetailsProductOverviewController.season;

public class DetailedProduct extends Product {
	
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
	
	private void initSeasons() {
		seasons = new season[12];
		seasons[0] = seasons[1] = seasons[2] = seasons[10] = seasons[11] = season.low;
		seasons[3] = seasons[4] = seasons[9] = season.medium;
		seasons[5] = seasons[6] = seasons[7] = seasons[8] = season.high;
	}
	
	private DetailedProduct(Product product , String short_description) {
		super(product);
		this.short_description = short_description;
		this.
		
		initSeasons();
	}
	
	public void setLabel(String short_description) {
		DetailedProductHolder.INSTANCE.short_description = short_description;
	}
	
	public static void setSeasons(season[] seasons) {
		System.arraycopy(seasons, 0, DetailedProductHolder.INSTANCE.seasons, 0, DetailedProductHolder.INSTANCE.seasons.length);
	}
	
	public String getShortDescription() {
		return DetailedProductHolder.INSTANCE.short_description;
	}
	
	public static season[] getSeasons() {
		season[] seasons = new season[12];
		System.arraycopy(DetailedProductHolder.INSTANCE.seasons, 0, seasons, 0, DetailedProductHolder.INSTANCE.seasons.length);
		return seasons;
	}
	
	public static season[] getSeasons(season[] seasons) {
		System.arraycopy(DetailedProductHolder.INSTANCE.seasons, 0, seasons, 0, DetailedProductHolder.INSTANCE.seasons.length);
		return seasons;
	}
	
	public static String getSeason(Integer month) {
		return DetailedProductHolder.INSTANCE.seasons[month].name();
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