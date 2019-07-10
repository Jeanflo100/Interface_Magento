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
	
	private DetailsProductAdministrative administrativeDetails;
	
	private DetailsProductDescription descriptionDetails;
	
	private DetailsProductProduction productionDetails;
	
	private DetailsProductSale saleDetails;
	
	private Boolean isModified;
	
	@FXML
	private void initialize() {
		detailedProduct = DetailedProduct.getProduct();
		productionDetails = new DetailsProductProduction(detailedProduct, seasonGridPane, currentSeasonLabel);
		administrativeDetails = new DetailsProductAdministrative();
		descriptionDetails = new DetailsProductDescription();
		saleDetails = new DetailsProductSale();
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
			if (newValue.getText().equals(section.Administrative.name())) actualView = this.administrativeDetails;
			else if (newValue.getText().equals(section.Description.name())) actualView = this.descriptionDetails;
			else if (newValue.getText().equals(section.Production.name())) actualView = this.productionDetails;
			else if (newValue.getText().equals(section.Sale.name())) actualView = this.saleDetails;
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
		if (this.isModified) actualView.modificationDetails(true, null);
	}
	
	@FXML
	private void validationDetails() {
		actualView.modificationDetails(false, true);
		modificationDetails();
	}
	
	@FXML
	private void cancelDetails() {
		actualView.modificationDetails(false, false);
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