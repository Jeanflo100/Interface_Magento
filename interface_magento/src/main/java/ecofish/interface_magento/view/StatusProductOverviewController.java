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

public class StatusProductOverviewController{

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
	
	private String currentCategory;
	
	private String currentFamily;
	
	private Boolean newCategorySelected;
	
    private final static PseudoClass inactiveToActive = PseudoClass.getPseudoClass("inactive-to-active");
    private final static PseudoClass activeToInactive = PseudoClass.getPseudoClass("active-to-inactive");
	
	@FXML
	private void handleUpdatePriceButton() {
		System.out.println("UpdatePrice bouton");
		StageService.showView(ViewService.getView("PriceProductOverview"));
	}
	
	@FXML
	private void handleLeftToRightButton() {
		System.out.println("LeftToRight Button");
		if (currentInactiveProduct != null) ProductService.changeStatusProduct(currentInactiveProduct);
		sortActiveProductTable();
	}
	
	@FXML
	private void handleRightToLeftButton() {
		System.out.println("RightToLeft Button");
		if (currentActiveProduct != null) ProductService.changeStatusProduct(currentActiveProduct);
		sortInactiveProductTable();
	}
	
	@FXML
	private void resetCategory() {
		if (this.currentCategory != null) {
			this.familyComboBox.setDisable(true);
			this.categoryComboBox.getSelectionModel().clearSelection();
			showProductTables();
		}
	}
	
	@FXML
	private void resetFamily() {
		if (this.currentFamily != null) {
			this.familyComboBox.getSelectionModel().clearSelection();
			showProductTables();
		}
	}
	
	@FXML
	private void showFamily() {
		if (this.currentCategory != null) {
			if (this.newCategorySelected == true) {
				this.newCategorySelected = false;
				this.familyComboBox.setItems(FilterService.getFamilies(this.currentCategory));
				this.familyComboBox.setDisable(false);
			}
			this.familyComboBox.requestFocus();
			this.familyComboBox.show();
		}
	}
	
	@FXML
	private void showProductTables() {
		if (!this.inactiveProductTable.getItems().isEmpty()) this.inactiveProductTable.requestFocus();
		else if (!this.activeProductTable.getItems().isEmpty()) this.activeProductTable.requestFocus();
		this.inactiveProductTable.getSelectionModel().selectFirst();
		this.activeProductTable.getSelectionModel().selectFirst();
		this.inactiveProductTable.scrollTo(this.currentInactiveProduct);
		this.activeProductTable.scrollTo(this.currentActiveProduct);
	}
	
	@FXML
	private void initialize() {
		System.out.println("initialize");
				
		this.inactiveProductTable.setItems(ProductService.getInactiveProducts(null, null));
		this.nameInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.inactiveProductTable.setPlaceholder(new Label("No inactive products"));
		this.inactiveProductTable.refresh();
		this.inactiveProductTable.getSortOrder().add(this.nameInactiveProductColumn);
		this.inactiveProductTable.getSortOrder().add(this.sizeInactiveProductColumn);
		this.inactiveProductTable.getSortOrder().add(this.qualityInactiveProductColumn);
		sortInactiveProductTable();

		this.activeProductTable.setPlaceholder(new Label("No active products"));
		this.nameActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.sizeActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.qualityActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.activeProductTable.setItems(ProductService.getActiveProducts(null, null));
		this.activeProductTable.refresh();
		this.activeProductTable.getSortOrder().add(this.nameActiveProductColumn);
		this.activeProductTable.getSortOrder().add(this.sizeActiveProductColumn);
		this.activeProductTable.getSortOrder().add(this.qualityActiveProductColumn);
		sortActiveProductTable();
		
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
		
		this.inactiveProductTable.getSelectionModel().selectFirst();
		this.activeProductTable.getSelectionModel().selectFirst();
		
		this.inactiveProductTable.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.RIGHT) this.activeProductTable.requestFocus();
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleLeftToRightButton();
		});
		
		this.activeProductTable.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.LEFT) this.inactiveProductTable.requestFocus();
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleRightToLeftButton();
		});
		
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
		
		this.categoryComboBox.setItems(FilterService.getCategories());
		this.familyComboBox.setDisable(true);
		this.newCategorySelected = false;
		this.categoryComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				newCategorySelected = true;
				updateProductTable(newValue, null);
			}
		});
		
		this.familyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateProductTable(currentCategory, newValue);
			}
		});
		
	}
	
	private void updateProductTable(String category, String family) {
		this.currentCategory = category;
		this.currentFamily = family;
		this.inactiveProductTable.setItems(ProductService.getInactiveProducts(this.currentCategory, this.currentFamily));
		this.activeProductTable.setItems(ProductService.getActiveProducts(this.currentCategory, this.currentFamily));
		sortInactiveProductTable();
		sortActiveProductTable();
		this.inactiveProductTable.refresh();
		this.activeProductTable.refresh();
	}
	
	private void sortInactiveProductTable() {
		this.nameInactiveProductColumn.setSortable(true);
		this.sizeInactiveProductColumn.setSortable(true);
		this.qualityInactiveProductColumn.setSortable(true);
		this.qualityInactiveProductColumn.setSortable(false);
		this.sizeInactiveProductColumn.setSortable(false);
		this.nameInactiveProductColumn.setSortable(false);
	}
	
	private void sortActiveProductTable() {
		this.nameActiveProductColumn.setSortable(true);
		this.sizeActiveProductColumn.setSortable(true);
		this.qualityActiveProductColumn.setSortable(true);
		this.qualityActiveProductColumn.setSortable(false);
		this.sizeActiveProductColumn.setSortable(false);
		this.nameActiveProductColumn.setSortable(false);
	}
	
}