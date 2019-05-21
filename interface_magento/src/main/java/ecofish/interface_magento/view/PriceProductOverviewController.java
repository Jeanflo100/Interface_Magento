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
	Text successfulBackupText;
	
	private Integer numberVisibleRow;
	
	private Product currentProduct;
	
	private String currentCategory;
	
	private String currentFamily;
	
    private final static PseudoClass increasePrice = PseudoClass.getPseudoClass("increase-price");
    private final static PseudoClass decreasePrice = PseudoClass.getPseudoClass("decrease-price");
	
	private static Integer getNumberVisibleRow(TableView<?> table) {
		Integer numberColumnRow = 0;
		Integer newNumberColumnRow = numberColumnRow;
		for (TableColumn<?, ?> column : table.getColumns()) {
			Integer temp_newNumberColumnRow = getNumberColumnRow(column, numberColumnRow);
			if (temp_newNumberColumnRow > newNumberColumnRow) {
				newNumberColumnRow = temp_newNumberColumnRow;
			}
		}
		numberColumnRow = newNumberColumnRow;
		Integer numberVisibleRow = (int) ((table.getPrefHeight() - numberColumnRow * 20) / table.getFixedCellSize());
		return numberVisibleRow;
	}
	
	private static Integer getNumberColumnRow(TableColumn<?, ?> column, Integer actualNumberColumnRow) {
		Integer newNumberColumnRow = actualNumberColumnRow;
		for (TableColumn<?, ?> subColumn : column.getColumns()) {
			Integer temp_newNumberColumnRow = getNumberColumnRow(subColumn, actualNumberColumnRow);
			if (temp_newNumberColumnRow > newNumberColumnRow) {
				newNumberColumnRow = temp_newNumberColumnRow;
			}
		}
		return newNumberColumnRow + 1;
	}
    
	@FXML
	private void handleUpdatePriceButton() {
		if (this.currentProduct != null && this.newPriceTextField.getText().length() != 0) {
			Double actualPrice = this.currentProduct.getActualPrice();
			Double newPrice = Double.parseDouble(this.newPriceTextField.getText());
			if (Math.abs(newPrice - actualPrice) / actualPrice > 0.1) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initOwner(StageService.getPrimaryStage());
				alert.setTitle("WARNING");
				alert.setHeaderText("The difference between the actual and the new price is more than 10%, continue ?");
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get() != ButtonType.OK) {
					return;
		    	}
			}
			updateNewPrice(newPrice);
			this.newPriceTextField.clear();	
		}
		this.productTable.requestFocus();
		selectNextProduct();
	}
	
	@FXML
	private void handleSaveChangesButton() throws SQLException {
		ProductService.updateProduct();
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
		this.numberVisibleRow = getNumberVisibleRow(this.productTable);
		this.productTable.setItems(ProductService.getActiveProducts(null, null));
		this.productTable.refresh();
		this.productTable.getSortOrder().add(this.nameColumn);
		this.productTable.getSortOrder().add(this.sizeColumn);
		this.productTable.getSortOrder().add(this.qualityColumn);
		sortProductTable();
		
		this.productTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
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
		
		TextFormatterDouble textFormatter = new TextFormatterDouble();
		this.newPriceTextField.setTextFormatter(textFormatter.getTextFormatterDouble());

		this.productTable.setOnKeyTyped(keyEvent -> {
			if (!keyEvent.getCharacter().equals("\r") && !keyEvent.getCharacter().equals(" ") && !keyEvent.getCharacter().equals("") && !keyEvent.getCharacter().equals("")) {
				this.newPriceTextField.requestFocus();
				this.newPriceTextField.clear();
				this.newPriceTextField.appendText(keyEvent.getCharacter());
			}
		});
		
		this.productTable.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) {
				this.newPriceTextField.requestFocus();
			}
			else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
				updateNewPrice(null);
				selectNextProduct();
			}
		});
		
		this.newPriceTextField.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleUpdatePriceButton();
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
		sortProductTable();
		this.productTable.refresh();
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
	
	private void selectNextProduct() {
		this.productTable.getSelectionModel().selectNext();
		if(this.numberVisibleRow/2 - this.productTable.getSelectionModel().getSelectedIndex() <= 0) {
			this.productTable.scrollTo(this.productTable.getSelectionModel().getSelectedIndex() - this.numberVisibleRow/2);
		}
		else {
			this.productTable.scrollTo(0);
		}
	}
	
	private void updateNewPrice(Double newPrice) {
		if (this.currentProduct.getActualPrice().equals(newPrice)) newPrice = null;
		this.currentProduct.setNewPrice(newPrice);
		if (this.currentProduct.getNewPrice() != null) this.newPriceTextField.setPromptText(this.currentProduct.getNewPrice().toString());
		else this.newPriceTextField.setPromptText(this.currentProduct.getActualPrice().toString());
		this.productTable.refresh();
	}
	
}