package ecofish.interface_magento.view;

import java.util.Optional;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.Filters;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.util.DetailsTableView;
import ecofish.interface_magento.util.TextFormatterDouble;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
	CheckBox onlyModifiedProductsCheckBox;
	
	@FXML
	TextField nameTextField;
	
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
	
    private static final PseudoClass increasePrice = PseudoClass.getPseudoClass("high");
    private static final PseudoClass decreasePrice = PseudoClass.getPseudoClass("medium");
	
    private static final Double percentageAllowedBetweenNewAndOldPrice = 10.0;
    
	private Integer numberVisibleRowProductTable;
	
	private Filters filters;
	
	private Product currentProduct;
	private Integer lastIndexValidCurrentProduct;
	
	/**
	 * Checking conditions before updating the price
	 */
	@FXML
	private void handleUpdatePriceButton() {
		if (this.currentProduct != null && this.newPriceTextField.getText().length() != 0) {
			Double actualPrice = this.currentProduct.getActualPrice();
			Double newPrice = Double.parseDouble(this.newPriceTextField.getText());
			if ((Math.abs(newPrice - actualPrice) / actualPrice) > (percentageAllowedBetweenNewAndOldPrice/100)) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initOwner(StageService.getPrimaryStage());
				alert.setHeaderText("The difference between the actual and the new price is more than " + percentageAllowedBetweenNewAndOldPrice +" %, continue ?");
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
		this.filters.resetCategory();
	}
	
	/**
	 * Reset the filter by family
	 */
	@FXML
	private void resetFamily() {
		this.filters.resetFamily();
	}
	
	/**
	 * Initialization of the view
	 */
	@FXML
	private void initialize() {
		initProductTable();
		initItemSelectionTable();
		initKeyPressesTable();
		setComponents();
	}
	
	/**
	 * Set up table features
	 */
	private void initProductTable() {
		this.productTable.setPlaceholder(new Label("No active products"));
		this.nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.actualPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("actualPrice"));
		this.newPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("newPrice"));
		this.productTable.setFixedCellSize(DetailsTableView.CellSize);
		this.numberVisibleRowProductTable = DetailsTableView.getNumberVisibleRow(this.productTable);
		this.productTable.setRowFactory(productTable -> new TableRow<Product>() {
		    @Override
		    protected void updateItem(Product product, boolean empty) {
		        super.updateItem(product, empty);
		        this.pseudoClassStateChanged(increasePrice, false);
		        this.pseudoClassStateChanged(decreasePrice, false);
		        if (product != null && product.getNewPrice() != null) {
		        	if (product.getNewPrice() > product.getActualPrice()) this.pseudoClassStateChanged(increasePrice, true);
		        	else if (product.getNewPrice() < product.getActualPrice()) this.pseudoClassStateChanged(decreasePrice, true);
		        }
		    }
		});
	}
	
	/**
	 * Adding action on the item element
	 */
	private void initItemSelectionTable() {
		this.productTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Product> observable, Product oldValue, Product newValue) -> {
			this.currentProduct = newValue;
			if (newValue != null) this.lastIndexValidCurrentProduct = this.productTable.getSelectionModel().getSelectedIndex();
			showProduct(newValue);
		});
	}
	
	/**
	 * Added faster navigation using the keys
	 */
	private void initKeyPressesTable() {
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
			if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE) {
				updateNewPrice(null);
				selectNextProduct();
			}
			if(keyEvent.getCode() == KeyCode.UP) this.productTable.scrollTo(this.productTable.getSelectionModel().getSelectedIndex()-1 - this.numberVisibleRowProductTable/2);
			if (keyEvent.getCode() == KeyCode.DOWN) this.productTable.scrollTo(this.productTable.getSelectionModel().getSelectedIndex()+1 - this.numberVisibleRowProductTable/2);
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
		SortedList<Product> sortedActiveProducts = ProductService.getActiveProducts();
		sortedActiveProducts.setComparator((Product o1, Product o2) -> {
			if (o1.getName().compareTo(o2.getName()) != 0) return o1.getName().compareTo(o2.getName());
			else if (o1.getSize() != null && o2.getSize() != null && o1.getSize().compareTo(o2.getSize()) != 0) return o1.getSize().compareTo(o2.getSize());
			else if (o1.getQuality() != null && o2.getQuality() != null && o1.getQuality().compareTo(o2.getQuality()) != 0) return o1.getQuality().compareTo(o2.getQuality());
			else if (o1.getActualPrice() != null && o2.getActualPrice() != null && o1.getActualPrice().compareTo(o2.getActualPrice()) != 0) return o1.getActualPrice().compareTo(o2.getActualPrice());
			else if (o1.getNewPrice() != null && o2.getNewPrice() != null && o1.getNewPrice().compareTo(o2.getNewPrice()) != 0) return o1.getNewPrice().compareTo(o2.getNewPrice());
			else return o1.getIdProduct().compareTo(o2.getIdProduct());
		});
		
		FilteredList<Product> sortedAndFilteredActiveProducts = new FilteredList<Product>(sortedActiveProducts);
		this.productTable.setItems(sortedAndFilteredActiveProducts);
		this.productTable.getSelectionModel().selectFirst();
		
		this.filters = new Filters(this.categoryComboBox, this.familyComboBox, this.nameTextField, this.onlyModifiedProductsCheckBox, ProductService.getUpdatingProductsOnPrice(), sortedAndFilteredActiveProducts) {
			@Override
			public void showTable() {
				productTable.getSelectionModel().clearSelection();
				if (!productTable.getItems().isEmpty()) productTable.requestFocus();
				if (productTable.getItems().contains(currentProduct)) {
					productTable.getSelectionModel().select(currentProduct);
					productTable.scrollTo(productTable.getSelectionModel().getSelectedIndex() - numberVisibleRowProductTable/2);
				}
				else productTable.getSelectionModel().select(0);
			}
		};
		
		TextFormatterDouble textFormatter = new TextFormatterDouble();
		this.newPriceTextField.setTextFormatter(textFormatter.getTextFormatterDouble());
	}
	
	/**
	 * Show informations of product selected
	 * @param product - product currently selected
	 */
	private void showProduct(Product product) {
		if (product == null) {
			this.descriptionText.setText(null);
			this.actualPriceText.setText(null);
			this.newPriceTextField.setPromptText(null);
		}
		else {
			this.descriptionText.setText(product.toString());
			this.actualPriceText.setText(product.getActualPrice().toString());
			if (product.getNewPrice() == null) {
				this.newPriceTextField.setPromptText(product.getActualPrice().toString());
			}
			else {
				this.newPriceTextField.setPromptText(product.getNewPrice().toString());
			}
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
			this.productTable.refresh();
		}
	}
	
	/**
	 * Customize the display of the selection in the table to have the currently selected product around the center of the table
	 */
	private void selectNextProduct() {
		if (!this.productTable.getItems().contains(this.currentProduct)) {
			if (this.lastIndexValidCurrentProduct > this.productTable.getItems().size()-1) this.productTable.getSelectionModel().select(this.lastIndexValidCurrentProduct-1);
			else this.productTable.getSelectionModel().select(this.lastIndexValidCurrentProduct);
		}
		else {
			if (this.lastIndexValidCurrentProduct+1 > this.productTable.getItems().size()-1) this.productTable.getSelectionModel().select(this.lastIndexValidCurrentProduct);
			else this.productTable.getSelectionModel().select(this.lastIndexValidCurrentProduct+1);
		}		
		this.productTable.scrollTo(this.productTable.getSelectionModel().getSelectedIndex() - this.numberVisibleRowProductTable/2);
	}
	
}