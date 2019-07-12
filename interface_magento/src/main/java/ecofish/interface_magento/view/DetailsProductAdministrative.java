package ecofish.interface_magento.view;

import ecofish.interface_magento.model.DetailedProduct;

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
		this.eanCodeTextField.setText(detailedProduct.getEanCode());
		this.ecSalesCodeTextField.setText(detailedProduct.getEcSalesCode());
		//alergenVBox.getChildren().addAll(null);
	}

	private void saveModification() {
		this.detailedProduct.setEanCode(this.eanCodeTextField.getText());
		this.detailedProduct.setEcSalesCode(this.ecSalesCodeTextField.getText());
	}
	
	@Override
	public void modificationDetails(Boolean isModification, Boolean isSave) {
		this.administrativeModificationAnchorPane.setVisible(isModification);
		this.eanCodeTextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.ecSalesCodeTextField.pseudoClassStateChanged(unmodifiable, !isModification);	
		this.eanCodeTextField.setEditable(isModification);
		this.ecSalesCodeTextField.setEditable(isModification);
		if (isSave != null) {
			if (isSave) saveModification();
			else if (!isSave) setContentComponents();
		}
	}
	
}