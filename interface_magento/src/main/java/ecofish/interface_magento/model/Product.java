package ecofish.interface_magento.model;

/**
 * Product model
 * @author Jean-Florian Tassart
 */
public class Product implements Comparable<Product>{
	
	private final Integer idProduct;
	private final String category;
	private final String family;
	private final String name;
	private final String quality;
	private final String size;

	private Double actualPrice;
	private Double newPrice;
	private Boolean active;
	private Boolean changeActive;
	
	/**
	 * Constructor of product
	 * @param idProduct - unique product ID
	 * @param name - product name
	 * @param category - product category
	 * @param family - product family
	 * @param quality - product quality
	 * @param size - product size
	 * @param actualPrice - actual product price
	 * @param active - active or inactive status product
	 */
	public Product(Integer idProduct, String category, String family, String name, String quality, String size, Double actualPrice, Boolean active) {
		this.idProduct = idProduct;
		this.category = category;
		this.family = family;
		this.name = name;
		this.quality = quality;
		this.size = size;
		this.actualPrice = actualPrice;
		this.newPrice = null;
		this.active = active;
		this.changeActive = false;
	}
	
	/*public void setName(String name) {
		this.name = name;
	}
	
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	public void setSize(String size) {
		this.size = size;
	}*/
	
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
	
	public Integer getIdProduct() {
		return idProduct;
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
		return "Name : " + name + "\n" + "Size : " + size + "\n" + "Quality : " + quality + "\n";
	}
	
	/**
	 * Comparison order : category, family, name, size, quality, idProduct
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
		else if (this.getSize().compareTo(product.getSize()) != 0) {
			return this.getSize().compareTo(product.getSize());
		}
		else if (this.getQuality().compareTo(product.getQuality()) != 0) {
			return this.getQuality().compareTo(product.getQuality());
		}
		else {
			return this.getIdProduct().compareTo(product.getIdProduct()); 
		}
	}
	
}