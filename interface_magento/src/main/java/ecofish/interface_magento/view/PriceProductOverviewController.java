package ecofish.interface_magento.view;


import java.sql.SQLException;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

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
	Button updateButton;
	
	@FXML
	Button saveButton;
	
	@FXML
	public ProgressBar saveProgressBar;
	
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
	
    private final static PseudoClass increasePrice = PseudoClass.getPseudoClass("increase-price");
    private final static PseudoClass decreasePrice = PseudoClass.getPseudoClass("decrease-price");
	
	@FXML
	private void handleUpdatePriceButton() {
		System.out.println(this.newPriceTextField.getText());
		if (this.currentProduct != null && this.newPriceTextField.getText().length() != 0) {
			Double oldPrice = Double.parseDouble(this.actualPriceText.getText());
			Double newPrice = Double.parseDouble(this.newPriceTextField.getText());
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
	private void handleSaveChangesButton() throws SQLException {
		this.saveProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
		ProductService.updateDatabase(this.saveProgressBar);
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
			updateProductTable(this.currentCategory, null);
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
		this.productTable.setItems(ProductService.getActiveProducts(null, null));
		this.productTable.setPlaceholder(new Label("No active products"));
		this.productTable.refresh();
		
		this.productTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				System.out.println("passage");
				showProduct(newValue);
			}
		});
		
		this.productTable.setRowFactory(productTable -> new TableRow<Product>() {
	
		    @Override
		    protected void updateItem(Product product, boolean empty) {
		        super.updateItem(product, empty);
		        cancelHighlight();
		        if (product != null && product.getNewPrice() != null) {
		        	if (product.getNewPrice() > product.getActualPrice()) this.pseudoClassStateChanged(increasePrice, true);
		        	else if (product.getNewPrice() < product.getActualPrice()) this.pseudoClassStateChanged(decreasePrice, true);
		        }
		    }
		    
		    private void cancelHighlight() {
		    	 this.pseudoClassStateChanged(increasePrice, false);
			     this.pseudoClassStateChanged(decreasePrice, false);
		    }
	    
		});
				
		this.categoryComboBox.setItems(CategoryService.getCategory());
		this.familyComboBox.setDisable(true);
		
		this.categoryComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateProductTable(newValue, null);
				updateFamilyComboBox(newValue);
			}
		});
		
		this.familyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateProductTable(currentCategory, newValue);
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
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleUpdatePriceButton();
			if(keyEvent.getCode() == KeyCode.ESCAPE) this.productTable.requestFocus();
		});
		
		
		
		//System.out.println(this.productTable.getSortPolicy());
		
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
		if (this.currentCategory == null) {
			this.familyComboBox.setDisable(true);
			resetFamily();
		}
		else {
			this.familyComboBox.setDisable(false);
			this.familyComboBox.setItems(FamilyService.getFamily(category));
		}
	}
	
	private void updateProductTable(String category, String family) {
		this.currentCategory = category;
		this.currentFamily = family;
		this.productTable.setItems(ProductService.getActiveProducts(this.currentCategory, this.currentFamily));
		this.productTable.getSelectionModel().selectFirst();
		this.productTable.refresh();
		this.productTable.requestFocus();
	}
	
}