package me.Cutiemango.Nonogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLauncher extends Application
{
	public static void main(String[] args) {
		launch(args);
	}

	private static Stage currentStage;
	private static Scene menuScene;

	@Override
	public void start(Stage stage) throws Exception {
		currentStage = stage;
		Parent root = FXMLLoader.load(Main.getResource("/assets/Menu.fxml"));
		menuScene = new Scene(root, 1200, 900);

		GameManager.loadNonogram();

		currentStage.setTitle("Nonogram");
		currentStage.setScene(menuScene);
		currentStage.show();
	}

	public static void setScene(Scene scene) {
		currentStage.setScene(scene);
	}

	public static void exit() {
		currentStage.close();
	}
}
