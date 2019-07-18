package ecofish.interface_magento.view;

import java.util.Calendar;

import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.model.DetailedProduct.season;
import ecofish.interface_magento.service.GlobalDetails;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DetailsProductProduction implements DetailsProductInterface {
	
	private final DetailedProduct detailedProduct;
	
	private final AnchorPane productionModificationAnchorPane;
	private final Label productionTypeLabel;
	private final CheckBox statusCheckBox;
	private final AnchorPane seasonAnchorPane;
	private final Label actualSeasonLabel;
	private final GridPane seasonsGridPane;
	private final VBox countryOfManufactureVBox;
	
	private season selectedSeason;
    private Node selectedLegend;
	
	public DetailsProductProduction(DetailedProduct detailedProduct, AnchorPane productionModificationAnchorPane, Label productionTypeLabel, CheckBox statusCheckBox, AnchorPane seasonAnchorPane, Label actualSeasonLabel, GridPane seasonsGridPane, VBox countryOfManufactureVBox) {
		this.detailedProduct = detailedProduct;
		this.productionModificationAnchorPane = productionModificationAnchorPane;
		this.productionTypeLabel = productionTypeLabel;
		this.statusCheckBox = statusCheckBox;
		this.seasonAnchorPane = seasonAnchorPane;
		this.actualSeasonLabel = actualSeasonLabel;
		this.seasonsGridPane = seasonsGridPane;
		this.countryOfManufactureVBox = countryOfManufactureVBox;

		initComponents();
		modificationDetails(false, false);
	}
	
	private void initComponents() {
		ComboBox<String> productionTypeComboBox = new ComboBox<String>();
		productionTypeComboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		productionTypeComboBox.setItems(GlobalDetails.getProductionTypes());
		productionTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null && !GlobalDetails.getProductionTypes().contains(oldValue)) productionTypeComboBox.getSelectionModel().select(null);
			else this.productionTypeLabel.setText(newValue);
		});
		this.productionTypeLabel.textProperty().addListener((obersavable, oldValue, newValue) -> {
			productionTypeComboBox.getSelectionModel().select(newValue);
		});
		this.productionTypeLabel.setGraphic(productionTypeComboBox);
		
		this.statusCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) this.statusCheckBox.setText("Active");
			else this.statusCheckBox.setText("Inactive");
		});
		
		Integer month = 0;
		for (Node node : seasonsGridPane.getChildren().subList(0, DetailedProduct.nb_month)) {
			node.setOnMouseClicked(click -> {
				if (selectedSeason != null) {
					Node clickedNode = click.getPickResult().getIntersectedNode().getClass() != Label.class ? click.getPickResult().getIntersectedNode().getParent() : click.getPickResult().getIntersectedNode();
					changeSeason(clickedNode, selectedSeason);
				}
			});
			month++;
		}
	}

	private void setContentComponents() {
		this.detailedProduct.setNewProductionType(null);
		this.detailedProduct.setChangeActive(null);
		this.detailedProduct.setNewSeasons(null);
		this.detailedProduct.setNewCountriesOfManufacture(null);
		
		this.productionTypeLabel.setText(this.detailedProduct.getProductionType());
		this.statusCheckBox.setSelected(this.detailedProduct.getActive());
		Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		Integer month = 0;
		for (Node node : seasonsGridPane.getChildren().subList(0, DetailedProduct.nb_month)) {
			changeSeason(node, this.detailedProduct.getSeason(month));
			if (month == currentMonth) {
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(season.current.name()), true);
				actualSeasonLabel.setText(this.detailedProduct.getSeason(month).name().replaceFirst(".",(this.detailedProduct.getSeason(month).name().charAt(0)+"").toUpperCase()));
			}
			else node.pseudoClassStateChanged(PseudoClass.getPseudoClass(season.current.name()), false);
			month++;
		}
		GlobalDetails.setSelectedCountriesOfManufacture(detailedProduct.getCountriesOfManufacture());
		resetList();
	}
	
	private void resetList() {
		this.countryOfManufactureVBox.getChildren().clear();
		this.countryOfManufactureVBox.getChildren().addAll(GlobalDetails.getCountryOfManufacture());
	}
	
	private void saveModification() {
		this.detailedProduct.setNewProductionType(this.productionTypeLabel.getText());
		this.detailedProduct.setChangeActive(this.statusCheckBox.isSelected());
		season[] seasons = new season[DetailedProduct.nb_month];
		Integer month = 0;
		for (Node node : seasonsGridPane.getChildren().subList(0, DetailedProduct.nb_month)) {
			for (PseudoClass pseudoClass : node.getPseudoClassStates()) {
				for (season element : season.values()) {
					if (!element.equals(season.current) && element.name().equals(pseudoClass.getPseudoClassName())) seasons[month] = season.valueOf(pseudoClass.getPseudoClassName());
				}
			}
			month++;
		}
		this.detailedProduct.setNewSeasons(seasons);
		this.detailedProduct.setNewCountriesOfManufacture(GlobalDetails.getSelectedCountriesOfManufacture());
	}
	
	private void changeSeason(Node node, season toSeason) {
		node.pseudoClassStateChanged(PseudoClass.getPseudoClass(toSeason.name()), true);
		for (PseudoClass pseudoClass : node.getPseudoClassStates()) {
			if (pseudoClass.getPseudoClassName().equals(season.current.name())) {
				this.actualSeasonLabel.setText(toSeason.name().replaceFirst(".",(toSeason.name().charAt(0)+"").toUpperCase()));						
			}
			else {
				for (season element : season.values()) {
					if (!element.equals(toSeason) && element.name().equals(pseudoClass.getPseudoClassName())) node.pseudoClassStateChanged(pseudoClass, false);
				}
			}
		}
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
		this.productionModificationAnchorPane.setVisible(isModification);
		if (isModification) this.productionTypeLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		else this.productionTypeLabel.setContentDisplay(ContentDisplay.TEXT_ONLY);
		this.statusCheckBox.setMouseTransparent(!isModification);
		this.seasonAnchorPane.setMouseTransparent(!isModification);
		if (!isModification) selectSeason(null);
		this.countryOfManufactureVBox.setMouseTransparent(!isModification);
		if (isSave != null) {
			if (isSave) {
				saveModification();
			}
			else if (!isSave) {
				setContentComponents();
			}
		}
		GlobalDetails.onlySelectedCountryOfManufacture(!isModification);
		resetList();
	}
	
	protected void changeChoicesProductionType() {
		ModificationChoicesDetailsProductController.setCharacteristic("Production type");
		ModificationChoicesDetailsProductController.setChoices(GlobalDetails.getProductionTypesSource());
		StageService.showView(Views.viewsSecondaryStage.ModificationChoicesDetailsProduct, true);
	}
	
	protected void changeChoicesCountryOfManufacture() {
		ModificationChoicesDetailsProductController.setCharacteristic("Country of manufacture");
		ModificationChoicesDetailsProductController.setChoices(GlobalDetails.getCountriesOfManufactureSource());
		StageService.showView(Views.viewsSecondaryStage.ModificationChoicesDetailsProduct, true);
		this.countryOfManufactureVBox.getChildren().clear();
		this.countryOfManufactureVBox.getChildren().addAll(GlobalDetails.getCountryOfManufacture());
	}
	
}