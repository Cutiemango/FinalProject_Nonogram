package me.Cutiemango.Nonogram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LevelController implements Initializable
{
	private static final HashMap<String, Nonogram> levelMap = new HashMap<>();
	private FXMLLoader gameFXML;

	public void startLevel(String levelID) {
		if (levelMap.containsKey(levelID)) {
			Nonogram level = levelMap.get(levelID);
			try {
				Parent game = gameFXML.load();

				GridController controller = gameFXML.getController();
				controller.startLevel(level);

				GameManager.startNewGame(level);

				Scene gameScene = new Scene(game, 1200, 900);
				gameScene.getRoot().requestFocus();
				GameLauncher.setScene(gameScene);

				System.out.println("[LevelController] Trying to switch to game...");
			}
			catch (IOException e) {
				System.out.println("[LevelController] Error while trying to switch to game");
				e.printStackTrace();
			}
		}
	}

	private void loadNonogram() {
		try {
			InputStream listOfLevels = Main.getResourceAsStream("/assets/level/levels.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(listOfLevels));
			String levelName;
			while ((levelName = reader.readLine()) != null) {
				Nonogram nonogram = readNonogramFromStream(Main.getResourceAsStream("/assets/level/" + levelName + ".txt"));
				Image image = new Image(Main.getResource("/assets/level/" + levelName + ".png").toString());
				nonogram.setImage(image);
				levelMap.put(levelName, nonogram);
				System.out.println("[LevelController] Successfully read nonogram: " + levelName);
			}
		} catch (IOException e) {
			System.out.println("[LevelController] An error occurred while reading nonogram data...");
			e.printStackTrace();
		}
	}

	private Nonogram readNonogramFromStream(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		// the first line is the size
		int size = Integer.parseInt(reader.readLine());
		boolean[][] map = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			String[] row = reader.readLine().split(" ");
			for (int j = 0; j < size; j++) {
				map[i][j] = row[j].equals("1");
			}
		}
		return new Nonogram(size, map);
	}

	@FXML
	public void onClick() {
		startLevel("odoka");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		gameFXML = new FXMLLoader(Main.getResource("/assets/Game.fxml"));

		loadNonogram();
	}
}
