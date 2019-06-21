package ecofish.interface_magento.view;


import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.Filters;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.Views;
import javafx.beans.value.ObservableValue;
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
		sortActiveProductTable();
		this.inactiveProductTable.requestFocus();
	}
	
	/**
	 * Pass the product from the active table to the inactive table
	 */
	@FXML
	private void handleRightToLeftButton() {
		if (currentActiveProduct != null) ProductService.changeStatusProduct(currentActiveProduct);
		sortInactiveProductTable();
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
	 * Show the list of family
	 */
	@FXML
	private void showFamily() {
		this.filters.showFamily();
	}
	
	/**
	 * Select the product tables
	 */
	@FXML
	private void showProductTables() {
		if (!this.inactiveProductTable.getItems().isEmpty()) this.inactiveProductTable.requestFocus();
		else if (!this.activeProductTable.getItems().isEmpty()) this.activeProductTable.requestFocus();
		this.inactiveProductTable.getSelectionModel().selectFirst();
		this.activeProductTable.getSelectionModel().selectFirst();
		this.inactiveProductTable.scrollTo(this.currentInactiveProduct);
		this.activeProductTable.scrollTo(this.currentActiveProduct);
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
		initFilter();
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
	
	private void initFilter() {
		this.filters = new Filters(this.categoryComboBox, this.familyComboBox) {
			@Override
			protected void updateTable(String category, String family) {
				inactiveProductTable.setItems(ProductService.getInactiveProductsFiltered(category, family));
				activeProductTable.setItems(ProductService.getActiveProductsFiltered(category, family));
				sortInactiveProductTable();
				sortActiveProductTable();
				inactiveProductTable.refresh();
				activeProductTable.refresh();
			}
			@Override
			public void showTable() {
				showProductTables();
			}
		};
	}
	
	/**
	 * Adding data to the view components
	 */
	private void setComponents() {
		this.inactiveProductTable.setItems(ProductService.getInactiveProductsFiltered(null, null));
		this.inactiveProductTable.refresh();
		this.inactiveProductTable.getSortOrder().add(this.nameInactiveProductColumn);
		this.inactiveProductTable.getSortOrder().add(this.sizeInactiveProductColumn);
		this.inactiveProductTable.getSortOrder().add(this.qualityInactiveProductColumn);
		sortInactiveProductTable();
		this.inactiveProductTable.getSelectionModel().selectFirst();

		this.activeProductTable.setItems(ProductService.getActiveProductsFiltered(null, null));
		this.activeProductTable.refresh();
		this.activeProductTable.getSortOrder().add(this.nameActiveProductColumn);
		this.activeProductTable.getSortOrder().add(this.sizeActiveProductColumn);
		this.activeProductTable.getSortOrder().add(this.qualityActiveProductColumn);
		sortActiveProductTable();
		this.activeProductTable.getSelectionModel().selectFirst();
	}

	/**
	 * Sort products in the inactive table
	 */
	private void sortInactiveProductTable() {
		this.nameInactiveProductColumn.setSortable(true);
		this.sizeInactiveProductColumn.setSortable(true);
		this.qualityInactiveProductColumn.setSortable(true);
		this.qualityInactiveProductColumn.setSortable(false);
		this.sizeInactiveProductColumn.setSortable(false);
		this.nameInactiveProductColumn.setSortable(false);
	}
	
	/**
	 * Sort products in the active table
	 */
	private void sortActiveProductTable() {
		this.nameActiveProductColumn.setSortable(true);
		this.sizeActiveProductColumn.setSortable(true);
		this.qualityActiveProductColumn.setSortable(true);
		this.qualityActiveProductColumn.setSortable(false);
		this.sizeActiveProductColumn.setSortable(false);
		this.nameActiveProductColumn.setSortable(false);
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