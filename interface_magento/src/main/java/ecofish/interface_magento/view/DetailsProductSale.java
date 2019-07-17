package ecofish.interface_magento.view;

import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.util.TextFormatterDouble;
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
		
		initComponents();		
		modificationDetails(false, false);
	}

	private void initComponents() {
		TextFormatterDouble actualPriceTextFormatter = new TextFormatterDouble();
		this.actualPriceTextField.setTextFormatter(actualPriceTextFormatter.getTextFormatterDouble());
		TextFormatterDouble pricePack2TextFormatter = new TextFormatterDouble();
		this.pricePack2TextField.setTextFormatter(pricePack2TextFormatter.getTextFormatterDouble());
		TextFormatterDouble pricePack3TextFormatter = new TextFormatterDouble();
		this.pricePack3TextField.setTextFormatter(pricePack3TextFormatter.getTextFormatterDouble());
		TextFormatterDouble pricePack4TextFormatter = new TextFormatterDouble();
		this.pricePack4TextField.setTextFormatter(pricePack4TextFormatter.getTextFormatterDouble());
	}
	
	private void setContentComponents() {
		this.basicPackTextField.setText(this.detailedProduct.getBasicPack());
		this.actualPriceTextField.setText(this.detailedProduct.getActualPrice().toString());
		this.pack2TextField.setText(this.detailedProduct.getNameSecondPack());
		this.pricePack2TextField.setText(this.detailedProduct.getPriceSecondPack().toString());
		this.pack3TextField.setText(this.detailedProduct.getNameThirdPack());
		this.pricePack3TextField.setText(this.detailedProduct.getPriceThirdPack().toString());
		this.pack4TextField.setText(this.detailedProduct.getNameFourthPack());
		this.pricePack4TextField.setText(this.detailedProduct.getPriceFourthPack().toString());
	}

	private void saveModification() {
		this.detailedProduct.setBasicPack(this.basicPackTextField.getText());
		this.detailedProduct.setActualPrice(Double.parseDouble(this.actualPriceTextField.getText()));
		this.detailedProduct.setSecondPack(this.pack2TextField.getText(), Double.parseDouble(this.pricePack2TextField.getText()));
		this.detailedProduct.setThirdPack(this.pack3TextField.getText(), Double.parseDouble(this.pricePack3TextField.getText()));
		this.detailedProduct.setFourthPack(this.pack4TextField.getText(), Double.parseDouble(this.pricePack4TextField.getText()));
	}

	@Override
	public void modificationDetails(Boolean isModification, Boolean isSave) {
		this.basicPackTextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.actualPriceTextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.pack2TextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.pricePack2TextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.pack3TextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.pricePack3TextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.pack4TextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.pricePack4TextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.basicPackTextField.setEditable(isModification);
		this.actualPriceTextField.setEditable(isModification);
		this.pack2TextField.setEditable(isModification);
		this.pricePack2TextField.setEditable(isModification);
		this.pack3TextField.setEditable(isModification);
		this.pricePack3TextField.setEditable(isModification);
		this.pack4TextField.setEditable(isModification);
		this.pricePack4TextField.setEditable(isModification);
		if (isSave != null) {
			if (isSave) saveModification();
			else if (!isSave) setContentComponents();
		}	
	}
	
}