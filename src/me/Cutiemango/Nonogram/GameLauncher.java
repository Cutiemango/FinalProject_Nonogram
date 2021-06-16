package me.Cutiemango.Nonogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EnumMap;

public class GameLauncher extends Application
{
	public static void main(String[] args) {
		launch(args);
	}

	private static Stage currentStage;

	private static final EnumMap<GameScene, Scene> sceneMap = new EnumMap<>(GameScene.class);
	private static final EnumMap<GameScene, FXMLLoader> fxmlMap = new EnumMap<>(GameScene.class);

	@Override
	public void start(Stage stage) {
		GameManager.loadNonogram();
		currentStage = stage;
		currentStage.setTitle("Nonogram");

		transitionTo(GameScene.MENU);
		currentStage.show();
	}

	public static <T> T transitionTo(GameScene sceneType) {
		// lazy load
		if (!sceneMap.containsKey(sceneType)) {
			FXMLLoader loader = new FXMLLoader(Main.getResource(sceneType.getFxmlLocation()));
			try {
				Parent root = loader.load();
				sceneMap.put(sceneType, new Scene(root, 1200, 900));
				fxmlMap.put(sceneType, loader);
			} catch (IOException e) {
				System.out.println("[Launcher] An error occurred while loading FXML...");
				e.printStackTrace();
			}
		}
		Scene scene = sceneMap.get(sceneType);
		scene.getRoot().requestFocus();
		currentStage.setScene(scene);
		return fxmlMap.get(sceneType).getController();
	}

	public static void exit() {
		currentStage.close();
	}
}
