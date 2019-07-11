package ecofish.interface_magento.view;

import javafx.css.PseudoClass;

public interface DetailsProductInterface {
	
	public final PseudoClass unmodifiable = PseudoClass.getPseudoClass("unmodifiable");
	
	public void modificationDetails(Boolean isModification, Boolean isSave);
	
}