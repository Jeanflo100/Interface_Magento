package ecofish.interface_magento.service;

import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Supplies the different categories and families of product
 * @author Jean-Florian Tassart
 */
public class FilterService {

	private static TreeMap<String, TreeSet<String>> groups;
	private ObservableList<String> categories;
	private ObservableList<String> families;
	
	/**
	 * Initialization of parameters
	 */
	public FilterService() {
		this.categories = FXCollections.observableArrayList();
		this.families = FXCollections.observableArrayList();
	}
	
	/**
	 * Returns a dictionary with as key each category and as value the list of families associated
	 * @return Dictionnary category/families
	 */
	public static TreeMap<String, TreeSet<String>> getGroups(){
		if (groups == null) groups = new TreeMap<String, TreeSet<String>>();
		return groups;
	}
	
	/**
	 * Returns the list of categories
	 * @return List of categories
	 */
	public ObservableList<String> getCategories(){
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
	public ObservableList<String> getFamilies(String category){
		this.families.clear();
		for (String family : groups.get(category)) {
			this.families.add(family);
		}
		return this.families;
	}
	
}
