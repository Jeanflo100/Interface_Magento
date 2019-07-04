package ecofish.interface_magento.view;

import java.util.Calendar;


import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class DetailsProductOverviewController {
	
	@FXML
	Label skuLabel;
	
	@FXML
	Label descriptionLabel;
	
	@FXML
	Label currentSeasonLabel;
	
	@FXML
	ImageView administrativeModifiedImage;
	
	@FXML
	ImageView characteristicModifiedImage;
	
	@FXML
	ImageView saleModifiedImage;
	
	@FXML
	ImageView productionModifiedImage;
	
	@FXML
	AnchorPane administrativeModificationAnchorPane;

	@FXML
	AnchorPane characteristicModificationAnchorPane;

	@FXML
	AnchorPane saleModificationAnchorPane;

	@FXML
	AnchorPane productionModificationAnchorPane;
	
	@FXML
	AnchorPane productionAnchorPane;
	
	@FXML
	GridPane seasonGridPane;
    
	private enum season{high, medium, low, unspecified, current;};
	
	private season selectedSeason;
	
	private Node selectedLegend;
	
    private season[] seasons = new season[12];
    
    private void test() {
    	seasons[0] = seasons[1] = seasons[2] = seasons[10] = seasons[11] = season.low;
    	seasons[3] = seasons[4] = seasons[9] = season.medium;
    	seasons[5] = seasons[6] = seasons[7] = seasons[8] = season.high;
    }
    
	@FXML
	private void initialize() {
		test();
		
		initSeasons();
	}
	
	/*private void initSeasons() {
		for (String month : high.split("/")) {
			
		}
	}*/
	
	private void initSeasons() {
		selectedSeason = null;
		
		Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		Integer month = 0;
		for (Node node : seasonGridPane.getChildren()) {
			if (month < 12) {
				node.setId(month.toString());
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(seasons[month].name()), true);
				if (month == currentMonth) {
					node.pseudoClassStateChanged(PseudoClass.getPseudoClass(season.current.name()), true);
					currentSeasonLabel.setText(seasons[currentMonth].name().replaceFirst(".",(seasons[currentMonth].name().charAt(0)+"").toUpperCase()));
				}
				node.setOnMouseClicked(click -> {
					if (selectedSeason != null) {
						Node clickedNode = click.getPickResult().getIntersectedNode().getClass() != Label.class ? click.getPickResult().getIntersectedNode().getParent() : click.getPickResult().getIntersectedNode();
						clickedNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(selectedSeason.name()), true);
						for (PseudoClass pseudoClass : clickedNode.getPseudoClassStates()) {
							if (pseudoClass.getPseudoClassName().equals(season.current.name())) {
								currentSeasonLabel.setText(selectedSeason.name().replaceFirst(".",(selectedSeason.name().charAt(0)+"").toUpperCase()));						
							}
							else if (!pseudoClass.getPseudoClassName().equals("hover") && !pseudoClass.getPseudoClassName().equals(selectedSeason.name())) {
								clickedNode.pseudoClassStateChanged(pseudoClass, false);
							}
						}
						seasons[Integer.parseInt(clickedNode.getId())] = selectedSeason;
					}
				});
				month++;
			}
		}
	}
	
	@FXML
	private void changeStateModificationProductionView() {
		this.productionModifiedImage.setVisible(!this.productionModifiedImage.isVisible());
		this.productionModificationAnchorPane.setVisible(!this.productionModificationAnchorPane.isVisible());
		this.productionAnchorPane.setMouseTransparent(!this.productionAnchorPane.isMouseTransparent());
	}
	
	@FXML
	private void selectSeason(MouseEvent click) {
		if (selectedLegend != null) selectedLegend.setStyle(selectedLegend.getStyle() + " -fx-stroke: grey;");
		if (click != null) {
			selectedLegend = click.getPickResult().getIntersectedNode();
			selectedLegend.setStyle(selectedLegend.getStyle() + " -fx-stroke: blue;");
			System.out.println(selectedLegend.getStyle());
			String style = click.getPickResult().getIntersectedNode().getStyle();
			String valueSeason = style.substring(style.indexOf("-fx-fill: ") + "-fx-fill: ".length(), style.indexOf(';', style.indexOf("-fx-fill: ") + "-fx-fill: ".length()));			
			selectedSeason = season.valueOf(valueSeason);
		}

	}
	
}