package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import me.Cutiemango.Nonogram.GameLauncher;
import me.Cutiemango.Nonogram.GameManager;
import me.Cutiemango.Nonogram.GameScene;
import me.Cutiemango.Nonogram.Nonogram;

public class GameFinishController
{
	@FXML
	private ImageView image;

	@FXML
	private void onClickBack() {
		LevelController controller = GameLauncher.transitionTo(GameScene.LEVEL_SELECTION);
		controller.resetNodes();
		controller.chooseDifficulty(GameManager.SELECTED_DIFFICULTY);
	}

	public void setFinishedLevel(Nonogram level) {
		image.setImage(level.getImage());
	}
}
