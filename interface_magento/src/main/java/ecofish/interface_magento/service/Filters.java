package ecofish.interface_magento.service;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import ecofish.interface_magento.model.Product;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * Provides processing for category and family ComboBox
 * @author Jean-Florian Tassart
 */
public abstract class Filters {

	private static TreeMap<String, TreeSet<String>> groups;
	private final ObservableList<String> categories;
	private final ObservableList<String> families;
	
	private final ComboBox<String> categoryComboBox;
	private final ComboBox<String> familyComboBox;
	private final TextField nametextField;
	private final CheckBox onlyModifiedProductsCheckBox;
	private final FilteredList<Product> modifiedProductList;
	private final List<FilteredList<Product>> productList;
	
	private String currentCategory;
	private String currentFamily;
	private String currentName;
	private Boolean onlyModifiedProducts;
	
	private Boolean isNewCategory;
	
	/**
	 * Updates existing product groups
	 * @param productGroups - existing product groups
	 */
	public static void setGroups(TreeMap<String, TreeSet<String>> productGroups){
		groups = productGroups;
	}
	
	/**
	 * Initialization of parameters
	 * @param categoryComboBox - combobox in which the categories will be displayed
	 * @param familyComboBox - combobox in which the families will be displayed
	 * @param nameTextField - product search area
	 * @param onlyModifiedProductsCheckBox - Only displays products that have been modified
	 * @param modifiedProductList - list of products that have been modified
	 * @param productList - list to be updated according to the category and family selected
	 */
	public Filters(ComboBox<String> categoryComboBox, ComboBox<String> familyComboBox, TextField nameTextField, CheckBox onlyModifiedProductsCheckBox, FilteredList<Product> modifiedProductList, FilteredList<Product> productList) {
		this(categoryComboBox, familyComboBox, nameTextField, onlyModifiedProductsCheckBox, modifiedProductList, Arrays.asList(productList));
	}
	
	/**
	 * Initialization of parameters
	 * @param categoryComboBox - combobox in which the categories will be displayed
	 * @param familyComboBox - combobox in which the families will be displayed
	 * @param nameTextField - product search area
	 * @param onlyModifiedProductsCheckBox - Only displays products that have been modified
	 * @param modifiedProductList - list of products that have been modified
	 * @param productList - list to be updated according to the category and family selected
	 */
	public Filters(ComboBox<String> categoryComboBox, ComboBox<String> familyComboBox, TextField nameTextField, CheckBox onlyModifiedProductsCheckBox, FilteredList<Product> modifiedProductList,List<FilteredList<Product>> productList) {
		this.categoryComboBox = categoryComboBox;
		this.familyComboBox = familyComboBox;
		this.nametextField = nameTextField;
		this.onlyModifiedProductsCheckBox = onlyModifiedProductsCheckBox;
		this.modifiedProductList = modifiedProductList;
		this.productList = productList;

		this.categories = FXCollections.observableArrayList();
		this.families = FXCollections.observableArrayList();
		
		this.currentName = "";
		this.onlyModifiedProducts = false;
		
		initItemSelection();
		initSmoothness();
		setComponents();
		
		updateCategories();
	}
	
	/**
	 * Addition action on the item selectionned
	 */
	private void initItemSelection() {
		this.categoryComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (!categoryComboBox.isShowing() && categoryComboBox.getSelectionModel().getSelectedItem() != null) {
				categoryComboBox.show();
			}
			currentCategory = newValue;
			if (newValue != null) isNewCategory = true;
			else isNewCategory = false;
			updateFamilies(newValue);
			updateList(newValue, currentFamily, currentName, onlyModifiedProducts);
		});
		
		this.familyComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (!familyComboBox.isShowing() && familyComboBox.getSelectionModel().getSelectedItem() != null) {
				familyComboBox.show();
			}
			currentFamily = newValue;
			updateList(currentCategory, newValue, currentName, onlyModifiedProducts);
		});
		
		this.nametextField.textProperty().addListener((observable, oldValue, newValue) -> {
			currentName = newValue;
			updateList(currentCategory, currentFamily, newValue, onlyModifiedProducts);
		});
		
		this.onlyModifiedProductsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			onlyModifiedProducts = newValue;
			updateList(currentCategory, currentFamily, currentName, newValue);
			showTable();
		});
		
		this.modifiedProductList.addListener((ListChangeListener.Change<? extends Product> change) -> { 
			if (onlyModifiedProducts) updateList(currentCategory, currentFamily, currentName, onlyModifiedProducts);
		});
	}
	
	/**
	 * Addition of events allowing a smooth use of the interface
	 */
	private void initSmoothness() {
		this.categoryComboBox.setOnHidden(hidden -> {
			if (isNewCategory) {
				isNewCategory = false;
				showFamily();
			}
			else showTable();
		});
		
		this.familyComboBox.setOnHidden(hidden -> {
			showTable();
		});
		
		this.nametextField.setOnKeyPressed(keyEvent -> {
			if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.ESCAPE) showTable();
		});
	}
	
	/**
	 * Adding data to the view components
	 */
	private void setComponents() {
		this.categoryComboBox.setItems(categories);
		this.familyComboBox.setItems(families);
		this.familyComboBox.setDisable(true);
		this.isNewCategory = false;
	}
	
	/**
	 * Updates the list of categories
	 */
	private void updateCategories(){
		if (groups != null) this.categories.setAll(groups.keySet());
	}
	
	/**
	 * Updates the list of families of the category passed in parameter
	 * @param category - desired category
	 */
	private void updateFamilies(String category){
		if (groups != null && category != null) this.families.setAll(groups.get(category));
	}
	
	/**
	 * Reset the filter by category
	 */
	public void resetCategory() {
		resetFamily();
		this.familyComboBox.setDisable(true);
		if (this.currentCategory != null) {
			this.categoryComboBox.getSelectionModel().clearSelection();
			showTable();
		}
	}
	
	/**
	 * Reset the filter by family
	 */
	public void resetFamily() {
		if (this.currentFamily != null) {
			this.familyComboBox.getSelectionModel().clearSelection();
			showTable();
		}
	}
	
	/**
	 * Show the list of family
	 */
	public void showFamily() {
		this.familyComboBox.setDisable(false);
		this.familyComboBox.requestFocus();
		this.familyComboBox.show();
	}
	
	/**
	 * Update lists with filtering by category and family
	 * @param category - the category to be used for filtering
	 * @param family - the family to be used for filtering
	 */
	private void updateList(String category, String family, String name, Boolean onlyModifiedProducts) {
		for (FilteredList<Product> filteredList : this.productList) {
			filteredList.setPredicate(product -> {
				if (!onlyModifiedProducts || modifiedProductList.contains(product)) {
					if (product.getName().toLowerCase().contains(name.toLowerCase()) || product.getIdProduct().toString().toLowerCase().contains(name.toLowerCase())) {
						if (category == null && family == null) {
							return true;
						}
						else if (product.getCategory().equals(category) && family == null) {
							return true;
						}
						else if ((product.getCategory().contentEquals(category)) && (product.getFamily().contentEquals(family))) {
							return true;
						}
					}
				}
				return false;
			});
		}
	}
	
	/**
	 * Select the product tables
	 */
	protected abstract void showTable();
	
}
