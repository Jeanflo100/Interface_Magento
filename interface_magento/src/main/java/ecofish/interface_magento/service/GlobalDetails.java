package ecofish.interface_magento.service;

import java.util.ArrayList;

import ecofish.interface_magento.daos.GettingGlobalDetailsThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.CheckBox;

public class GlobalDetails {
	
	private final FilteredList<CheckBox> filteredAlergens;
	private final FilteredList<CheckBox> filteredBrands;
	private final FilteredList<CheckBox> filteredLabels;
	private final SortedList<String> productionTypes;
	private final FilteredList<CheckBox> filtered_countries_of_manufacture;
	
	
	private GlobalDetails() {
		filteredAlergens = new FilteredList<CheckBox>(FXCollections.emptyObservableList());
		filteredBrands = new FilteredList<CheckBox>(FXCollections.emptyObservableList());
		filteredLabels = new FilteredList<CheckBox>(FXCollections.emptyObservableList());
		productionTypes = new SortedList<String>(FXCollections.emptyObservableList(), (String o1, String o2) -> o1.compareTo(o2));
		filtered_countries_of_manufacture = new FilteredList<CheckBox>(FXCollections.emptyObservableList());
		
		GettingGlobalDetailsThread gettingGlobalDetailsThread = new GettingGlobalDetailsThread();
		new Thread(gettingGlobalDetailsThread).start();
	}
	
	public static void setAlergens(ArrayList<String> alergens) {
		SortedList<CheckBox> sortedAlergens = new SortedList<CheckBox>(FXCollections.emptyObservableList(), (CheckBox o1, CheckBox o2) -> {
			return o1.getText().compareTo(o2.getText());
		});
		for (String alergen : alergens) sortedAlergens.add(new CheckBox(alergen));
		GlobalDetailsHolder.INSTANCE.filteredAlergens.addAll(sortedAlergens);
	}
	
	public static ObservableList<CheckBox> getAlergens() {
		return GlobalDetailsHolder.INSTANCE.filteredAlergens;
	}
	
	public static void updateAlergens(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filteredAlergens.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	public static void setBrands(ArrayList<String> brands) {
		SortedList<CheckBox> sortedBrands = new SortedList<CheckBox>(FXCollections.emptyObservableList(), (CheckBox o1, CheckBox o2) -> {
			return o1.getText().compareTo(o2.getText());
		});
		for (String brand : brands) sortedBrands.add(new CheckBox(brand));
		GlobalDetailsHolder.INSTANCE.filteredAlergens.addAll(sortedBrands);
	}
	
	public static ObservableList<CheckBox> getBrands() {
		return GlobalDetailsHolder.INSTANCE.filteredBrands;
	}
	
	public static void updateBrands(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filteredBrands.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	public static void setLabels(ArrayList<String> labels) {
		SortedList<CheckBox> sortedLabels = new SortedList<CheckBox>(FXCollections.emptyObservableList(), (CheckBox o1, CheckBox o2) -> {
			return o1.getText().compareTo(o2.getText());
		});
		for (String label : labels) sortedLabels.add(new CheckBox(label));
		GlobalDetailsHolder.INSTANCE.filteredLabels.addAll(sortedLabels);
	}
	
	public static ObservableList<CheckBox> getLabels() {
		return GlobalDetailsHolder.INSTANCE.filteredLabels;
	}
	
	public static void updateLabels(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filteredLabels.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	public static void setProductionTypes(ArrayList<String> productionTypes) {
		GlobalDetailsHolder.INSTANCE.productionTypes.addAll(productionTypes);
	}
	
	public static ObservableList<String> getProductionTypes() {
		return GlobalDetailsHolder.INSTANCE.productionTypes;
	}
	
	public static void setCountryOfManufacture(ArrayList<String> countries_of_manufacture) {
		SortedList<CheckBox> sorted_countries_of_manufacture = new SortedList<CheckBox>(FXCollections.emptyObservableList(), (CheckBox o1, CheckBox o2) -> {
			return o1.getText().compareTo(o2.getText());
		});
		for (String country_of_manufacture : countries_of_manufacture) sorted_countries_of_manufacture.add(new CheckBox(country_of_manufacture));
		GlobalDetailsHolder.INSTANCE.filtered_countries_of_manufacture.addAll(sorted_countries_of_manufacture);
	}
	
	public static ObservableList<CheckBox> getCountryOfManufacture() {
		return GlobalDetailsHolder.INSTANCE.filtered_countries_of_manufacture;
	}
	
	public static void updateCountryOfManufacture(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filtered_countries_of_manufacture.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	private static class GlobalDetailsHolder {
		private static GlobalDetails INSTANCE = new GlobalDetails();
	}
	
}