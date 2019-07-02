package ecofish.interface_magento.view;

import java.util.ArrayList;
import java.util.Hashtable;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class DetailsProductOverviewController {
	
	@FXML
	GridPane testGridPane;
	
	@FXML
	GridPane seasonGridPane;
	
    private final static String highSeason = "-fx-background-color: #76C778";
    private final static String mediumSeason = "-fx-background-color: #EAA471";
    private final static String lowSeason = "-fx-background-color: #FFE655";
	
    private String[] seasons_test = new String[12];
    private Hashtable<String, ArrayList<Integer>> seasons;
    private String high = "1/2/3/4/11/12";
    private String med = "5/6/7/10";
    private String low = "8/9";
    
	@FXML
	private void initialize() {
		initSeasons();
	}
	
	/*private void initSeasons() {
		for (String month : high.split("/")) {
			
		}
	}*/
	
	private void initSeasons() {
		Integer nb_month = 1;
		for (Node node : seasonGridPane.getChildren()) {
			node.setStyle("-fx-background-color: #EAA471");
			nb_month++;
		}
	}
	
}