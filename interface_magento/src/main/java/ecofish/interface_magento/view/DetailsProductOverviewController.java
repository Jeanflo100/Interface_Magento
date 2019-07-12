package ecofish.interface_magento.view;

import java.net.MalformedURLException;

import ecofish.interface_magento.model.DetailedProduct;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DetailsProductOverviewController {
	
	////////////////// General Components //////////////////
	
	@FXML
	Label skuLabel;
	
	@FXML
	Label characteristicLabel;
	
	@FXML
	ImageView modificationDetailsImage;
	
	@FXML
	ImageView validationDetailsImage;
	
	@FXML
	ImageView cancelDetailsImage;
	
	@FXML
	Button saveAndQuitButton;
	
	@FXML
	TabPane detailsProductTabPane;
	
	////////////////// Administrative Components //////////////////
	
	@FXML
	AnchorPane administrativeModificationAnchorPane;
	
	@FXML
	TextField eanCodeTextField;
	
	@FXML
	TextField ecSalesCodeTextField;
	
	@FXML
	VBox alergenVBox;
	
	@FXML
	VBox brandVBox;
	
	@FXML
	VBox labelVBox;
	
	////////////////// Description Components //////////////////
	
	@FXML
	AnchorPane descriptionModificationAnchorPane;
	
	@FXML
	TextArea shortDescriptionTextArea;
	
	@FXML
	ImageView imageImage;
	
	@FXML
	TextArea descriptionTextArea;
	
	@FXML
	TextField latinNameTextField;
	
	////////////////// Production Components //////////////////
	
	@FXML
	AnchorPane productionModificationAnchorPane;
	
	@FXML
	TextField productionTypeTextField;
	
	@FXML
	CheckBox statusCheckBox;
	
	@FXML
	AnchorPane seasonAnchorPane;
	
	@FXML
	Label actualSeasonLabel;
	
	@FXML
	GridPane seasonGridPane;
	
	@FXML
	VBox countryOfManufactureVBox;
	
	////////////////// Sale Components //////////////////
	
	@FXML
	TextField basicPackTextField;
	
	@FXML
	TextField actualPriceTextField;
	
	@FXML
	LineChart<String, Double> priceLineChart;
	
	@FXML
	TextField pack2TextField;
	
	@FXML
	TextField pricePack2TextField;
	
	@FXML
	TextField pack3TextField;
	
	@FXML
	TextField pricePack3TextField;
	
	@FXML
	TextField pack4TextField;
	
	@FXML
	TextField pricePack4TextField;
	
	////////////////// Variables //////////////////
	
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
		administrativeDetails = new DetailsProductAdministrative(detailedProduct, administrativeModificationAnchorPane, eanCodeTextField, ecSalesCodeTextField, alergenVBox, brandVBox, labelVBox);
		descriptionDetails = new DetailsProductDescription(detailedProduct, descriptionModificationAnchorPane, shortDescriptionTextArea, imageImage, descriptionTextArea, latinNameTextField);
		productionDetails = new DetailsProductProduction(detailedProduct, productionModificationAnchorPane, productionTypeTextField, statusCheckBox, seasonAnchorPane, actualSeasonLabel, seasonGridPane, countryOfManufactureVBox);
		saleDetails = new DetailsProductSale(detailedProduct, basicPackTextField, actualPriceTextField, priceLineChart, pack2TextField, pricePack2TextField, pack3TextField, pricePack3TextField, pack4TextField, pricePack4TextField);
		isModified = false;
		
		initTitleView();
		initActualView();
	}
	
	private void initTitleView() {
		this.skuLabel.setText("Product: " + detailedProduct.getIdProduct().toString());
		this.characteristicLabel.setText(
				detailedProduct.getCategory()
				+ (detailedProduct.getFamily() != null ? " - " + detailedProduct.getFamily() : "")
				+ " - " + detailedProduct.getName()
				+ (detailedProduct.getSize() != null ? " - " + detailedProduct.getSize() : "")
				+ (detailedProduct.getQuality() != null ? " - " + detailedProduct.getQuality() : "")
				);
	}
	
	private void initActualView() {
		if (detailsProductTabPane.getSelectionModel().getSelectedItem().getText().equals(section.Administrative.name())) actualView = this.administrativeDetails;
		else if (detailsProductTabPane.getSelectionModel().getSelectedItem().getText().equals(section.Description.name())) actualView = this.descriptionDetails;
		else if (detailsProductTabPane.getSelectionModel().getSelectedItem().getText().equals(section.Production.name())) actualView = this.productionDetails;
		else if (detailsProductTabPane.getSelectionModel().getSelectedItem().getText().equals(section.Sale.name())) actualView = this.saleDetails;
		
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
	
	@FXML
	private void changeImage() throws MalformedURLException {
		this.descriptionDetails.changeImage();
	}
	
	@FXML
	private void changeChoices(MouseEvent click) {
		System.out.println(click.getPickResult().getIntersectedNode().getId());
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