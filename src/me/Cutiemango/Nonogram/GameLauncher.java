package me.Cutiemango.Nonogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
		Parent root = FXMLLoader.load(Main.getResource("/assets/LevelSelection.fxml"));
		menuScene = new Scene(root, 600, 400);

		currentStage.setTitle("Nonogram");
		currentStage.setScene(menuScene);
		currentStage.show();
	}

	public static void setScene(Scene scene) {
		currentStage.setScene(scene);
	}
}
