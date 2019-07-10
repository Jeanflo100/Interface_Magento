package ecofish.interface_magento.view;

import ecofish.interface_magento.model.DetailedProduct;
import javafx.fxml.FXML;
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
	ImageView modificationDetailsImage;
	
	@FXML
	ImageView validationDetailsImage;
	
	@FXML
	ImageView cancelDetailsImage;
	
	@FXML
	TabPane detailsProductTabPane;
	
	@FXML
	AnchorPane administrativeModificationAnchorPane;

	@FXML
	AnchorPane descriptionModificationAnchorPane;

	@FXML
	AnchorPane productionModificationAnchorPane;

	@FXML
	AnchorPane saleModificationAnchorPane;
	
	@FXML
	GridPane seasonGridPane;
	
	@FXML
	Label currentSeasonLabel;
	
	private DetailedProduct detailedProduct;
	
	private DetailsProductInterface actualView;
	
	private enum section{Administrative, Description, Production, Sale;};
	
	private ProductionDetailsProduct productionDetails;
	
	private Boolean isModified;
	
	@FXML
	private void initialize() {
		detailedProduct = DetailedProduct.getProduct();
		productionDetails = new ProductionDetailsProduct(detailedProduct, seasonGridPane, currentSeasonLabel);
		isModified = false;
		
		initTitleView();
		initListenerTab();
	}
	
	private void initTitleView() {
		this.skuLabel.setText("Product: " + detailedProduct.getIdProduct().toString());
		this.descriptionLabel.setText(
				detailedProduct.getCategory()
				+ (detailedProduct.getFamily() != null ? " - " + detailedProduct.getFamily() : "")
				+ " - " + detailedProduct.getName()
				+ (detailedProduct.getSize() != null ? " - " + detailedProduct.getSize() : "")
				+ (detailedProduct.getQuality() != null ? " - " + detailedProduct.getQuality() : "")
				);
	}
	
	private void initListenerTab() {
		this.detailsProductTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.getText().equals(section.Administrative.name())) actualView = null;
			else if (newValue.getText().equals(section.Description.name())) actualView = null;
			else if (newValue.getText().equals(section.Production.name())) actualView = this.productionDetails;
			else if (newValue.getText().equals(section.Sale.name())) actualView = null;
		});
	}
	
	@FXML
	private void modificationDetails() {		
		this.modificationDetailsImage.setVisible(!this.modificationDetailsImage.isVisible());
		this.validationDetailsImage.setVisible(!this.validationDetailsImage.isVisible());
		this.cancelDetailsImage.setVisible(!this.cancelDetailsImage.isVisible());
		
		Tab selectedTab = this.detailsProductTabPane.getSelectionModel().getSelectedItem();
		for (Tab tab : this.detailsProductTabPane.getTabs()) {
			if (tab != selectedTab) {
				tab.setDisable(!tab.isDisable());
			}
		}

		this.isModified = !this.isModified;
		if (actualView != null && this.isModified) actualView.modificationDetails(true, null);
	}
	
	@FXML
	private void validationDetails() {
		if (actualView != null) actualView.modificationDetails(false, true);
		modificationDetails();
	}
	
	@FXML
	private void cancelDetails() {
		if (actualView != null) actualView.modificationDetails(false, false);
		modificationDetails();
	}
	
	/*addProductionTypeImage.setVisible(!addProductionTypeImage.isVisible());
	ComboBox<String> productionTypeComboBox = new ComboBox<String>();
	productionTypeComboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	productionTypeComboBox.setItems(GlobalDetails.getProductionTypes());
	productionTypeComboBox.getSelectionModel().select(productionTypeLabel.getText());
	productionTypeLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	productionTypeLabel.setGraphic(productionTypeComboBox);*/
	
	@FXML
	private void selectSeason(MouseEvent click) {
		productionDetails.selectSeason(click);
	}
	
}