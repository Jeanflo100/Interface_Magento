package ecofish.interface_magento.view;

import java.util.Calendar;

import ecofish.interface_magento.model.DetailedProduct;
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
	
	public enum season{high, medium, low, unspecified, current;};
	
	private DetailedProduct product;
	
	private season selectedSeason;
	
	private Node selectedLegend;
	
    private season[] seasons_tmp;
    
	@FXML
	private void initialize() {
		product = DetailedProduct.getProduct();
		
		initTitleView();
		initProductionView();
	}
	
	private void initTitleView() {
		this.skuLabel.setText(product.getIdProduct().toString());
		this.descriptionLabel.setText(
				product.getCategory()
				+ (product.getFamily() != null ? " - " + product.getFamily() : "")
				+ " - " + product.getName()
				+ (product.getSize() != null ? " - " + product.getSize() : "")
				+ (product.getQuality() != null ? " - " + product.getQuality() : "")
				);
	}
	
	private void initProductionView() {
		initSeasons();
	}
	
	private void initSeasons() {
		seasons_tmp = DetailedProduct.getSeasons();
		selectedSeason = null;
		
		Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		Integer month = 0;
		for (Node node : seasonGridPane.getChildren()) {
			if (month < 12) {
				node.setId(month.toString());
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(DetailedProduct.getSeason(month)), true);
				if (month == currentMonth) {
					node.pseudoClassStateChanged(PseudoClass.getPseudoClass(season.current.name()), true);
					currentSeasonLabel.setText(DetailedProduct.getSeason(month).replaceFirst(".",(DetailedProduct.getSeason(month).charAt(0)+"").toUpperCase()));
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
						seasons_tmp[Integer.parseInt(clickedNode.getId())] = selectedSeason;
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
		if (this.productionAnchorPane.isMouseTransparent()) {
			selectSeason(null);
		}
	}
	
	@FXML
	private void saveModificationProductionView() {
		DetailedProduct.setSeasons(seasons_tmp);
		changeStateModificationProductionView();
	}
	
	@FXML
	private void cancelModificationProductionView() {
		Integer month = 0;
		for (Node node : seasonGridPane.getChildren()) {
			if (month < 12) {
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(DetailedProduct.getSeason(month)), true);
				for (PseudoClass pseudoClass : node.getPseudoClassStates()) {
					if (pseudoClass.getPseudoClassName().equals(season.current.name())) {
						currentSeasonLabel.setText(selectedSeason.name().replaceFirst(".",(selectedSeason.name().charAt(0)+"").toUpperCase()));						
					}
					else if (!pseudoClass.getPseudoClassName().equals("hover") && !pseudoClass.getPseudoClassName().equals(DetailedProduct.getSeason(month))) {
						node.pseudoClassStateChanged(pseudoClass, false);
					}
				}
				month++;
			}
		}
		DetailedProduct.getSeasons(seasons_tmp);
	}
	
	@FXML
	private void stopModificationProductionView() {
		cancelModificationProductionView();
		changeStateModificationProductionView();
	}
	
	@FXML
	private void selectSeason(MouseEvent click) {
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
	
}