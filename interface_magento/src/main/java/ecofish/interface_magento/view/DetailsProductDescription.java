package ecofish.interface_magento.view;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;

import ecofish.interface_magento.log.Logging;
import ecofish.interface_magento.model.DetailedProduct;
import ecofish.interface_magento.service.StageService;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class DetailsProductDescription implements DetailsProductInterface {

	private final DetailedProduct detailedProduct;
	
	private final  AnchorPane descriptionModificationAnchorPane;
	private final TextArea shortDescriptionTextArea;
	private final ImageView imageImage;
	private final TextArea descriptionTextArea;
	private final TextField latinNameTextField;
	
	private String url_image;
	
	public DetailsProductDescription(DetailedProduct detailedProduct, AnchorPane descriptionModificationAnchorPane, TextArea shortDescriptionTextArea, ImageView imageImage, TextArea descriptionTextArea, TextField latinNameTextField) {
		this.detailedProduct = detailedProduct;
		this.descriptionModificationAnchorPane = descriptionModificationAnchorPane;
		this.shortDescriptionTextArea = shortDescriptionTextArea;
		this.imageImage = imageImage;
		this.descriptionTextArea = descriptionTextArea;
		this.latinNameTextField = latinNameTextField;
		
		modificationDetails(false, false);
	}

	public void setContentComponents() {
		this.detailedProduct.setNewShortDescription(null);
		this.detailedProduct.setNewDescription(null);
		this.detailedProduct.setNewUrlImage(null);
		this.detailedProduct.setNewLatinName(null);
		
		this.shortDescriptionTextArea.setText(detailedProduct.getShortDescription());
		this.url_image = this.detailedProduct.getUrlImage();
		if (this.url_image == null) this.imageImage.setImage(null);
		else this.imageImage.setImage(new Image(url_image));
		this.descriptionTextArea.setText(detailedProduct.getDescription());
		this.latinNameTextField.setText(detailedProduct.getLatinName());
	}

	protected void changeImage() {
		File imageFile = imageFile();
		if (imageFile != null) {
			try {
				this.url_image = imageFile.toURI().toURL().toString();
				this.imageImage.setImage(new Image(this.url_image));
			} catch (MalformedURLException e) {
				Logging.getLogger().log(Level.WARNING, "Error when retrieving the image url:\n" + e.getMessage());
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setHeaderText("Error when retrieving the image url");
				alert.setContentText("Retry the action or restart the application if the problem persists");
				alert.showAndWait();
			}
		}
	}
	
	private File imageFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select the product image");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ALL", "*.jpg", "*.png"), new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		File file = fileChooser.showOpenDialog(StageService.getPrimaryStage());
        return file;
	}
	
	private void saveModification() {
		this.detailedProduct.setNewShortDescription(this.shortDescriptionTextArea.getText());
		this.detailedProduct.setNewDescription(this.descriptionTextArea.getText());
		this.detailedProduct.setNewUrlImage(url_image);
		this.detailedProduct.setNewLatinName(this.latinNameTextField.getText());
	}
	
	@Override
	public void modificationDetails(Boolean isModification, Boolean isSave) {
		this.descriptionModificationAnchorPane.setVisible(isModification);
		this.shortDescriptionTextArea.pseudoClassStateChanged(unmodifiable, !isModification);
		this.descriptionTextArea.pseudoClassStateChanged(unmodifiable, !isModification);
		this.latinNameTextField.pseudoClassStateChanged(unmodifiable, !isModification);
		this.shortDescriptionTextArea.setEditable(isModification);
		this.descriptionTextArea.setEditable(isModification);
		this.latinNameTextField.setEditable(isModification);
		this.imageImage.setDisable(!isModification);
		if (isSave != null) {
			if (isSave) saveModification();
			else if (!isSave) setContentComponents();
		}
	}
	
}