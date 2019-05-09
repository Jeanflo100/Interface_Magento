package ecofish.interface_magento.model;


public class Product{
	
	private String name;
	private String quality;
	private String size;
	private Double newPrice;
	private Double actualPrice;
	
	public Product() {
		name = "";
		quality = "";
		size = "";
		actualPrice = null;
		newPrice = null;
	}
	
	public Product(String name, String quality, String size, double actualPrice) {
		this.name = name;
		this.quality = quality;
		this.size = size;
		this.actualPrice = actualPrice;
		this.newPrice = null;
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
	
	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}
	
	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
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
	
	public Double getNewPrice() {
		return newPrice;
	}
	
	public Double getActualPrice() {
		return actualPrice;
	}
	
	@Override
	public String toString() {
		return name + "\nQuality : " + quality + "\nSize : " + size;
	}
	
}