package ecofish.interface_magento.view;


import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.FilterService;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.Label;

/**
 * View controller asoociated to the change in product status
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
	Button updatePriceButton;
	
	@FXML
	ComboBox<String> categoryComboBox;
	
	@FXML
	ComboBox<String> familyComboBox;	
	
	private Product currentInactiveProduct;
	
	private Product currentActiveProduct;
	
	private FilterService filterService;
	
	private String currentCategory;
	
	private String currentFamily;
	
	private Boolean newCategorySelected;
	
    private final static PseudoClass inactiveToActive = PseudoClass.getPseudoClass("inactive-to-active");
    private final static PseudoClass activeToInactive = PseudoClass.getPseudoClass("active-to-inactive");
	
    /**
     * Go to show product price view
     */
	@FXML
	private void handleUpdatePriceButton() {
		StageService.showView(ViewService.getView("PriceProductOverview"));
	}
	
	/**
	 * Pass the product from the inactive table to the active table
	 */
	@FXML
	private void handleLeftToRightButton() {
		if (currentInactiveProduct != null) ProductService.changeStatusProduct(currentInactiveProduct);
		sortActiveProductTable();
	}
	
	/**
	 * Pass the product from the active table to the inactive table
	 */
	@FXML
	private void handleRightToLeftButton() {
		if (currentActiveProduct != null) ProductService.changeStatusProduct(currentActiveProduct);
		sortInactiveProductTable();
	}
	
	/**
	 * Reset the filter by category
	 */
	@FXML
	private void resetCategory() {
		if (this.currentCategory != null) {
			this.familyComboBox.setDisable(true);
			this.categoryComboBox.getSelectionModel().clearSelection();
			showProductTables();
		}
	}
	
	/**
	 * Reset the filter by family
	 */
	@FXML
	private void resetFamily() {
		if (this.currentFamily != null) {
			this.familyComboBox.getSelectionModel().clearSelection();
			showProductTables();
		}
	}
	
	/**
	 * Show the list of family
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
		initInactiveTable();
		initActiveTable();
		initItemSelection();
		initKeyPresses();
		setComponents();
	}
	
	/**
	 * Set up inactive table features
	 */
	private void initInactiveTable() {
		this.inactiveProductTable.setPlaceholder(new Label("No inactive products"));
		this.nameInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.inactiveProductTable.setRowFactory(productTable -> new TableRow<Product>() {
		    @Override
		    protected void updateItem(Product product, boolean empty) {
		        super.updateItem(product, empty);
		        if (product != null && product.getChangeActive() == true) {
		        	this.pseudoClassStateChanged(activeToInactive, true);
		        }
		        else {
		        	this.pseudoClassStateChanged(activeToInactive, false);
		        }
		    }
		});
	}
	
	/**
	 * Set up active table features
	 */
	private void initActiveTable() {
		this.activeProductTable.setPlaceholder(new Label("No active products"));
		this.nameActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.activeProductTable.setRowFactory(productTable -> new TableRow<Product>() {
		    @Override
		    protected void updateItem(Product product, boolean empty) {
		        super.updateItem(product, empty);
		        if (product != null && product.getChangeActive() == true) {
		        	this.pseudoClassStateChanged(inactiveToActive, true);
		        }
		        else {
		        	this.pseudoClassStateChanged(inactiveToActive, false);
		        }
		    }
		});
	}
	
	/**
	 * Adding action on the item selected
	 */
	private void initItemSelection() {
		this.inactiveProductTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				currentInactiveProduct = newValue;
			}
		});
		
		this.activeProductTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				currentActiveProduct = newValue;
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
		
		this.filterService = new FilterService();
		this.categoryComboBox.setItems(this.filterService.getCategories());
		this.familyComboBox.setDisable(true);
		this.newCategorySelected = false;
	}
	
	/**
	 * Update product table with filtering by category and family
	 * @param category - the category to be used for filtering
	 * @param family - the family to be used for filtering
	 */
	private void updateProductTable(String category, String family) {
		this.currentCategory = category;
		this.currentFamily = family;
		this.inactiveProductTable.setItems(ProductService.getInactiveProductsFiltered(this.currentCategory, this.currentFamily));
		this.activeProductTable.setItems(ProductService.getActiveProductsFiltered(this.currentCategory, this.currentFamily));
		sortInactiveProductTable();
		sortActiveProductTable();
		this.inactiveProductTable.refresh();
		this.activeProductTable.refresh();
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
	
}