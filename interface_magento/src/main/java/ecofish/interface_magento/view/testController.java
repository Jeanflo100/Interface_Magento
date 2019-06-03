package ecofish.interface_magento.view;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class testController {

	@FXML
	LineChart<Number, Number> testLineChart;
	
	@FXML
	NumberAxis testNumberAxis;
	
	@FXML
	CategoryAxis testCategoryAxis;
	
	@FXML
	private void initialize() {
		XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
	    series1.setName("Series 1");
	    series1.getData().add(new XYChart.Data<>(1, 20));
	    series1.getData().add(new XYChart.Data<>(2, 100));
	    series1.getData().add(new XYChart.Data<>(3, 80));
	    series1.getData().add(new XYChart.Data<>(4, 180));
	    series1.getData().add(new XYChart.Data<>(5, 20));
	    series1.getData().add(new XYChart.Data<>(6, -10));
	    testLineChart.getData().add(series1);
        //this.testCategoryAxis = new CategoryAxis();
        //xAxis.setLabel("Number of Month");
        //creating the chart
        //this.testLineChart = new LineChart(testNumberAxis, testCategoryAxis);

        /*//defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        series.setName("My portfolio");
        //populating the series with Data<Number, Number>
        series.getData().add(new XYChart.Data<Number, Number>(1, 23));
        series.getData().add(new XYChart.Data<Number, Number>(2, 14));
        series.getData().add(new XYChart.Data<Number, Number>(3, 15));
        series.getData().add(new XYChart.Data<Number, Number>(4, 24));
        series.getData().add(new XYChart.Data<Number, Number>(5, 34));
        series.getData().add(new XYChart.Data<Number, Number>(6, 36));
        series.getData().add(new XYChart.Data<Number, Number>(7, 22));
        series.getData().add(new XYChart.Data<Number, Number>(8, 45));
        series.getData().add(new XYChart.Data<Number, Number>(9, 43));
        series.getData().add(new XYChart.Data<Number, Number>(10, 17));
        series.getData().add(new XYChart.Data<Number, Number>(11, 29));
        series.getData().add(new XYChart.Data<Number, Number>(12, 25));
        
        this.testLineChart.getData().add(series);*/
		//this.testLineChart.setVisible(true);
        //this.testLineChart.
	}
}