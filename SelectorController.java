package application;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

/**
 * Controls the selector interface.
 * 
 * @author Gáspár Tamás
 */
public class SelectorController {

	private final FileChooser fileChooser = new FileChooser();
	
	/**
	 * The SRT file currently selected. null if no file is selected.
	 */
	private static File selectedFile;
	
	/**
	 * Label that shows the path of the selected srt subtitle file.
	 */
	@FXML private Label selectedFileLabel;
	
	@FXML public void initialize() {
		fileChooser.setTitle("Select SRT file");
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("SRT Subtitle files", "*.srt");
		fileChooser.getExtensionFilters().add(filter); //filter for srt files only
	}
	
	/**
	 * Opens up a file selector and modifies the displayed path of the file.
	 */
	@FXML public void selectButtonPressed() {
		selectedFile = fileChooser.showOpenDialog(Main.primaryStage);
		if(selectedFile != null) {
			selectedFileLabel.setText(selectedFile.getAbsolutePath());
		}
	}

	public static File getSelectedFile() {
		return selectedFile;
	}
}
