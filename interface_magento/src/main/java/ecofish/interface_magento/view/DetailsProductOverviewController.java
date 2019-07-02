package ecofish.interface_magento.view;

import java.util.Calendar;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DetailsProductOverviewController {
	
	@FXML
	Label currentSeasonLabel;
	
	@FXML
	GridPane testGridPane;
	
	@FXML
	GridPane seasonGridPane;
    
    private static final PseudoClass currentSeason = PseudoClass.getPseudoClass("current");
    private static final PseudoClass highSeason = PseudoClass.getPseudoClass("high");
    private static final PseudoClass mediumSeason = PseudoClass.getPseudoClass("medium");
    private static final PseudoClass lowSeason = PseudoClass.getPseudoClass("low");
	
    private PseudoClass[] seasons = new PseudoClass[12];
    
    private void test() {
    	seasons[0] = seasons[1] = seasons[2] = seasons[10] = seasons[11] = lowSeason;
    	seasons[3] = seasons[4] = seasons[9] = mediumSeason;
    	seasons[5] = seasons[6] = seasons[7] = seasons[8] = highSeason;
    }
    
	@FXML
	private void initialize() {
		test();
		
		initSeasons();
	}
	
	/*private void initSeasons() {
		for (String month : high.split("/")) {
			
		}
	}*/
	
	private void initSeasons() {
		Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		Integer month = 0;
		for (Node node : seasonGridPane.getChildren()) {
			if (month < 12) {
				node.pseudoClassStateChanged(seasons[month], true);
				if (month == currentMonth) {
					node.pseudoClassStateChanged(currentSeason, true);
					if (seasons[currentMonth].equals(highSeason)) currentSeasonLabel.setText("High");
					else if (seasons[currentMonth].equals(mediumSeason)) currentSeasonLabel.setText("Medium");
					else if (seasons[currentMonth].equals(lowSeason)) currentSeasonLabel.setText("Low");
				}
				month++;
			}
		}
	}
	
}