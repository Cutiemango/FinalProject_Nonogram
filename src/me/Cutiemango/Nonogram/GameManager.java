package me.Cutiemango.Nonogram;

import javafx.scene.image.Image;
import javafx.util.Pair;
import me.Cutiemango.Nonogram.controller.GridController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class GameManager
{
	private static final int MAX_HEALTH = 5;

	public static final List<Nonogram> EASY_LEVELS = new ArrayList<>();
	public static final List<Nonogram> NORMAL_LEVELS = new ArrayList<>();
	public static final List<Nonogram> HARD_LEVELS = new ArrayList<>();

	private static final HashMap<String, Nonogram> levelMap = new HashMap<>();
	private static Nonogram currentLevel;
	private static int currentHealth;
	private static int remainingPanes;
	private static boolean[][] currentPanes;

	public static void startNewGame(Nonogram level) {
		currentLevel = level;
		currentHealth = MAX_HEALTH;
		remainingPanes = level.getTotalPanes();

		int size = level.getSize();
		currentPanes = new boolean[size][size];
	}

	public static void selectPanes(LinkedList<Pair<Integer, Integer>> selectedPanes) {
		boolean fail = false;
		for (Pair<Integer, Integer> coord : selectedPanes) {
			int x = coord.getKey(), y = coord.getValue();
			if (currentPanes[x][y])
				continue;
			if (!currentLevel.isValid(x, y)) {
				// a pane is wrong!
				fail = true;
				currentPanes[x][y] = true;
				GridController.setWrongColor(x, y);
				System.out.println("[Game] Selected wrong pane! HP - 1!");
				currentHealth -= 1;
				checkGameOver();
				break;
			}
		}

		for (Pair<Integer, Integer> coord : selectedPanes) {
			int x = coord.getKey(), y = coord.getValue();
			if (currentPanes[x][y])
				continue;

			if (fail) {
				GridController.setNormalColor(x, y);
			} else {
				GridController.reveal(x, y);
				currentPanes[x][y] = true;
				remainingPanes--;
			}
		}

		checkGameFinished();
	}

	public static boolean hasSelected(int x, int y) {
		return currentPanes[x][y];
	}

	public static boolean hasFinishedLevel(String id) {
		// TODO
		return true;
	}

	private static void checkGameFinished() {
		if (remainingPanes == 0) {
			System.out.println("[Game] Congratulations! You've finished the puzzle!");
		} else if (remainingPanes < 5) {
			System.out.printf("[Game] %d pieces remaining.\n", remainingPanes);
		}
	}

	public static Optional<Nonogram> getLevel(String id) {
		return Optional.ofNullable(levelMap.get(id));
	}

	private static void checkGameOver() {
		if (currentHealth <= 0) {

		}
	}

	public static void loadNonogram() {
		try {
			InputStream listOfLevels = Main.getResourceAsStream("/assets/level/levels.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(listOfLevels));
			String levelName;
			while ((levelName = reader.readLine()) != null) {
				Nonogram nonogram = readNonogramFromStream(levelName, Main.getResourceAsStream("/assets/level/" + levelName + ".txt"));
				switch (nonogram.getSize()) {
					case 5:
						EASY_LEVELS.add(nonogram);
						break;
					case 10:
						NORMAL_LEVELS.add(nonogram);
						break;
					case 15:
						HARD_LEVELS.add(nonogram);
						break;
				}
				Image image = new Image(Main.getResource("/assets/level/" + levelName + ".png").toString());
				nonogram.setImage(image);
				levelMap.put(levelName, nonogram);
				System.out.println("[LevelController] Successfully read nonogram: " + levelName);
			}
		}
		catch (IOException e) {
			System.out.println("[LevelController] An error occurred while reading nonogram data...");
			e.printStackTrace();
		}
	}

	private static Nonogram readNonogramFromStream(String levelName, InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		// the first line is the size
		int size = Integer.parseInt(reader.readLine());
		int total = 0;
		boolean[][] map = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			String[] row = reader.readLine().split(" ");
			for (int j = 0; j < size; j++) {
				if (row[j].equals("1")) {
					map[i][j] = true;
					total++;
				}
			}
		}
		return new Nonogram(levelName, size, total, map);
	}
}
