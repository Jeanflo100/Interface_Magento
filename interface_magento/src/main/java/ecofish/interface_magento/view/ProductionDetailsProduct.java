package ecofish.interface_magento.view;

import java.util.Calendar;

import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.model.DetailedProduct.season;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ProductionDetailsProduct implements DetailsProductInterface {
	
	private final DetailedProduct detailedProduct;

	private final GridPane seasonsGridPane;
	private final Label currentSeasonLabel;
	
	private season selectedSeason;
    private Node selectedLegend;

    private season[] seasons_tmp;
    

	
	public ProductionDetailsProduct(DetailedProduct detailedProduct, GridPane seasonsGridPane, Label currentSeasonLabel) {
		this.detailedProduct = detailedProduct;
		this.seasonsGridPane = seasonsGridPane;
		this.currentSeasonLabel = currentSeasonLabel;
		
		initSeasons();
	}
	
	private void initSeasons() {
		seasons_tmp = detailedProduct.getSeasons();
		selectedSeason = null;
		
		Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		Integer month = 0;
		for (Node node : seasonsGridPane.getChildren()) {
			if (month < 12) {
				node.setId(month.toString());
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(detailedProduct.getSeason(month)), true);
				if (month == currentMonth) {
					node.pseudoClassStateChanged(PseudoClass.getPseudoClass(season.current.name()), true);
					currentSeasonLabel.setText(detailedProduct.getSeason(month).replaceFirst(".",(detailedProduct.getSeason(month).charAt(0)+"").toUpperCase()));
				}
				node.setOnMouseClicked(click -> {
					if (selectedSeason != null) {
						Node clickedNode = click.getPickResult().getIntersectedNode().getClass() != Label.class ? click.getPickResult().getIntersectedNode().getParent() : click.getPickResult().getIntersectedNode();
						clickedNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(selectedSeason.name()), true);
						changeSeason(clickedNode, selectedSeason.name());
						seasons_tmp[Integer.parseInt(clickedNode.getId())] = selectedSeason;
					}
				});
				month++;
			}
		}
	}
	
	private void changeSeason(Node node, String toSeason) {
		for (PseudoClass pseudoClass : node.getPseudoClassStates()) {
			if (pseudoClass.getPseudoClassName().equals(season.current.name())) {
				currentSeasonLabel.setText(toSeason.replaceFirst(".",(toSeason.charAt(0)+"").toUpperCase()));						
			}
			else if (!pseudoClass.getPseudoClassName().equals("hover") && !pseudoClass.getPseudoClassName().equals(toSeason)) {
				node.pseudoClassStateChanged(pseudoClass, false);
			}
		}
	}
	
	private void cancelModificationProductionView() {
		Integer month = 0;
		for (Node node : seasonsGridPane.getChildren()) {
			if (month < 12) {
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(detailedProduct.getSeason(month)), true);
				changeSeason(node, detailedProduct.getSeason(month));
				month++;
			}
		}
		detailedProduct.getSeasons(seasons_tmp);
	}
	
	protected void selectSeason(MouseEvent click) {
		if (selectedLegend != null) selectedLegend.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
		if (click != null) {
			selectedLegend = click.getPickResult().getIntersectedNode();
			selectedLegend.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
			
			String style = click.getPickResult().getIntersectedNode().getStyle();
			if (style.contains("-fx-fill:")) {
				String valueSeason = style.substring(style.indexOf("-fx-fill: ") + "-fx-fill: ".length(), style.indexOf(';', style.indexOf("-fx-fill: ") + "-fx-fill: ".length()));	
				selectedSeason = season.valueOf(valueSeason);
			}
			else selectedSeason = null;
		}
		else selectedSeason = null;
	}
	
	@Override
	public void modificationDetails(Boolean isModification, Boolean isSave) {
		if (isModification) {
			
		}
		else {
			selectSeason(null);
			if (isSave) {
				detailedProduct.setSeasons(seasons_tmp);
			}
			else {
				cancelModificationProductionView();
			}
		}
	}
	
}