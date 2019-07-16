package ecofish.interface_magento.view;

import java.util.Optional;

import ecofish.interface_magento.service.GlobalDetails;
import ecofish.interface_magento.service.StageService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class ModificationChoicesDetailsProductController {
	
	@FXML
	AnchorPane backgroundAnchorPane;
	
	@FXML
	Label characteristicLabel;
	
	@FXML
	TextField addChoiceTextField;
	
	@FXML
	ComboBox<String> deleteChoiceComboBox;
	
	private static StringProperty characteristic = new SimpleStringProperty();
	
	private static ObservableList<String> choices;
	
	public static void setCharacteristic(String characteristicModified) {
		characteristic.set(characteristicModified);
	}
	
	public static void setChoices(ObservableList<String> choicesModified) {
		choices = choicesModified;
	}
	
	@FXML
	private void initialize() {
		this.backgroundAnchorPane.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ESCAPE) {
				closeWindow();
			}
		});
		this.characteristicLabel.textProperty().bind(characteristic);
		this.deleteChoiceComboBox.setItems(choices);
	}
	
	@FXML
	private void addChoice() {
		if (!this.addChoiceTextField.getText().isEmpty()) {
			for (String choice : choices) {
				if (choice.toLowerCase().equals(this.addChoiceTextField.getText().toLowerCase())) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.initOwner(StageService.getPrimaryStage());
					alert.setHeaderText("This choice already exists");
					alert.showAndWait();
					return;
				}
			}
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.initOwner(StageService.getPrimaryStage());
			alert.setHeaderText("Are you sure you want to add '" + this.addChoiceTextField.getText() + "' as a choice for the '" + characteristic.getValue() + "' feature?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() != ButtonType.OK) {
				return;
	    	}
			// requête SQL ajout caracteristique
			choices.add(this.addChoiceTextField.getText());
			this.addChoiceTextField.clear();
			
		}
	}
	
	@FXML
	private void deleteChoice() {
		if (!this.deleteChoiceComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.initOwner(StageService.getPrimaryStage());
			alert.setHeaderText("Are you sure you want to delete '" + this.addChoiceTextField.getText() + "'from the choices for the '" + characteristic.getValue() + "' feature?");
			Optional<ButtonType> option = alert.showAndWait();
			if (option.get() != ButtonType.OK) {
				return;
	    	}
			// requête SQL supprimer caracteristique
			choices.remove(this.deleteChoiceComboBox.getSelectionModel().getSelectedItem());
			this.deleteChoiceComboBox.getSelectionModel().clearSelection();
		}
	}
	
	@FXML
	private void closeWindow() {
		this.addChoiceTextField.clear();
		this.deleteChoiceComboBox.getSelectionModel().clearSelection();
		StageService.closeSecondaryStage();
	}
	
}