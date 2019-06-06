package ecofish.interface_magento.model;


public class Product implements Comparable<Product>{
	
	private String sku;
	private String category;
	private String family;
	private String name;
	private String size;
	private String quality;
	private Double newPrice;
	private Double actualPrice;
	private Boolean active;
	private Boolean changeActive;
	
	public Product(String sku, String category, String family, String name, String size, String quality, Double actualPrice, Boolean active) {
		this.sku = sku;
		this.category = category;
		this.family = family;
		this.name = name;
		this.size = size;
		this.quality = quality;
		this.actualPrice = actualPrice;
		this.newPrice = null;
		this.active = active;
		this.changeActive = false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}
	
	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public void setChangeActive(Boolean changeActive) {
		this.changeActive = changeActive;
	}
	
	public String getSku() {
		return sku;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getFamily() {
		return family;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSize() {
		return size;
	}
	
	public String getQuality() {
		return quality;
	}
	
	public Double getNewPrice() {
		return newPrice;
	}
	
	public Double getActualPrice() {
		return actualPrice;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public Boolean getChangeActive() {
		return changeActive;
	}
	
	@Override
	public String toString() {
		return name + "\nSize : " + size + "\nQuality : " + quality;
	}

	@Override
	public int compareTo(Product product) {
		if (this.getName().compareTo(product.getName()) != 0) {
			return this.getName().compareTo(product.getName());
		}
		else if (this.getSize().compareTo(product.getSize()) != 0) {
			return this.getSize().compareTo(product.getSize());
		}
		else if (this.getQuality().compareTo(product.getQuality()) != 0) {
			return this.getQuality().compareTo(product.getQuality());
		}
		else {
			return this.getSku().compareTo(product.getSku()); 
		}
	}
	
}