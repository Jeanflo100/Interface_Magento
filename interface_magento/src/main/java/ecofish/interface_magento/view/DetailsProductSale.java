package ecofish.interface_magento.view;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.util.TextFormatterDouble;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

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
	
	private final SimpleDateFormat formatCalendar = new SimpleDateFormat("dd-MM-yyyy");
	
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
		this.priceLineChart.getData().add(new Series<String, Double>());
		this.priceLineChart.getData().get(0).getData().clear();
		this.detailedProduct.getPricehistory().forEach((key, value) -> {
			this.priceLineChart.getData().get(0).getData().add(new Data<String, Double>(formatCalendar.format(key.getTimeInMillis()), value));
		});
		addTooltipPriceHistory();
	}
	
	/**
	 * Add tooltips indicating the exact value at each node of each line in the chart
	 */
	private void addTooltipPriceHistory() {
		Set<Node> nodes = new HashSet<Node>();
		for (int i=0; i<this.priceLineChart.getData().size(); i++) {
			nodes.addAll(priceLineChart.lookupAll(".chart-line-symbol.series" + i + "."));
		}
		nodes.forEach((node) -> {
			node.setOnMouseEntered((MouseEvent event) -> {
				String point = event.getSource().toString();
				Integer serieNumber = Integer.parseInt(point.substring(point.indexOf("series") + "series".length(), point.indexOf(" ", point.indexOf("series"))));
				Integer dataNumber = Integer.parseInt(point.substring(point.indexOf("data") + "data".length(), point.indexOf(" ", point.indexOf("data"))));
				Tooltip t = new Tooltip(this.priceLineChart.getData().get(serieNumber).getData().get(dataNumber).getYValue().toString());
				Tooltip.install(node, t);
			});
		});
	}
	
	private void setContentComponents() {
		this.detailedProduct.setNewBasicPack(null);
		this.detailedProduct.setNewPrice(null);
		this.detailedProduct.setNewSecondPack(null);
		this.detailedProduct.setNewThirdPack(null);
		this.detailedProduct.setNewFourthPack(null);
		
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
		this.detailedProduct.setNewBasicPack(this.basicPackTextField.getText());
		this.detailedProduct.setNewPrice(Double.parseDouble(this.actualPriceTextField.getText()));
		this.detailedProduct.setNewSecondPack(Collections.singletonMap(this.pack2TextField.getText(), Double.parseDouble(this.pricePack2TextField.getText())));
		this.detailedProduct.setNewThirdPack(Collections.singletonMap(this.pack3TextField.getText(), Double.parseDouble(this.pricePack3TextField.getText())));
		this.detailedProduct.setNewFourthPack(Collections.singletonMap(this.pack4TextField.getText(), Double.parseDouble(this.pricePack4TextField.getText())));
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