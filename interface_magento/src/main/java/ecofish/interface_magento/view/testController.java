package ecofish.interface_magento.view;

import java.util.HashSet;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

public class testController {

	@FXML
	LineChart<String, Number> testLineChart;
	
	@FXML
	NumberAxis testNumberAxis;
	
	@FXML
	CategoryAxis testCategoryAxis;
	
	@FXML
	private void initialize() {
		this.testNumberAxis.setLabel("test");
		XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
		series1.getData().add(new XYChart.Data<String, Number>("Jan", 23));
        series1.getData().add(new XYChart.Data<String, Number>("Feb", 14));
        series1.getData().add(new XYChart.Data<String, Number>("Mar", 15));
        series1.getData().add(new XYChart.Data<String, Number>("Apr", 24));
        series1.getData().add(new XYChart.Data<String, Number>("May", 34));
        series1.getData().add(new XYChart.Data<String, Number>("Jun", 36));
        series1.getData().add(new XYChart.Data<String, Number>("Jul", 22));
        series1.getData().add(new XYChart.Data<String, Number>("Aug", 45));
        series1.getData().add(new XYChart.Data<String, Number>("Sep", 43));
        series1.getData().add(new XYChart.Data<String, Number>("Oct", 17));
        series1.getData().add(new XYChart.Data<String, Number>("Nov", 29));
        series1.getData().add(new XYChart.Data<String, Number>("Dec", 25));
		this.testLineChart.getData().add(series1);
		
		XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
		series2.getData().add(new XYChart.Data<String, Number>("Jan", 20));
        series2.getData().add(new XYChart.Data<String, Number>("Feb", 19));
        series2.getData().add(new XYChart.Data<String, Number>("Mar", 35));
        series2.getData().add(new XYChart.Data<String, Number>("Apr", 14));
        series2.getData().add(new XYChart.Data<String, Number>("May", 48));
        series2.getData().add(new XYChart.Data<String, Number>("Jun", 61));
        series2.getData().add(new XYChart.Data<String, Number>("Jul", 35));
        series2.getData().add(new XYChart.Data<String, Number>("Aug", 14));
        series2.getData().add(new XYChart.Data<String, Number>("Sep", 51));
        series2.getData().add(new XYChart.Data<String, Number>("Oct", 47));
        series2.getData().add(new XYChart.Data<String, Number>("Nov", 48));
        series2.getData().add(new XYChart.Data<String, Number>("Dec", 26));
		this.testLineChart.getData().add(series2);
		
		addTooltip();
		
	}
	
	/**
	 * Add tooltips indicating the exact value at each node of each line in the chart
	 */
	private void addTooltip() {
		Set<Node> nodes = new HashSet<Node>();
		for (int i=0; i<this.testLineChart.getData().size(); i++) {
			nodes.addAll(testLineChart.lookupAll(".chart-line-symbol.series" + i + "."));
		}
		nodes.forEach((node) -> {
			node.setOnMouseEntered((MouseEvent event) -> {
				String point = event.getSource().toString();
				Integer serieNumber = Integer.parseInt(point.substring(point.indexOf("series") + "series".length(), point.indexOf(" ", point.indexOf("series"))));
				Integer dataNumber = Integer.parseInt(point.substring(point.indexOf("data") + "data".length(), point.indexOf(" ", point.indexOf("data"))));
				Tooltip t = new Tooltip(this.testLineChart.getData().get(serieNumber).getData().get(dataNumber).getYValue().toString());
				Tooltip.install(node, t);
				//Label l = new Label(this.testLineChart.getData().get(serieNumber).getData().get(dataNumber).getYValue().toString());
				//l.
			});
		});
	}
	
}