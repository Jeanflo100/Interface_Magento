package ecofish.interface_magento.view;



import java.util.Optional;
import java.util.function.UnaryOperator;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.util.NameValueFactory;
import ecofish.interface_magento.util.PriceValueFactory;
import ecofish.interface_magento.util.QualityValueFactory;
import ecofish.interface_magento.util.SizeValueFactory;
import ecofish.interface_magento.service.CategoryService;
import ecofish.interface_magento.service.FamilyService;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

public class ProductOverviewController{
	
	@FXML
	ComboBox<String> categoryComboBox;
	
	@FXML
	ComboBox<String> familyComboBox;
	
	@FXML
	TableView<Product> productTable;

	@FXML
	TableColumn<Product, String> nameColumn;
	
	@FXML
	TableColumn<Product, String> qualityColumn;

	@FXML
	TableColumn<Product, String> sizeColumn;
	
	@FXML
	TableColumn<Product, String> priceColumn;
	
	@FXML
	Text descriptionText;
	
	@FXML
	Text oldPriceText;
	
	@FXML
	TextField newPriceTextField;
	
	@FXML
	Button saveButton;
	
	/*@FXML
	DatePicker birthDatePicker;
	
	@FXML
	ComboBox<Category> categoryComboBox;
	
	@FXML
	ImageView photoImageView;
	
	@FXML
	ImageView changePhotoImageView;
	
	@FXML
	Button validateButton;
	
	@FXML
	ComboBox<Category> filterComboBox;
	
	@FXML
	TextField filterTextField;
	
	@FXML
	Button filterButton;
	
	private final Image defaultPhoto = new Image(InterfaceMagento.class.getResource("image/default-photo.jpg").toString());
	
	private String urlChangePhoto;*/
	
	Product currentProduct;
	
	String currentCategory;
	
	String currentFamily;
	
	/*private boolean newPerson;
	
	@FXML
	private void handleValidateButton() {
		System.out.println("Validate bouton");
	}*/
	
	@FXML
	private void handleSaveButton() {
		System.out.println("Save bouton");
		System.out.println(this.newPriceTextField.getText());
		if (this.currentProduct != null && this.newPriceTextField.getText().length() != 0) {
			Double oldPrice = Double.parseDouble(this.oldPriceText.getText());
			Double newPrice = Double.parseDouble(this.newPriceTextField.getText());
			// nouveau nombre - ancien nombre le tout diviser par ancien nombre < 0.1
			if ((newPrice - oldPrice) / oldPrice > 0.1) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initOwner(StageService.getPrimaryStage());
				alert.setTitle("WARNING");
				alert.setHeaderText("The difference between the old and the new price is more than 10%, continue ?");
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() != ButtonType.OK) {
					return;
		    	}
			}
			this.currentProduct.setPrice(newPrice);
			this.oldPriceText.setText(this.currentProduct.getPrice().toString());
			this.newPriceTextField.setPromptText(this.currentProduct.getPrice().toString());
			this.newPriceTextField.clear();
			this.productTable.refresh();
		}
	}
	
	@FXML
	private void test1() {
		System.out.println("test1");
	}
	
	@FXML
	private void test2() {
		//this.categoryComboBox.getSelectionModel().clearSelection();
		System.out.println("test2");
	}
	
	@FXML
	private void initialize() {
		System.out.println("initialize");
		this.nameColumn.setCellValueFactory(new NameValueFactory());
		this.qualityColumn.setCellValueFactory(new QualityValueFactory());
		this.sizeColumn.setCellValueFactory(new SizeValueFactory());
		this.priceColumn.setCellValueFactory(new PriceValueFactory());
		this.productTable.setPlaceholder(new Label("No category and/or family selected"));
		this.productTable.refresh();
		
		this.productTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				System.out.println("passage");
				showProduct(newValue);
			}
		});
				
		this.categoryComboBox.setItems(CategoryService.getCategory());
		this.familyComboBox.setDisable(true);
		
		this.categoryComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateFamilyComboBox(newValue);
			}
		});
		
		this.familyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateProductTable(newValue);
			}
		});
		
		UnaryOperator<TextFormatter.Change> doubleOnlyFilter = change -> {
			String text = change.getText();
			if (text.isEmpty() || text == null) return change;
			for (int i = 0; i<text.length(); i++) {
				if (!String.valueOf(text.charAt(i)).matches("[.0-9]")) return null;
			}
			return change;
		};
		
		TextFormatter<Double> newPriceDoubleOnlyFormatter = new TextFormatter<Double>(doubleOnlyFilter);
		this.newPriceTextField.setTextFormatter(newPriceDoubleOnlyFormatter);
	}
	
	private void showProduct(Product product) {
		this.currentProduct = product;
		if (this.currentProduct == null) {
			this.descriptionText.setText(null);
			this.oldPriceText.setText(null);
			this.newPriceTextField.setPromptText(null);
		}
		else {
			this.descriptionText.setText(currentProduct.toString());
			this.oldPriceText.setText(currentProduct.getPrice().toString());
			this.newPriceTextField.setPromptText(currentProduct.getPrice().toString());
		}
		
	}
	
	private void updateFamilyComboBox(String category) {
		System.out.println("update familyComboBox");
		//this.familyComboBox.show();
		this.currentCategory = category;
		this.familyComboBox.setDisable(false);
		this.familyComboBox.setItems(FamilyService.getFamily(category));
	}
	
	private void updateProductTable(String family) {
		System.out.println("update productTable");
		this.currentFamily = family;
		this.productTable.setItems(ProductService.getProducts(this.currentCategory, this.currentFamily));
		this.productTable.refresh();
	}
	
}