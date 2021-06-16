package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import me.Cutiemango.Nonogram.GameLauncher;
import me.Cutiemango.Nonogram.GameScene;

public class MenuController
{
	@FXML
	private void selectLevel() {
		GameLauncher.transitionTo(GameScene.DIFFICULTY_SELECTION);
	}

	@FXML
	private void exit() {
		GameLauncher.exit();
	}
}
