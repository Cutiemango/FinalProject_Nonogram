package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import me.Cutiemango.Nonogram.GameLauncher;
import me.Cutiemango.Nonogram.GameManager;
import me.Cutiemango.Nonogram.GameScene;

public class GameOverController
{
	@FXML
	private void onClickBack() {
		LevelController controller = GameLauncher.transitionTo(GameScene.LEVEL_SELECTION);
		controller.resetNodes();
		controller.chooseDifficulty(GameManager.SELECTED_DIFFICULTY);
	}
}
