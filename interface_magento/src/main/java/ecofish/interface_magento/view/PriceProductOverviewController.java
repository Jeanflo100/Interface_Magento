package ecofish.interface_magento.view;



import java.util.Optional;
import java.util.function.UnaryOperator;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.CategoryService;
import ecofish.interface_magento.service.FamilyService;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

public class PriceProductOverviewController{
	
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
	TableColumn<Product, String> newPriceColumn;
	
	@FXML
	TableColumn<Product, String> actualPriceColumn;
	
	@FXML
	Text descriptionText;
	
	@FXML
	Text actualPriceText;
	
	@FXML
	TextField newPriceTextField;
	
	@FXML
	Button saveButton;
	
	@FXML
	Button testButton;
	
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
	
	private Product currentProduct;
	
	private String currentCategory;
	
	private String currentFamily;
	
	/*final PseudoClass highlightMessage = PseudoClass.getPseudoClass("highlight-message");
	
	private final PseudoClass increasePrice = PseudoClass.getPseudoClass("increase-price");
	private final PseudoClass decreasePrice = PseudoClass.getPseudoClass("decrease-price");*/
	
	@FXML
	private void handleSaveButton() {
		System.out.println(this.newPriceTextField.getText());
		if (this.currentProduct != null && this.newPriceTextField.getText().length() != 0) {
			Double oldPrice = Double.parseDouble(this.actualPriceText.getText());
			Double newPrice = Double.parseDouble(this.newPriceTextField.getText());
			// nouveau nombre - ancien nombre le tout diviser par ancien nombre < 0.1
			if (Math.abs(newPrice - oldPrice) / oldPrice > 0.1) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initOwner(StageService.getPrimaryStage());
				alert.setTitle("WARNING");
				alert.setHeaderText("The difference between the old and the new price is more than 10%, continue ?");
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() != ButtonType.OK) {
					return;
		    	}
			}
			this.currentProduct.setNewPrice(newPrice);
			this.newPriceTextField.setPromptText(this.currentProduct.getNewPrice().toString());
			this.newPriceTextField.clear();
			this.productTable.refresh();
		}
		this.productTable.requestFocus();
		this.productTable.getSelectionModel().selectNext();
	}
	
	@FXML
	private void resetCategory() {
		if (this.currentCategory != null) {
			this.categoryComboBox.getSelectionModel().clearSelection();
			updateFamilyComboBox(null);
		}
	}
	
	@FXML
	private void resetFamily() {
		if (this.currentFamily != null) {
			this.familyComboBox.getSelectionModel().clearSelection();
			updateProductTable(null);
		}
	}
		
	@FXML
	private void initialize() {
		System.out.println("initialize");
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.qualityColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.sizeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.actualPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("actualPrice"));
		this.newPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("newPrice"));
		this.productTable.setPlaceholder(new Label("No active products"));
		this.productTable.refresh();
		

		/*this.productTable.setRowFactory((final TableView<Product> p) -> new ProductTableRow());
		
		final PseudoClass highlightMessage = PseudoClass.getPseudoClass("highlight-message");

		this.productTable.setRowFactory(productTable -> new TableRow<Product>());
		
		this.productTable.setRowFactory(productTable -> new TableRow<Product>() {

		    {
		        selectedProperty().addListener((o, oldVal, newVal) -> {
		        	if (newVal) {
		                Product item = getItem();
		                if (item != null) {
		                	System.out.println("Passage Row");
		                	pseudoClassStateChanged(increasePrice, true);
		                }
		            }
		        });
		    }

		    @Override
		    protected void updateItem(Product item, boolean empty) {
		        super.updateItem(item, empty);
		        System.out.println("highlight");
		        System.out.println(item);
		        pseudoClassStateChanged(decreasePrice, item != null);
		    }
		    
		});*/
		
		
		
		
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

		this.productTable.setOnKeyTyped(keyEvent -> {
			if (!keyEvent.getCharacter().equals("\r") && !keyEvent.getCharacter().equals(" ")) {
				this.newPriceTextField.requestFocus();
				this.newPriceTextField.clear();
				this.newPriceTextField.appendText(keyEvent.getCharacter());
			}
		});
		
		this.productTable.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) {
				this.newPriceTextField.requestFocus();
			}
		});
		
		this.newPriceTextField.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleSaveButton();
			if(keyEvent.getCode() == KeyCode.ESCAPE) this.productTable.requestFocus();
		});
		
	}
	
	private void showProduct(Product product) {
		this.currentProduct = product;
		if (this.currentProduct == null) {
			this.descriptionText.setText(null);
			this.actualPriceText.setText(null);
			this.newPriceTextField.setPromptText(null);
		}
		else {
			this.descriptionText.setText(this.currentProduct.toString());
			this.actualPriceText.setText(this.currentProduct.getActualPrice().toString());
			if (this.currentProduct.getNewPrice() == null) {
				this.newPriceTextField.setPromptText(this.currentProduct.getActualPrice().toString());
			}
			else {
				this.newPriceTextField.setPromptText(this.currentProduct.getNewPrice().toString());
			}
		}
		
	}
	
	private void updateFamilyComboBox(String category) {
		//this.familyComboBox.show();
		this.currentCategory = category;
		if (this.currentCategory == null) {
			this.familyComboBox.setDisable(true);
			resetFamily();
		}
		else {
			this.familyComboBox.setDisable(false);
			this.familyComboBox.setItems(FamilyService.getFamily(category));
		}
	}
	
	private void updateProductTable(String family) {
		this.currentFamily = family;
		this.productTable.setItems(ProductService.getProducts(this.currentCategory, this.currentFamily));
		this.productTable.getSelectionModel().selectFirst();
		this.productTable.refresh();
		this.productTable.requestFocus();
	}
	
}