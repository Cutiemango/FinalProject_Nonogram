package FinalProject;

import javafx.util.Pair;

import java.util.LinkedList;

public class GameManager
{
	private static final int MAX_HEALTH = 5;

	private static Nonogram currentLevel;
	private static int currentHealth;
	private static boolean[][] currentPanes;

	public static void startNewGame(Nonogram level) {
		currentLevel = level;
		currentHealth = MAX_HEALTH;

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
			}
		}
	}

	public static boolean hasSelected(int x, int y) {
		return currentPanes[x][y];
	}

	private static void checkGameOver() {
		if (currentHealth <= 0) {

		}
	}
}
