package ecofish.interface_magento.service;

import java.awt.Checkbox;
import java.util.ArrayList;

import ecofish.interface_magento.daos.GettingGlobalDetailsThread;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Font;

public class GlobalDetails {
	
	private static ObservableList<String> alergensSource;
	private static ObservableList<String> brandsSource;
	private static ObservableList<String> labelsSource;
	private static ObservableList<String> productionTypesSource;
	private static ObservableList<String> countries_of_manufacture_source;
	
	private final ObservableList<CheckBox> alergens;
	private final ObservableList<CheckBox> brands;
	private final ObservableList<CheckBox> labels;
	private final ObservableList<CheckBox> countries_of_manufacture;
	
	private final FilteredList<CheckBox> filteredAlergens;
	private final FilteredList<CheckBox> filteredBrands;
	private final FilteredList<CheckBox> filteredLabels;
	private final SortedList<String> sortedProductionTypes;
	private final FilteredList<CheckBox> filtered_countries_of_manufacture;
	
	
	private GlobalDetails() {		
		alergensSource = FXCollections.observableArrayList();
		brandsSource = FXCollections.observableArrayList();
		labelsSource = FXCollections.observableArrayList();
		productionTypesSource = FXCollections.observableArrayList();
		countries_of_manufacture_source = FXCollections.observableArrayList();
		
		alergens = FXCollections.observableArrayList();
		brands = FXCollections.observableArrayList();
		labels = FXCollections.observableArrayList();
		countries_of_manufacture = FXCollections.observableArrayList();
		
		alergensSource.addListener((ListChangeListener<String>) change -> changeOnSourceList(change, alergens));
		brandsSource.addListener((ListChangeListener<String>) change -> changeOnSourceList(change, brands));
		labelsSource.addListener((ListChangeListener<String>) change -> changeOnSourceList(change, labels));
		countries_of_manufacture_source.addListener((ListChangeListener<String>) change -> changeOnSourceList(change, countries_of_manufacture));
		
		filteredAlergens = new FilteredList<CheckBox>(new SortedList<CheckBox>(alergens, (CheckBox o1, CheckBox o2) -> o1.getText().compareTo(o2.getText())));
		filteredBrands = new FilteredList<CheckBox>(new SortedList<CheckBox>(brands, (CheckBox o1, CheckBox o2) -> o1.getText().compareTo(o2.getText())));
		filteredLabels = new FilteredList<CheckBox>(new SortedList<CheckBox>(labels, (CheckBox o1, CheckBox o2) -> o1.getText().compareTo(o2.getText())));
		sortedProductionTypes = new SortedList<String>(productionTypesSource, (String o1, String o2) -> o1.compareTo(o2));
		filtered_countries_of_manufacture = new FilteredList<CheckBox>(new SortedList<CheckBox>(countries_of_manufacture, (CheckBox o1, CheckBox o2) -> o1.getText().compareTo(o2.getText())));
		
		GettingGlobalDetailsThread gettingGlobalDetailsThread = new GettingGlobalDetailsThread();
		new Thread(gettingGlobalDetailsThread).start();
    	StageService.showView(Views.viewsSecondaryStage.LoadingProduct, true);
	}
	
	private void changeOnSourceList(Change<? extends String> change, ObservableList<CheckBox> list) {
		while(change.next()) {
			if (change.wasAdded()) {
				for (String modif : change.getAddedSubList()) list.add(createCheckBox(modif));
			}
			else if (change.wasRemoved()) {
				for (String modif : change.getRemoved()) list.removeIf(element -> element.getText().equals(modif));
			}
		}
	}
	
	private static CheckBox createCheckBox(String text) {
		CheckBox checkBox = new CheckBox(text);
		checkBox.setAlignment(Pos.TOP_LEFT);
		checkBox.setFont(new Font(13));
		checkBox.setCursor(Cursor.HAND);
		return checkBox;
	}
	
	public static void setAlergens(ArrayList<String> alergens) {
		alergensSource.addAll(alergens);
	}
	
	public static ObservableList<String> getAlergensSource() {
		return alergensSource;
	}
	
	public static void setSelectedAlergens(ArrayList<String> selectedAlergens) {
		GlobalDetailsHolder.INSTANCE.alergens.forEach(alergen -> {
			alergen.setSelected(selectedAlergens.contains(alergen.getText()));
		});
	}
	
