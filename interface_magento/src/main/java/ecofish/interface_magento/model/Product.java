package ecofish.interface_magento.model;


public class Product implements Comparable<Product>{
	
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
	
	/*public Product() {
		this.name = "";
		this.quality = "";
		this.size = "";
		this.actualPrice = null;
		this.newPrice = null;
		this.active = true;
	}*/
	
	public Product(Integer idProduct, String name, String quality, String size, Double actualPrice, Boolean active) {
		this.idProduct = idProduct;
		this.name = name;
		this.quality = quality;
		this.size = size;
		this.actualPrice = actualPrice;
		this.newPrice = null;
		this.active = active;
		this.changeActive = false;
	}
	
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
			return this.getIdProduct().compareTo(product.getIdProduct()); 
		}
	}
	
}