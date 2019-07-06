package ecofish.interface_magento.view;

import java.util.Calendar;

import ecofish.interface_magento.daos.GettingGlobalDetailsThread;
import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.service.GlobalDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	Label productionTypeLabel;
	
	@FXML
	TabPane detailsProductTabPane;
	
	@FXML
	ImageView addProductionTypeImage;
	
	@FXML
	ImageView modificationProductImage;
	
	@FXML
	ComboBox<String> productionTypeComboBox;
	
	@FXML
	AnchorPane administrativeModificationAnchorPane;

	@FXML
	AnchorPane characteristicModificationAnchorPane;

	@FXML
	AnchorPane saleModificationAnchorPane;

	@FXML
	AnchorPane productionModificationAnchorPane;
	
	@FXML
	AnchorPane modificationProductionAnchorPane;
	
	@FXML
	GridPane seasonGridPane;
	
	private enum section{Administrative, Characteristic, Production, Sale;};
	
	public enum season{high, medium, low, unspecified, current;};
	
	private DetailedProduct product;
	
	private season selectedSeason;
	
	private Node selectedLegend;
	
    private season[] seasons_tmp;
    
	@FXML
	private void initialize() {
		product = DetailedProduct.getProduct();
		ObservableList<String> productionTypeItems = FXCollections.observableArrayList();
		productionTypeItems.add("elevage");
		productionTypeItems.add("sauvage");
		productionTypeComboBox.setItems(productionTypeItems);

		initTitleView();
		initTabs();
		initProductionView();
	}
	
	private void initTitleView() {
		this.skuLabel.setText("Product: " + product.getIdProduct().toString());
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
		GettingGlobalDetailsThread.init();
		productionTypeLabel.setText(product.getProductionType());
	}
	
	private void initSeasons() {
		seasons_tmp = product.getSeasons();
		selectedSeason = null;
		
		Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		Integer month = 0;
		for (Node node : seasonGridPane.getChildren()) {
			if (month < 12) {
				node.setId(month.toString());
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(product.getSeason(month)), true);
				if (month == currentMonth) {
					node.pseudoClassStateChanged(PseudoClass.getPseudoClass(season.current.name()), true);
					currentSeasonLabel.setText(product.getSeason(month).replaceFirst(".",(product.getSeason(month).charAt(0)+"").toUpperCase()));
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
	
	/*@FXML
	private void changeStateModificationProductionView() {
		this.productionModifiedImage.setVisible(!this.productionModifiedImage.isVisible());
		this.productionModificationAnchorPane.setVisible(!this.productionModificationAnchorPane.isVisible());
		this.modificationProductionAnchorPane.setVisible(!this.modificationProductionAnchorPane.isVisible());
		if (this.modificationProductionAnchorPane.isVisible()) {
			selectSeason(null);
		}
	}*/
	
	private void initTabs() {
		for (Tab tab : this.detailsProductTabPane.getTabs()) {
			tab.getContent().setMouseTransparent(true);
		}
	}
	
	@FXML
	private void modificationProduct() {
		Tab selectedTab = this.detailsProductTabPane.getSelectionModel().getSelectedItem();
		for (Tab tab : this.detailsProductTabPane.getTabs()) {
			if (tab != selectedTab) {
				tab.setDisable(!tab.isDisable());
			}
		}
		selectedTab.getContent().setMouseTransparent(!selectedTab.getContent().isMouseTransparent());
		if (selectedTab.getText().equals(section.Administrative.name())) modificationAdministrativeProduct();
		else if (selectedTab.getText().equals(section.Characteristic.name())) modificationCharacteristicProduct();
		else if (selectedTab.getText().equals(section.Production.name())) modificationProductionProduct();
		else if (selectedTab.getText().equals(section.Sale.name())) modificationSaleProduct();
	}
	
	private void modificationAdministrativeProduct() {
		
	}
	
	private void modificationCharacteristicProduct() {
		
	}
	
	private void modificationProductionProduct() {
		addProductionTypeImage.setVisible(!addProductionTypeImage.isVisible());
		ComboBox<String> productionTypeComboBox = new ComboBox<String>();
		productionTypeComboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		productionTypeComboBox.setItems(GlobalDetails.getProductionTypes());
		productionTypeComboBox.getSelectionModel().select(productionTypeLabel.getText());
		productionTypeLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		productionTypeLabel.setGraphic(productionTypeComboBox);
	}
	
	@FXML
	private void addProductionType() {
		addProductionTypeImage.setVisible(!addProductionTypeImage.isVisible());
		
	}
	
	private void modificationSaleProduct() {
		
	}
	
	@FXML
	private void saveModificationProductionView() {
		product.setSeasons(seasons_tmp);
		//changeStateModificationProductionView();
	}
	
	@FXML
	private void cancelModificationProductionView() {
		Integer month = 0;
		for (Node node : seasonGridPane.getChildren()) {
			if (month < 12) {
				node.pseudoClassStateChanged(PseudoClass.getPseudoClass(product.getSeason(month)), true);
				changeSeason(node, product.getSeason(month));
				month++;
			}
		}
		product.getSeasons(seasons_tmp);
	}
	
	@FXML
	private void stopModificationProductionView() {
		cancelModificationProductionView();
		//changeStateModificationProductionView();
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
	
}