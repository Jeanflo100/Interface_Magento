package ecofish.interface_magento.daos;

import java.util.ArrayList;

import ecofish.interface_magento.service.GlobalDetails;

public class GettingGlobalDetailsThread {
	
	public static void init() {
		ArrayList<String> productionTypes = new ArrayList<String>();
		productionTypes.add("Elevage");
		productionTypes.add("Sauvage");
		GlobalDetails.setProductionTypes(productionTypes);
	}
	
}