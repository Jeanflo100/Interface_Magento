package ecofish.interface_magento.view;



import java.util.Optional;
import java.util.function.UnaryOperator;

import ecofish.interface_magento.model.Product;
import ecofish.interface_magento.util.NameValueFactory;
import ecofish.interface_magento.util.ActualPriceValueFactory;
import ecofish.interface_magento.util.QualityValueFactory;
import ecofish.interface_magento.util.SizeValueFactory;
import ecofish.interface_magento.service.CategoryService;
import ecofish.interface_magento.service.FamilyService;
import ecofish.interface_magento.service.ProductService;
import ecofish.interface_magento.service.StageService;
import ecofish.interface_magento.service.ViewService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
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
	
	//private boolean newPerson;
	
	@FXML
	private void handleUpdatePriceButton() {
		System.out.println("UpdatePrice bouton");
		StageService.showView(ViewService.getView("PriceProductOverview"));
	}
	
	/*@FXML
	private void test1() {
		System.out.println("test1");
	}
	
	@FXML
	private void test2() {
		//this.productTable.setOnKeyPressed(keyEvent -> System.out.printf("Touche enfoncée : %s %s", keyEvent.getCode(), keyEvent.getCharacter()).println());
		System.out.println("test2");
	}*/
	
	@FXML
	private void initialize() {
		System.out.println("initialize");
		/*this.nameColumn.setCellValueFactory(new NameValueFactory());
		this.qualityColumn.setCellValueFactory(new QualityValueFactory());
		this.sizeColumn.setCellValueFactory(new SizeValueFactory());
		this.priceColumn.setCellValueFactory(new PriceValueFactory());
		this.productTable.setPlaceholder(new Label("No category and/or family selected"));
		this.productTable.refresh();
		
		this.productTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				System.out.println("passage");
				showProduct(newValue);
			}
		});
				
		this.categoryComboBox.setItems(CategoryService.getCategory());
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