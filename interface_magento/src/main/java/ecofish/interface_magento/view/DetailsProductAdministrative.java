package ecofish.interface_magento.view;

import ecofish.interface_magento.model.DetailedProduct;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DetailsProductAdministrative implements DetailsProductInterface {

	private final DetailedProduct detailedProduct;
	
	private final AnchorPane modificationAnchorPane;
	private final TextField eanCodeTextField;
	private final TextField ecSalesCodeTextField;
	private final VBox alergenVBox;
	private final VBox brandVBox;
	private final VBox labelVBox;

	public DetailsProductAdministrative(DetailedProduct detailedProduct, AnchorPane modificationAnchorPane, TextField eanCodeTextField, TextField ecSalesCodeTextField, VBox alergenVBox, VBox brandVBox, VBox labelVBox) {
		this.detailedProduct = detailedProduct;
		this.modificationAnchorPane = modificationAnchorPane;
		this.eanCodeTextField = eanCodeTextField;
		this.ecSalesCodeTextField = ecSalesCodeTextField;
		this.alergenVBox = alergenVBox;
		this.brandVBox = brandVBox;
		this.labelVBox = labelVBox;
		
		initContentComponents();
		initStateComponents();
	}
	
	private void initContentComponents() {
		//alergenVBox.getChildren().addAll(null);
	}
	
	private void initStateComponents() {
		
	}

	@Override
	public void modificationDetails(Boolean isModification, Boolean isSave) {
	
	}
	
}