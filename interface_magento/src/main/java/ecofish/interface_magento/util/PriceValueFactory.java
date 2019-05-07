package ecofish.interface_magento.util;

import ecofish.interface_magento.model.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class PriceValueFactory
		implements Callback<TableColumn.CellDataFeatures<Product, String>, ObservableValue<String>> {

	@Override
	public ObservableValue<String> call(CellDataFeatures<Product, String> cellData) {
		return new SimpleStringProperty(cellData.getValue().getPrice().toString());
	}
}