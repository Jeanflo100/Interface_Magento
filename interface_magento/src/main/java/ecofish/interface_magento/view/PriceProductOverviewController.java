package ecofish.interface_magento.view;


import java.sql.SQLException;
import java.util.Optional;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.CategoryService;
import ecofish.interface_magento.service.FamilyService;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.util.TextFormatterDouble;
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
	TableColumn<Product, String> sizeColumn;
	
	@FXML
	TableColumn<Product, String> qualityColumn;
	
	@FXML
	TableColumn<Product, Double> actualPriceColumn;
	
	@FXML
	TableColumn<Product, Double> newPriceColumn;

	
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
	
	private Integer numberVisibleRow;
	
	private Product currentProduct;
	
	private String currentCategory;
	
	private String currentFamily;
	
    private final static PseudoClass increasePrice = PseudoClass.getPseudoClass("increase-price");
    private final static PseudoClass decreasePrice = PseudoClass.getPseudoClass("decrease-price");
	
	@FXML
	private void handleUpdatePriceButton() {
		System.out.println(this.newPriceTextField.getText());
		if (this.currentProduct != null && this.newPriceTextField.getText().length() != 0) {
			Double actualPrice = this.currentProduct.getActualPrice();
			Double newPrice = Double.parseDouble(this.newPriceTextField.getText());
			if (Math.abs(newPrice - actualPrice) / actualPrice > 0.1) {
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
		if(this.numberVisibleRow/2 - this.productTable.getSelectionModel().getSelectedIndex() <= 0) {
			this.productTable.scrollTo(this.productTable.getSelectionModel().getSelectedIndex() - this.numberVisibleRow/2);
		}
		else {
			this.productTable.scrollTo(0);
		}
	}
	
	@FXML
	private void handleSaveChangesButton() throws SQLException {
		this.saveProgressBar.setVisible(true);
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
		this.productTable.setPlaceholder(new Label("No active products"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.actualPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("actualPrice"));
		this.newPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("newPrice"));
		this.productTable.setFixedCellSize(25);
		setNumberVisibleRow();
		this.productTable.setItems(ProductService.getActiveProducts(null, null));
		this.productTable.refresh();
		this.productTable.getSortOrder().add(this.nameColumn);
		this.productTable.getSortOrder().add(this.sizeColumn);
		this.productTable.getSortOrder().add(this.qualityColumn);
		sortProductTable();
		
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
		
		this.productTable.getSelectionModel().selectFirst();
				
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
		
		this.newPriceTextField.setTextFormatter(TextFormatterDouble.getTextFormatterDouble());

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
		
	}
	
	private void setNumberVisibleRow() {
		Integer numberColumnRow = 0;
		Integer newNumberColumnRow = numberColumnRow;
		for (TableColumn<Product, ?> column : this.productTable.getColumns()) {
			Integer temp_newNumberColumnRow = getNumberColumnRow(column, numberColumnRow);
			if (temp_newNumberColumnRow > newNumberColumnRow) {
				newNumberColumnRow = temp_newNumberColumnRow;
			}
		}
		numberColumnRow = newNumberColumnRow;
		Integer numberVisibleRow = (int) ((this.productTable.getPrefHeight() - numberColumnRow * 20) / this.productTable.getFixedCellSize());
		this.numberVisibleRow = numberVisibleRow;
	}
	
	private Integer getNumberColumnRow(TableColumn<?, ?> column, Integer actualNumberColumnRow) {
		Integer newNumberColumnRow = actualNumberColumnRow;
		for (TableColumn<?, ?> subColumn : column.getColumns()) {
			Integer temp_newNumberColumnRow = getNumberColumnRow(subColumn, actualNumberColumnRow);
			if (temp_newNumberColumnRow > newNumberColumnRow) {
				newNumberColumnRow = temp_newNumberColumnRow;
			}
		}
		return newNumberColumnRow + 1;
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
		this.productTable.refresh();
		sortProductTable();
		this.productTable.getSelectionModel().selectFirst();
		this.productTable.requestFocus();
		this.productTable.scrollTo(currentProduct);
	}
	
	private void sortProductTable() {
		this.nameColumn.setSortable(true);
		this.sizeColumn.setSortable(true);
		this.qualityColumn.setSortable(true);
		this.qualityColumn.setSortable(false);
		this.sizeColumn.setSortable(false);
		this.nameColumn.setSortable(false);
	}
	
}