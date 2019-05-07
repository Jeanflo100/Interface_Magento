package ecofish.interface_magento.model;

public class Product{
	
	private String name;
	private String quality;
	private String size;
	private Double price;
	
	public Product() {
		name = "";
		quality = "";
		size = "";
		price = 0.00;
	}
	
	public Product(String name, String quality, String size, double price) {
		this.name = name;
		this.quality = quality;
		this.size = size;
		this.price = price;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public String getQuality() {
		return quality;
	}
	
	public String getSize() {
		return size;
	}
	
	public Double getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return name + "\nQuality : " + quality + "\nSize : " + size;
	}
	
}