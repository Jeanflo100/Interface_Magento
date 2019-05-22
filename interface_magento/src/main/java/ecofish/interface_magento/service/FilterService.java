package ecofish.interface_magento.service;

import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilterService {

	private TreeMap<String, TreeSet<String>> groups;
	private ObservableList<String> categories;
	private ObservableList<String> families;
	
	private FilterService() {
		groups = new TreeMap<String, TreeSet<String>>();
		categories = FXCollections.observableArrayList();
		families = FXCollections.observableArrayList();
	}
	
	public static TreeMap<String, TreeSet<String>> getGroups(){
		return FilterServiceHolder.INSTANCE.groups;
	}
	
	public static ObservableList<String> getCategories(){
		FilterServiceHolder.INSTANCE.categories.clear();
		for (String category : FilterServiceHolder.INSTANCE.groups.keySet()) {
			FilterServiceHolder.INSTANCE.categories.add(category);
		}
		return FilterServiceHolder.INSTANCE.categories;
	}
	
	public static ObservableList<String> getFamilies(String category){
		FilterServiceHolder.INSTANCE.families.clear();
		for (String family : FilterServiceHolder.INSTANCE.groups.get(category)) {
			FilterServiceHolder.INSTANCE.families.add(family);
		}
		return FilterServiceHolder.INSTANCE.families;
	}
	
	private static class FilterServiceHolder {
		private static FilterService INSTANCE = new FilterService();
	}
	
}
