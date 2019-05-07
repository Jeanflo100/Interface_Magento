package ecofish.interface_magento.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FamilyService {

	private ObservableList<String> family;
	
	private FamilyService() {
		family = FXCollections.observableArrayList();
		/*family.add("Orange");
		family.add("Citron");
		family.add("Pomelos");
		family.add("Mandarine");*/
	}
	
	public static ObservableList<String> getFamily(String category){
		// Requête SQL afin d'obtenir les différentes familles de cette catégorie
		FamilyServiceHolder.INSTANCE.family.clear();
		if (category == "Agrumes") {
			FamilyServiceHolder.INSTANCE.family.add("Orange");
			FamilyServiceHolder.INSTANCE.family.add("Citron");
			FamilyServiceHolder.INSTANCE.family.add("Pomelos");
		}
		else if (category == "Fruits à noyau") {
			FamilyServiceHolder.INSTANCE.family.add("Mandarine");
		}
		return FamilyServiceHolder.INSTANCE.family;
	}
	
	private static class FamilyServiceHolder {
		private static FamilyService INSTANCE = new FamilyService();
	}
	
}
