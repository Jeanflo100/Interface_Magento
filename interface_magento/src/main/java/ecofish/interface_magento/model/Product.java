package ecofish.interface_magento.model;

/**
 * Product model
 * @author Jean-Florian Tassart
 */
public class Product implements Comparable<Product>{
	
	private final String sku;
	private final String category;
	private final String family;
	private final String name;
	private final String size;
	private final String quality;

	private Double actualPrice;
	private Double newPrice;
	private Boolean active;
	private Boolean changeActive;
	
	/**
	 * Constructor of product
	 * @param sku - unique product ID
	 * @param category - product category
	 * @param family - product family
	 * @param name - product name
	 * @param quality - product quality
	 * @param size - product size
	 * @param actualPrice - actual product price
	 * @param active - active or inactive status product
	 */
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
	
	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}
	
	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
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
	
	public Double getActualPrice() {
		return actualPrice;
	}
	
	public Double getNewPrice() {
		return newPrice;
	}
	
	public Boolean getActive() {
		return active;
	}
	
	public Boolean getChangeActive() {
		return changeActive;
	}
	
	@Override
	public String toString() {
		String description = "Name : " + name + "\n";
		if (size != null) description += "Size : " + size + "\n";
		if (quality != null) description += "Quality : " + quality + "\n";
		return description;
	}
	
	/**
	 * Comparison order : category, family, name, size, quality, sku
	 */
	@Override
	public int compareTo(Product product) {
		if (this.getCategory().compareTo(product.getCategory()) != 0) {
			return this.getCategory().compareTo(product.getCategory());
		}
		else if (this.getFamily().compareTo(product.getFamily()) != 0) {
			return this.getFamily().compareTo(product.getFamily());
		}
		else if (this.getName().compareTo(product.getName()) != 0) {
			return this.getName().compareTo(product.getName());
		}
		else if (this.getSize() != null && product.getSize() != null && this.getSize().compareTo(product.getSize()) != 0) {
			return this.getSize().compareTo(product.getSize());
		}
		else if (this.getQuality() != null && product.getQuality() != null && this.getQuality().compareTo(product.getQuality()) != 0) {
			return this.getQuality().compareTo(product.getQuality());
		}
		else {
			return this.getSku().compareTo(product.getSku()); 
		}
	}
	
}