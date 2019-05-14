package ecofish.interface_magento.util;

import ecofish.interface_magento.model.Product;

import javafx.css.PseudoClass;
import javafx.scene.control.TableRow;


public final class ProductTableRow extends TableRow<Product> {

    
    private final PseudoClass increasePrice = PseudoClass.getPseudoClass("increase-price");
    private final PseudoClass decreasePrice = PseudoClass.getPseudoClass("decrease-price");

    @Override
    protected void updateItem(final Product product, final boolean empty) {
        super.updateItem(product, empty);
        System.out.println("passage Row");
        System.out.println(this);
        System.out.println(product);
        System.out.println(empty);
        if (product != null && product.getNewPrice() != null) {
        	if (product.getNewPrice() > product.getActualPrice()) this.pseudoClassStateChanged(increasePrice, true);
        	else if (product.getNewPrice() < product.getActualPrice()) this.pseudoClassStateChanged(decreasePrice, true);
        	else {this.pseudoClassStateChanged(increasePrice, false); this.pseudoClassStateChanged(decreasePrice, false);}
        }
        else {this.pseudoClassStateChanged(increasePrice, false); this.pseudoClassStateChanged(decreasePrice, false);}
    }

}