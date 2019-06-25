package ecofish.interface_magento.view;

import java.util.Arrays;
import java.util.Comparator;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.Filters;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

/**
 * View controller associated to the change in product status
 * @author Jean-Florian Tassart
 */
public class StatusProductOverviewController {

	@FXML
	TableView<Product> inactiveProductTable;
	
	@FXML
	TableView<Product> activeProductTable;
	
	@FXML
	TableColumn<Product, String> nameActiveProductColumn;

	@FXML
	TableColumn<Product, String> sizeActiveProductColumn;
	
	@FXML
	TableColumn<Product, String> qualityActiveProductColumn;
	
	@FXML
	TableColumn<Product, String> nameInactiveProductColumn;

	@FXML
	TableColumn<Product, String> sizeInactiveProductColumn;
	
	@FXML
	TableColumn<Product, String> qualityInactiveProductColumn;
	
	@FXML
	Button leftToRightButton;
	
	@FXML
	Button rightToLeftButton;
	
	@FXML
	Button productDetailsButton;
	
	@FXML
	Button updatePriceButton;
	
	@FXML
	Text descriptionText;
	
	@FXML
	ComboBox<String> categoryComboBox;
	
	@FXML
	ComboBox<String> familyComboBox;
	
    private final static PseudoClass inactiveToActive = PseudoClass.getPseudoClass("inactive-to-active");
    private final static PseudoClass activeToInactive = PseudoClass.getPseudoClass("active-to-inactive");
	
	private Filters filters;
	
	private Product currentInactiveProduct;
	
	private Product currentActiveProduct;
	
    /**
     * Go to show product price view
     */
	@FXML
	private void handleUpdatePriceButton() {
		StageService.showView(Views.viewsPrimaryStage.PriceProductOverview);
	}
	
	/**
	 * Go to show product details view
	 */
	@FXML
	private void handleProductDetailsButton() {
		//TODO
	}
	
	/**
	 * Pass the product from the inactive table to the active table
	 */
	@FXML
	private void handleLeftToRightButton() {
		if (currentInactiveProduct != null) ProductService.changeStatusProduct(currentInactiveProduct);
		this.inactiveProductTable.requestFocus();
	}
	
	/**
	 * Pass the product from the active table to the inactive table
	 */
	@FXML
	private void handleRightToLeftButton() {
		if (currentActiveProduct != null) ProductService.changeStatusProduct(currentActiveProduct);
		this.activeProductTable.requestFocus();
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
		initInactiveProductTable();
		initActiveProductTable();
		initItemSelectionTables();
		initKeyPressesTables();
		setComponents();
	}
	
	/**
	 * Set up inactive table features
	 */
	private void initInactiveProductTable() {
		this.inactiveProductTable.setPlaceholder(new Label("No inactive products"));
		this.nameInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.inactiveProductTable.setRowFactory(productTable -> new TableRow<Product>() {
		    @Override
		    protected void updateItem(Product product, boolean empty) {
		        super.updateItem(product, empty);
		        if (product != null && product.getChangeActive() == true) this.pseudoClassStateChanged(activeToInactive, true);
		        else this.pseudoClassStateChanged(activeToInactive, false);
		    }
		});
	}
	
	/**
	 * Set up active table features
	 */
	private void initActiveProductTable() {
		this.activeProductTable.setPlaceholder(new Label("No active products"));
		this.nameActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.activeProductTable.setRowFactory(productTable -> new TableRow<Product>() {
		    @Override
		    protected void updateItem(Product product, boolean empty) {
		        super.updateItem(product, empty);
		        if (product != null && product.getChangeActive() == true) this.pseudoClassStateChanged(inactiveToActive, true);
		        else this.pseudoClassStateChanged(inactiveToActive, false);
		    }
		});
	}
	
	/**
	 * Adding action on the item selected
	 */
	private void initItemSelectionTables() {
		this.inactiveProductTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Product> observable, Product oldValue, Product newValue) -> {
			currentInactiveProduct = newValue;
			showProduct(currentInactiveProduct);
		});
		
		this.activeProductTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Product> observable, Product oldValue, Product newValue) -> {
			currentActiveProduct = newValue;
			showProduct(currentActiveProduct);
		});
		
		this.inactiveProductTable.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (newValue) showProduct(currentInactiveProduct);
		});
		
		this.activeProductTable.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if (newValue) showProduct(currentActiveProduct);
		});
	}
	
	/**
	 * Added faster navigation using the keys
	 */
	private void initKeyPressesTables() {
		this.inactiveProductTable.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.RIGHT) this.activeProductTable.requestFocus();
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleLeftToRightButton();
		});
		
		this.activeProductTable.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.LEFT) this.inactiveProductTable.requestFocus();
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleRightToLeftButton();
		});
	}
	
	/**
	 * Adding data to the view components
	 */
	private void setComponents() {
		Comparator<Product> comparatorProduct = new Comparator<Product>() {
			@Override
			public int compare(Product o1, Product o2) {
				if (o1.getName().compareTo(o2.getName()) != 0) return o1.getName().compareTo(o2.getName());
				else if (o1.getSize() != null && o2.getSize() != null && o1.getSize().compareTo(o2.getSize()) != 0) return o1.getSize().compareTo(o2.getSize());
				else if (o1.getQuality() != null && o2.getQuality() != null && o1.getQuality().compareTo(o2.getQuality()) != 0) return o1.getQuality().compareTo(o2.getQuality());
				else return o1.getSku().compareTo(o2.getSku());
			}
		};
		
		SortedList<Product> sortedInactiveProducts = ProductService.getInactiveProducts();
		SortedList<Product> sortedActiveProducts = ProductService.getActiveProducts();
		
		sortedInactiveProducts.setComparator(comparatorProduct);
		sortedActiveProducts.setComparator(comparatorProduct);
		
		FilteredList<Product> sortedAndFilteredInactiveProducts = new FilteredList<Product>(sortedInactiveProducts);
		FilteredList<Product> sortedAndFilteredActiveProducts = new FilteredList<Product>(sortedActiveProducts);
		
		this.inactiveProductTable.setItems(sortedAndFilteredInactiveProducts);
		this.activeProductTable.setItems(sortedAndFilteredActiveProducts);
		
		this.inactiveProductTable.getSelectionModel().selectFirst();
		this.activeProductTable.getSelectionModel().selectFirst();
		
		this.filters = new Filters(this.categoryComboBox, this.familyComboBox, Arrays.asList(sortedAndFilteredInactiveProducts, sortedAndFilteredActiveProducts)) {
			@Override
			public void showTable() {
				if (!inactiveProductTable.getItems().isEmpty()) inactiveProductTable.requestFocus();
				else if (!activeProductTable.getItems().isEmpty()) activeProductTable.requestFocus();
				inactiveProductTable.getSelectionModel().selectFirst();
				activeProductTable.getSelectionModel().selectFirst();
				inactiveProductTable.scrollTo(currentInactiveProduct);
				activeProductTable.scrollTo(currentActiveProduct);
			}
		};
	}
	
	/**
	 * Show informations of product selected
	 * @param product - product currently selected
	 */
	private void showProduct(Product product) {
		if (product != null) {
			this.descriptionText.setText(product.toString());
			this.productDetailsButton.setDisable(false);
		}
		else {
			this.descriptionText.setText(null);
			this.productDetailsButton.setDisable(true);
		}
	}
	
}