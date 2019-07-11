package ecofish.interface_magento.view;

import ecofish.interface_magento.model.DetailedProduct;

import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;

public class DetailsProductSale implements DetailsProductInterface {

	private final DetailedProduct detailedProduct;
	
	private final TextField basicPackTextField;
	private final TextField actualPriceTextField;
	private final LineChart<String, Double> priceLineChart;
	private final TextField pack2TextField;
	private final TextField pricePack2TextField;
	private final TextField pack3TextField;
	private final TextField pricePack3TextField;
	private final TextField pack4TextField;
	private final TextField pricePack4TextField;
	
	public DetailsProductSale(DetailedProduct detailedProduct, TextField basicPackTextField, TextField actualPriceTextField, LineChart<String, Double> priceLineChart, TextField pack2TextField, TextField pricePack2TextField, TextField pack3TextField, TextField pricePack3TextField, TextField pack4TextField, TextField pricePack4TextField) {
		this.detailedProduct = detailedProduct;
		this.basicPackTextField = basicPackTextField;
		this.actualPriceTextField = actualPriceTextField;
		this.priceLineChart = priceLineChart;
		this.pack2TextField = pack2TextField;
		this.pricePack2TextField = pricePack2TextField;
		this.pack3TextField = pack3TextField;
		this.pricePack3TextField = pricePack3TextField;
		this.pack4TextField = pack4TextField;
		this.pricePack4TextField = pricePack4TextField;
	}

	@Override
	public void modificationDetails(Boolean isModification, Boolean isSave) {
	
	}
	
}