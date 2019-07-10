package application;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class ModifierController {

	/**
	 * Enum for delay and speed up constants.
	 */
	public enum DelaySpeedUp {DELAY, SPEED_UP}
	
	/**
	 * Choice box that allows selection between delaying and speeding up.
	 */
	@FXML private ChoiceBox<String> delaySpeedUpSelector; 
	
	/**
	 * The text fields holding the amount of seconds and miliseconds to delay/speed up with.
	 */
	@FXML private TextField secondsField, milisecondsField;
	
	/**
	 * The button that performs the updating of the file when clicked.
	 */
	@FXML private Button updateButton;
	
	@FXML public void initialize() {
		delaySpeedUpSelector.setItems(FXCollections.observableArrayList("Delay","Speed up"));
		delaySpeedUpSelector.setValue("Delay");
		delaySpeedUpSelector.setTooltip(new Tooltip("Delaying or speeding up is needed"));
	}
	
	/**
	 * Fires when the {@link #updateButton} is clicked.
	 */
	@FXML public void updateButtonClicked() {
		Alert msg = new Alert(AlertType.ERROR);
		if(SelectorController.getSelectedFile() == null) { //no selected file
			msg.setContentText("Select an SRT subtitle file first!");
			msg.showAndWait();
			return;
		}
		int seconds, miliseconds;
		try {
			seconds = Integer.parseInt(secondsField.getText());
		} catch(NumberFormatException e) { //invalid seconds input
			secondsField.setStyle("-fx-background-color: red");
			msg.setContentText("Invalid amount of seconds!");
			msg.showAndWait();
			return;
		}
		try {
			miliseconds = Integer.parseInt(milisecondsField.getText());
		} catch(NumberFormatException e) { //invalid seconds input
			milisecondsField.setStyle("-fx-background-color: red");
			msg.setContentText("Invalid amount of miliseconds!");
			msg.showAndWait();
			return;
		}
		secondsField.setStyle("-fx-background-color: white"); //correct input, make backgrounds white
		milisecondsField.setStyle("-fx-background-color: white");
		try {
			boolean completeSucess = new FileModifier(SelectorController.getSelectedFile(), seconds, miliseconds, delayOrSpeedUp()).replaceTimestamps();
			displayFinalMessage(completeSucess); //if completeSucess is false, that means some timestamps were poorly formatted, and could not be changed.
		} catch(IOException e) {
			msg.setContentText("Could not read from or write to selected file!");
			msg.showAndWait();
			return;
		}
	}
	
	/**
	 * Displays the result of the subtitle modification.
	 */
	private void displayFinalMessage(boolean sucess) {
		if(sucess) {
			Alert info = new Alert(AlertType.INFORMATION);
			info.setContentText("Sucessfully changed SRT file!");
			info.showAndWait();
		} else {
			Alert warning = new Alert(AlertType.WARNING);
			warning.setHeaderText("Partial sucess!");
			warning.setContentText("Some timestamps were poorly formatted, and could not be changed.");
			warning.showAndWait();
		}
	}
	
	/**
	 * @return The enum constant of the selected value of {@link #delaySpeedUpSelector}.
	 */
	private DelaySpeedUp delayOrSpeedUp() {
		return delaySpeedUpSelector.getSelectionModel().getSelectedItem().equals("Delay") ? DelaySpeedUp.DELAY : DelaySpeedUp.SPEED_UP;
	}
}
