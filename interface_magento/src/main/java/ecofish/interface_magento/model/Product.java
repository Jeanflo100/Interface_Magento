package ecofish.interface_magento.model;

/**
 * Product model
 * @author Jean-Florian Tassart
 */
public class Product implements Cloneable, Comparable<Product>{
	
	private Integer idProduct;
	private String name;
	private String category;
	private String family;
	private String quality;
	private String size;
	private Double newPrice;
	private Double actualPrice;
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
	public Product(Integer idProduct, String name, String category, String family, String quality, String size, Double actualPrice, Boolean active) {
		this.idProduct = idProduct;
		this.name = name;
		this.category = category;
		this.family = family;
		this.quality = quality;
		this.size = size;
		this.actualPrice = actualPrice;
		this.newPrice = null;
		this.active = active;
		this.changeActive = false;
	}
	
	public Product (Product product) {
		this.idProduct = product.getIdProduct();
		this.name = product.getName();
		this.category = product.getCategory();
		this.family = product.getFamily();
		this.quality = product.getQuality();
		this.size = product.getSize();
		this.actualPrice = product.getActualPrice();
		this.newPrice = product.getNewPrice();
		this.active = product.getActive();
		this.changeActive = product.getChangeActive();
		System.out.println("passage");
	}
	
	@Override
	public Product clone() throws CloneNotSupportedException {
		return (Product)super.clone();
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
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public void setChangeActive(Boolean changeActive) {
		this.changeActive = changeActive;
	}
	
	public Integer getIdProduct() {
		return idProduct;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getFamily() {
		return family;
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