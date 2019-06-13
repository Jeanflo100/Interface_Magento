package ecofish.interface_magento.view;


import java.util.Optional;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.FilterService;
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

/**
 * View controller asoociated to the change in product price
 * @author Jean-Florian Tassart
 */
public class PriceProductOverviewController {
	
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
	
	private FilterService filterService;
	
	private String currentCategory;
	
	private String currentFamily;
	
	private Boolean newCategorySelected;
	
    private final static PseudoClass increasePrice = PseudoClass.getPseudoClass("increase-price");
    private final static PseudoClass decreasePrice = PseudoClass.getPseudoClass("decrease-price");
	
    /**
     * Returns the number of rows of the table visible on the screen
     * @param table - table concerned
     * @return The number of visible rows
     */
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
	
	/**
	 * Recursive function returning the number of column rows
	 * @param column - column currently concerned
	 * @param actualNumberColumnRow - current number of subcolumn rows
	 * @return Total number of subcolumn rows
	 */
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
    
	/**
	 * Checking conditions before updating the price
	 */
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
	
	/**
	 * Update products in the database
	 */
	@FXML
	private void handleSaveChangesButton() {
		ProductService.updateProduct();
	}
	
	/**
	 * Reset the filter by category
	 */
	@FXML
	private void resetCategory() {
		if (this.currentCategory != null) {
			this.familyComboBox.setDisable(true);
			this.categoryComboBox.getSelectionModel().clearSelection();
			showProductTable();
		}
	}
	
	/**
	 * Reset the filter by family
	 */
	@FXML
	private void resetFamily() {
		if (this.currentFamily != null) {
			this.familyComboBox.getSelectionModel().clearSelection();
			showProductTable();
		}
	}
	
	/**
	 * Display the list of family
	 */
	@FXML
	private void showFamily() {
		if (this.currentCategory != null) {
			if (this.newCategorySelected == true) {
				this.newCategorySelected = false;
				this.familyComboBox.setItems(this.filterService.getFamilies(this.currentCategory));
				this.familyComboBox.setDisable(false);
			}
			this.familyComboBox.requestFocus();
			this.familyComboBox.show();
		}
	}
	
	/**
	 * Select the product table
	 */
	@FXML
	private void showProductTable() {
		if (!this.productTable.getItems().isEmpty()) this.productTable.requestFocus();
		this.productTable.getSelectionModel().selectFirst();
		this.productTable.scrollTo(this.currentProduct);
	}
	
	/**
	 * Initialization of the view
	 */
	@FXML
	private void initialize() {
		initTable();
		initItemSelection();
		initKeyPresses();
		setComponents();
	}
	
	/**
	 * Set up table features
	 */
	private void initTable() {
		this.productTable.setPlaceholder(new Label("No active products"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.actualPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("actualPrice"));
		this.newPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("newPrice"));
		this.productTable.setFixedCellSize(25);
		this.numberVisibleRow = getNumberVisibleRow(this.productTable);
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
	}
	
	/**
	 * Adding action on the item element
	 */
	private void initItemSelection() {
		this.productTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				showProduct(newValue);
			}
		});
		
		this.categoryComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!categoryComboBox.isShowing() && categoryComboBox.getSelectionModel().getSelectedItem() != null) {
					categoryComboBox.show();
				}
				newCategorySelected = true;
				updateProductTable(newValue, null);
			}
		});
		
		this.familyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!familyComboBox.isShowing() && familyComboBox.getSelectionModel().getSelectedItem() != null) {
					familyComboBox.show();
				}
				updateProductTable(currentCategory, newValue);
			}
		});
	}
	
	/**
	 * Added faster navigation using the keys
	 */
	private void initKeyPresses() {
		this.productTable.setOnKeyTyped(keyEvent -> {
			if(keyEvent.getCharacter().matches("[.0-9]")) {
				this.newPriceTextField.requestFocus();
				this.newPriceTextField.clear();
				if (keyEvent.getCharacter().contentEquals(".")) this.newPriceTextField.appendText("0");
				this.newPriceTextField.appendText(keyEvent.getCharacter());
			}
		});
		
		this.productTable.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) {
				this.newPriceTextField.requestFocus();
			}
			else if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE) {
				updateNewPrice(null);
				selectNextProduct();
			}
		});
		
		this.newPriceTextField.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleUpdatePriceButton();
			if(keyEvent.getCode() == KeyCode.ESCAPE) this.productTable.requestFocus();
		});
	}
	

	
	/**
	 * Adding data to the view components
	 */
	private void setComponents() {
		this.productTable.setItems(ProductService.getActiveProductsFiltered(null, null));
		this.productTable.refresh();
		this.productTable.getSortOrder().add(this.nameColumn);
		this.productTable.getSortOrder().add(this.sizeColumn);
		this.productTable.getSortOrder().add(this.qualityColumn);
		sortProductTable();
		this.productTable.getSelectionModel().selectFirst();
		
		this.filterService = new FilterService();
		this.categoryComboBox.setItems(this.filterService.getCategories());
		this.familyComboBox.setDisable(true);
		this.newCategorySelected = false;
		
		TextFormatterDouble textFormatter = new TextFormatterDouble();
		this.newPriceTextField.setTextFormatter(textFormatter.getTextFormatterDouble());
	}
	
	/**
	 * Show informations of product selected
	 * @param product - product currently selected
	 */
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
	
	/**
	 * Update product table with filtering by category and family
	 * @param category - the category to be used for filtering
	 * @param family - the family to be used for filtering
	 */
	private void updateProductTable(String category, String family) {
		this.currentCategory = category;
		this.currentFamily = family;
		this.productTable.setItems(ProductService.getActiveProductsFiltered(this.currentCategory, this.currentFamily));
		sortProductTable();
		this.productTable.refresh();
	}
	
	/**
	 * Sort products in the table
	 */
	private void sortProductTable() {
		this.nameColumn.setSortable(true);
		this.sizeColumn.setSortable(true);
		this.qualityColumn.setSortable(true);
		this.qualityColumn.setSortable(false);
		this.sizeColumn.setSortable(false);
		this.nameColumn.setSortable(false);
	}
	
	/**
	 * Customize the display of the selection in the table to have the currently selected product around the center of the table
	 */
	private void selectNextProduct() {
		this.productTable.getSelectionModel().selectNext();
		if(this.numberVisibleRow/2 - this.productTable.getSelectionModel().getSelectedIndex() <= 0) {
			this.productTable.scrollTo(this.productTable.getSelectionModel().getSelectedIndex() - this.numberVisibleRow/2);
		}
		else {
			this.productTable.scrollTo(0);
		}
	}
	
	/**
	 * Update the price of the current product
	 * @param newPrice - price to be updated on the current product 
	 */
	private void updateNewPrice(Double newPrice) {
		if (this.currentProduct != null) {
			if (this.currentProduct.getActualPrice().equals(newPrice)) newPrice = null;
			this.currentProduct.setNewPrice(newPrice);
			ProductService.updateUpdatingProducts(this.currentProduct);
			if (this.currentProduct.getNewPrice() != null) this.newPriceTextField.setPromptText(this.currentProduct.getNewPrice().toString());
			else this.newPriceTextField.setPromptText(this.currentProduct.getActualPrice().toString());
			this.productTable.refresh();
		}
	}
	
}