package ecofish.interface_magento.view;

import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.service.GlobalDetails;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DetailsProductAdministrative implements DetailsProductInterface {

	private final DetailedProduct detailedProduct;
	
	private final AnchorPane administrativeModificationAnchorPane;
	private final TextField eanCodeTextField;
	private final TextField ecSalesCodeTextField;
	private final VBox alergenVBox;
	private final VBox brandVBox;
	private final VBox labelVBox;

	public DetailsProductAdministrative(DetailedProduct detailedProduct, AnchorPane administrativeModificationAnchorPane, TextField eanCodeTextField, TextField ecSalesCodeTextField, VBox alergenVBox, VBox brandVBox, VBox labelVBox) {
		this.detailedProduct = detailedProduct;
		this.administrativeModificationAnchorPane = administrativeModificationAnchorPane;
		this.eanCodeTextField = eanCodeTextField;
		this.ecSalesCodeTextField = ecSalesCodeTextField;
		this.alergenVBox = alergenVBox;
		this.brandVBox = brandVBox;
		this.labelVBox = labelVBox;
		
		modificationDetails(false, false);
	}
	
	private void setContentComponents() {
		this.detailedProduct.setNewEanCode(null);
		this.detailedProduct.setNewEcSalesCode(null);
		this.detailedProduct.setNewAlergens(null);
		this.detailedProduct.setNewBrands(null);
		this.detailedProduct.setNewLabels(null);
		
		this.eanCodeTextField.setText(detailedProduct.getEanCode());
		this.ecSalesCodeTextField.setText(detailedProduct.getEcSalesCode());
		GlobalDetails.setSelectedAlergens(detailedProduct.getAlergens());
		GlobalDetails.setSelectedBrands(detailedProduct.getBrands());
		GlobalDetails.setSelectedLabels(detailedProduct.getLabels());
		resetLists();
	}
	
	private void resetLists() {
		this.alergenVBox.getChildren().clear();
		this.brandVBox.getChildren().clear();
		this.labelVBox.getChildren().clear();
		this.alergenVBox.getChildren().addAll(GlobalDetails.getAlergens());
		this.brandVBox.getChildren().addAll(GlobalDetails.getBrands());
		this.labelVBox.getChildren().addAll(GlobalDetails.getLabels());
	}

	private void saveModification() {
		this.detailedProduct.setNewEanCode(this.eanCodeTextField.getText());
		this.detailedProduct.setNewEcSalesCode(this.ecSalesCodeTextField.getText());
		this.detailedProduct.setNewAlergens(GlobalDetails.getSelectedAlergens());
		this.detailedProduct.setNewBrands(GlobalDetails.getSelectedBrands());
		this.detailedProduct.setNewLabels(GlobalDetails.getSelectedLabels());
	}
	
	@Override
	public void modificationDetails(Boolean isModification, Boolean isSave) {
		this.administrativeModificationAnchorPane.setVisible(isModification);
		this.eanCodeTextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.ecSalesCodeTextField.pseudoClassStateChanged(unmodifiable, !isModification);	
		this.eanCodeTextField.setEditable(isModification);
		this.ecSalesCodeTextField.setEditable(isModification);
		this.alergenVBox.setMouseTransparent(!isModification);
		this.brandVBox.setMouseTransparent(!isModification);
		this.labelVBox.setMouseTransparent(!isModification);
		if (isSave != null) {
			if (isSave) {
				saveModification();
			}
			else if (!isSave) {
				setContentComponents();
			}
		}
		GlobalDetails.onlySelectedAlergens(!isModification);
		GlobalDetails.onlySelectedBrands(!isModification);
		GlobalDetails.onlySelectedLabels(!isModification);
		resetLists();
	}
	
	protected void changeChoicesAlergen() {
		ModificationChoicesDetailsProductController.setCharacteristic("Alergen");
		ModificationChoicesDetailsProductController.setChoices(GlobalDetails.getAlergensSource());
		StageService.showView(Views.viewsSecondaryStage.ModificationChoicesDetailsProduct, true);
		this.alergenVBox.getChildren().clear();
		this.alergenVBox.getChildren().addAll(GlobalDetails.getBrands());
	}
	
	protected void changeChoicesBrand() {
		ModificationChoicesDetailsProductController.setCharacteristic("Brand");
		ModificationChoicesDetailsProductController.setChoices(GlobalDetails.getBrandsSource());
		StageService.showView(Views.viewsSecondaryStage.ModificationChoicesDetailsProduct, true);
		this.brandVBox.getChildren().clear();
		this.brandVBox.getChildren().addAll(GlobalDetails.getBrands());
	}
	
	protected void changeChoicesLabel() {
		ModificationChoicesDetailsProductController.setCharacteristic("Label");
		ModificationChoicesDetailsProductController.setChoices(GlobalDetails.getLabelsSource());
		StageService.showView(Views.viewsSecondaryStage.ModificationChoicesDetailsProduct, true);
		this.labelVBox.getChildren().clear();
		this.labelVBox.getChildren().addAll(GlobalDetails.getLabels());
	}
	
}