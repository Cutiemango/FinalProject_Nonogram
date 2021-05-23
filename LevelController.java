package finalProject;

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
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
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
				Launcher.setScene(gameScene);

				System.out.println("[LevelController] Trying to switch to game...");
			} catch (IOException e) {
				System.out.println("[LevelController] Error while trying to switch to game");
				e.printStackTrace();
			}
		}
	}

	private void loadNonogram() {
		File assetDir = new File(getClass().getResource("./assets/level/").getPath());
		for (File file : assetDir.listFiles()) {
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			String name = fileName.substring(0, i);
			if (i > 0 && fileName.substring(i + 1).equals("txt")) {
				// file extension is txt
				try {
					Optional<Nonogram> result = readNonogramFromFile(file);
					if (result.isPresent()) {
						Image image = new Image(file.toURI().toString().replace(".txt", ".png"));
						Nonogram nonogram = result.get();
						nonogram.setImage(image);
						levelMap.put(name, nonogram);
						System.out.println("[LevelController] Successfully read nonogram: " + name);
					}
				}
				catch (IOException e) {
					System.out.println("[LevelController] Error while reading nonogram: " + name);
					e.printStackTrace();
				}
			}
		}
	}

	private Optional<Nonogram> readNonogramFromFile(File txtFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(txtFile));
		// the first line is the size
		int size = Integer.parseInt(reader.readLine());
		boolean[][] map = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			String[] row = reader.readLine().split(" ");
			for (int j = 0; j < size; j++) {
				map[i][j] = row[j].equals("1");
			}
		}
		return Optional.of(new Nonogram(size, map));
	}

	@FXML
	public void onClick() {
		startLevel("odoka");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		gameFXML = new FXMLLoader(getClass().getResource("assets/Game.fxml"));

		loadNonogram();
	}
}
