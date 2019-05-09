package ecofish.interface_magento.view;



import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
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
	TableColumn<Product, String> qualityActiveProductColumn;

	@FXML
	TableColumn<Product, String> sizeActiveProductColumn;
	
	@FXML
	TableColumn<Product, String> nameInactiveProductColumn;
	
	@FXML
	TableColumn<Product, String> qualityInactiveProductColumn;

	@FXML
	TableColumn<Product, String> sizeInactiveProductColumn;
	
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
	
	/*@FXML
	TableView<Product> productTable;

	@FXML
	TableColumn<Product, String> nameColumn;
	
	@FXML
	TableColumn<Product, String> qualityColumn;

	@FXML
	TableColumn<Product, String> sizeColumn;
	
	@FXML
	TableColumn<Product, String> priceColumn;
	
	@FXML
	Text descriptionText;
	
	@FXML
	Text oldPriceText;
	
	@FXML
	TextField newPriceTextField;
	
	@FXML
	Button saveButton;*/
	
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
	
	private Product currentInactiveProduct;
	
	private Product currentActiveProduct;
	
	@FXML
	private void handleUpdatePriceButton() {
		System.out.println("UpdatePrice bouton");
		StageService.showView(ViewService.getView("PriceProductOverview"));
	}
	
	@FXML
	private void handleLeftToRightButton() {
		System.out.println("LeftToRight Button");
		if (currentInactiveProduct != null) ProductService.updateStatusProduct(currentInactiveProduct);
	}
	
	@FXML
	private void handleRightToLeftButton() {
		System.out.println("RightToLeft Button");
		if (currentActiveProduct != null) ProductService.updateStatusProduct(currentActiveProduct);
	}
	
	/*@FXML
	private void test2() {
		//this.productTable.setOnKeyPressed(keyEvent -> System.out.printf("Touche enfoncée : %s %s", keyEvent.getCode(), keyEvent.getCharacter()).println());
		System.out.println("test2");
	}*/
	
	@FXML
	private void initialize() {
		System.out.println("initialize");
		this.nameInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.qualityInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.sizeInactiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.inactiveProductTable.setItems(ProductService.getInactiveProducts(null, null));
		this.inactiveProductTable.setPlaceholder(new Label("No inactive products"));
		this.inactiveProductTable.refresh();
		
		this.nameActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		this.qualityActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("quality"));
		this.sizeActiveProductColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("size"));
		this.activeProductTable.setItems(ProductService.getActiveProducts(null, null));
		this.activeProductTable.setPlaceholder(new Label("No active products"));
		this.activeProductTable.refresh();
		
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
		
		if (this.inactiveProductTable.getItems().isEmpty() == false) this.inactiveProductTable.getSelectionModel().select(0);
		if (this.activeProductTable.getItems().isEmpty() == false) this.activeProductTable.getSelectionModel().select(0);
		
		this.inactiveProductTable.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.RIGHT) this.activeProductTable.requestFocus();
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleLeftToRightButton();
		});
		
		this.activeProductTable.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.LEFT) this.inactiveProductTable.requestFocus();
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.SPACE) this.handleRightToLeftButton();
		});
		
		/*this.categoryComboBox.setItems(CategoryService.getCategory());
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
		
		this.productTable.setOnKeyPressed(keyEvent -> System.out.printf("Touche enfoncée : %s %s", keyEvent.getCode(), keyEvent.getCharacter()).println());*/
		
	}
	
}