	public static ArrayList<String> getSelectedAlergens() {
		ArrayList<String> selectedAlergens = new ArrayList<String>();
		GlobalDetailsHolder.INSTANCE.alergens.forEach(alergen -> {
			if (alergen.isSelected()) selectedAlergens.add(alergen.getText());
		});
		return selectedAlergens;
	}
	
	public static FilteredList<CheckBox> getAlergens() {
		return GlobalDetailsHolder.INSTANCE.filteredAlergens;
	}
	
	public static void onlySelectedAlergens(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filteredAlergens.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	public static void setBrands(ArrayList<String> brands) {
		brandsSource.addAll(brands);
	}
	
	public static ObservableList<String> getBrandsSource() {
		return brandsSource;
	}
	
	public static void setSelectedBrands(ArrayList<String> selectedBrands) {
		GlobalDetailsHolder.INSTANCE.brands.forEach(brand -> {
			brand.setSelected(selectedBrands.contains(brand.getText()));
		});
	}
	
	public static ArrayList<String> getSelectedBrands() {
		ArrayList<String> selectedBrands = new ArrayList<String>();
		GlobalDetailsHolder.INSTANCE.brands.forEach(brand -> {
			if (brand.isSelected()) selectedBrands.add(brand.getText());
		});
		return selectedBrands;
	}
	
	public static FilteredList<CheckBox> getBrands() {
		return GlobalDetailsHolder.INSTANCE.filteredBrands;
	}
	
	public static void onlySelectedBrands(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filteredBrands.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	public static void setLabels(ArrayList<String> labels) {
		labelsSource.addAll(labels);
	}
	
	public static ObservableList<String> getLabelsSource() {
		return labelsSource;
	}
	
	public static void setSelectedLabels(ArrayList<String> selectedLabels) {
		GlobalDetailsHolder.INSTANCE.labels.forEach(label -> {
			label.setSelected(selectedLabels.contains(label.getText()));
		});
	}
	
	public static ArrayList<String> getSelectedLabels() {
		ArrayList<String> selectedLabels = new ArrayList<String>();
		GlobalDetailsHolder.INSTANCE.labels.forEach(label -> {
			if (label.isSelected()) selectedLabels.add(label.getText());
		});
		return selectedLabels;
	}
	
	public static FilteredList<CheckBox> getLabels() {
		return GlobalDetailsHolder.INSTANCE.filteredLabels;
	}
	
	public static void onlySelectedLabels(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filteredLabels.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	public static void setProductionTypes(ArrayList<String> productionTypes) {
		productionTypesSource.addAll(productionTypes);
	}
	
	public static ObservableList<String> getProductionTypesSource() {
		return productionTypesSource;
	}
	
	public static SortedList<String> getProductionTypes() {
		return GlobalDetailsHolder.INSTANCE.sortedProductionTypes;
	}
	
	public static void setCountriesOfManufacture(ArrayList<String> countries_of_manufacture) {
		countries_of_manufacture_source.addAll(countries_of_manufacture);
	}
	
	public static ObservableList<String> getCountriesOfManufactureSource() {
		return countries_of_manufacture_source;
	}
	
	public static void setSelectedCountriesOfManufacture(ArrayList<String> selectedCountriesOfManufacture) {
		GlobalDetailsHolder.INSTANCE.countries_of_manufacture.forEach(country_of_manufacture -> {
			country_of_manufacture.setSelected(selectedCountriesOfManufacture.contains(country_of_manufacture.getText()));
		});
	}
	
	public static ArrayList<String> getSelectedCountriesOfManufacture() {
		ArrayList<String> selectedCountriesOfManufacture = new ArrayList<String>();
		GlobalDetailsHolder.INSTANCE.countries_of_manufacture.forEach(country_of_manufacture -> {
			if (country_of_manufacture.isSelected()) selectedCountriesOfManufacture.add(country_of_manufacture.getText());
		});
		return selectedCountriesOfManufacture;
	}
	
	public static FilteredList<CheckBox> getCountryOfManufacture() {
		return GlobalDetailsHolder.INSTANCE.filtered_countries_of_manufacture;
	}
	
	public static void onlySelectedCountryOfManufacture(Boolean onlyChecked) {
		GlobalDetailsHolder.INSTANCE.filtered_countries_of_manufacture.setPredicate(o1 -> {
			if (!onlyChecked) return true;
			else return o1.isSelected();
		});
	}
	
	private static class GlobalDetailsHolder {
		private static GlobalDetails INSTANCE = new GlobalDetails();
	}
	
}