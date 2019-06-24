package ecofish.interface_magento.service;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import ecofish.interface_magento.model.Product;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

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
	private final List<FilteredList<Product>> filteredLists;
	
	private String currentCategory;
	private String currentFamily;
	
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
	 * @param filteredList - list to be updated according to the category and family selected
	 */
	public Filters(ComboBox<String> categoryComboBox, ComboBox<String> familyComboBox, FilteredList<Product> filteredList) {
		this(categoryComboBox, familyComboBox, Arrays.asList(filteredList));
	}
	
	/**
	 * Initialization of parameters
	 * @param categoryComboBox - combobox in which the categories will be displayed
	 * @param familyComboBox - combobox in which the families will be displayed
	 * @param filteredLists - lists to be updated according to the category and family selected
	 */
	public Filters(ComboBox<String> categoryComboBox, ComboBox<String> familyComboBox, List<FilteredList<Product>> filteredLists) {
		this.categoryComboBox = categoryComboBox;
		this.familyComboBox = familyComboBox;
		this.filteredLists = filteredLists;

		this.categories = FXCollections.observableArrayList();
		this.families = FXCollections.observableArrayList();
		
		initItemSelection();
		setComponents();
		
		updateCategories();
	}
	
	/**
	 * Adding action on the item selectionned and the action performed at the closure of the combobox
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
			updateList(newValue, null);
		});
		
		this.familyComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (!familyComboBox.isShowing() && familyComboBox.getSelectionModel().getSelectedItem() != null) {
				familyComboBox.show();
			}
			currentFamily = newValue;
			updateList(currentCategory, newValue);
		});
		
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
	private void updateList(String category, String family) {
		for (FilteredList<Product> filteredList : this.filteredLists)
			filteredList.setPredicate(product -> {
			if (category == null && family == null) {
				return true;
			}
			else if (product.getCategory().equals(category) && family == null) {
				return true;
			}
			else if ((product.getCategory().contentEquals(category)) && (product.getFamily().contentEquals(family))) {
				return true;
			}
			return false;
		});
	}
	
	/**
	 * Select the product tables
	 */
	protected abstract void showTable();
	
}
