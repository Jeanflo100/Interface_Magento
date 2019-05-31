package ecofish.interface_magento.service;

import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilterService {

	private static TreeMap<String, TreeSet<String>> groups;
	private ObservableList<String> categories;
	private ObservableList<String> families;
	
	public FilterService() {
		this.categories = FXCollections.observableArrayList();
		this.families = FXCollections.observableArrayList();
	}
	
	public static TreeMap<String, TreeSet<String>> getGroups(){
		if (groups == null) groups = new TreeMap<String, TreeSet<String>>();
		return groups;
	}
	
	public ObservableList<String> getCategories(){
		this.categories.clear();
		for (String category : groups.keySet()) {
			this.categories.add(category);
		}
		return this.categories;
	}
	
	public ObservableList<String> getFamilies(String category){
		this.families.clear();
		for (String family : groups.get(category)) {
			this.families.add(family);
		}
		return this.families;
	}
	
}
