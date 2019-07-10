package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Main class of the application.
 * 
 * @author Gáspár Tamás
 */
public class Main extends Application {
	
	private static final int WINDOW_SIZE = 500;
	
	public static Stage primaryStage;
	
	@FXML private Button exitButton;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = createAppPane();
			Scene scene = new Scene(root,1.5*WINDOW_SIZE,WINDOW_SIZE);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("SRT Subtitle adjuster");
			Main.primaryStage = primaryStage;
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return The application pane.
	 */
	private BorderPane createAppPane() {
		BorderPane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource("/application/AdjusterPane.fxml"));
			
			FlowPane mainInterface = FXMLLoader.load(getClass().getResource("/application/MainInterface.fxml"));
			mainInterface.getChildren().add(FXMLLoader.load(getClass().getResource("/application/SelectorPane.fxml"))); //add selector menu
			mainInterface.getChildren().add(FXMLLoader.load(getClass().getResource("/application/ModifierPane.fxml"))); //add modifier menu
			pane.setCenter(mainInterface);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pane;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Terminates the application.
	 */
	@FXML public void exitApp() {
		Platform.exit();
	}
	
	@Override
	public void stop() { }
}
