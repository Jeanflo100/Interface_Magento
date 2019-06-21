package ecofish.interface_magento.service;

import java.util.TreeMap;
import java.util.TreeSet;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	
	private String currentCategory;
	private String currentFamily;
	
	private Boolean newCategorySelected;
	
	/**
	 * Updates existing product groups
	 * @param productGroups - existing product groups
	 */
	public static void setGroups(TreeMap<String, TreeSet<String>> productGroups){
		groups = productGroups;
	}
	
	/**
	 * Initialization of parameters
	 * @param familyComboBox 
	 * @param categoryComboBox 
	 */
	public Filters(ComboBox<String> categoryComboBox, ComboBox<String> familyComboBox) {
		this.categoryComboBox = categoryComboBox;
		this.familyComboBox = familyComboBox;
		this.categories = FXCollections.observableArrayList();
		this.families = FXCollections.observableArrayList();
		initItemSelection();
		setComponents();
	}
	
	/**
	 * Adding action on the item element
	 */
	private void initItemSelection() {
		this.categoryComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (!categoryComboBox.isShowing() && categoryComboBox.getSelectionModel().getSelectedItem() != null) {
				categoryComboBox.show();
			}
			currentCategory = newValue;
			newCategorySelected = true;
			updateTable(currentCategory, null);
		});
		
		this.familyComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (!familyComboBox.isShowing() && familyComboBox.getSelectionModel().getSelectedItem() != null) {
				familyComboBox.show();
			}
			currentFamily = newValue;
			updateTable(currentCategory, currentFamily);
		});
	}
	
	/**
	 * Adding data to the view components
	 */
	private void setComponents() {
		this.categoryComboBox.setItems(getCategories());
		this.familyComboBox.setDisable(true);
		this.newCategorySelected = false;
	}
	
	/**
	 * Returns the list of categories
	 * @return List of categories
	 */
	private ObservableList<String> getCategories(){
		this.categories.clear();
		for (String category : groups.keySet()) {
			this.categories.add(category);
		}
		return this.categories;
	}
	
	/**
	 * Returns the list of families of the category passed in parameter
	 * @param category - desired category
	 * @return List of families in the chosen category
	 */
	private ObservableList<String> getFamilies(String category){
		this.families.clear();
		for (String family : groups.get(category)) {
			this.families.add(family);
		}
		return this.families;
	}
	
	/**
	 * Reset the filter by category
	 */
	public void resetCategory() {
		if (this.currentCategory != null) {
			this.familyComboBox.setDisable(true);
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
		if (this.currentCategory != null) {
			if (this.newCategorySelected == true) {
				this.newCategorySelected = false;
				this.familyComboBox.setItems(getFamilies(this.currentCategory));
				this.familyComboBox.setDisable(false);
			}
			this.familyComboBox.requestFocus();
			this.familyComboBox.show();
		}
		else {
			showTable();
		}
	}
	
	/**
	 * Update product table with filtering by category and family
	 * @param category - the category to be used for filtering
	 * @param family - the family to be used for filtering
	 */
	protected abstract void updateTable(String category, String family);
	
	/**
	 * Select the product tables
	 */
	protected abstract void showTable();
	
}