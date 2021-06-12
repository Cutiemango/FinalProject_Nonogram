package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import me.Cutiemango.Nonogram.Difficulty;
import me.Cutiemango.Nonogram.GameLauncher;
import me.Cutiemango.Nonogram.GameManager;
import me.Cutiemango.Nonogram.Main;
import me.Cutiemango.Nonogram.Nonogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class LevelController implements Initializable
{
	private final Pair<Integer, Integer>[] LAYOUT_4 = new Pair[] { new Pair<>(275, 275), new Pair<>(275, 600), new Pair<>(700, 275),
			new Pair<>(700, 600) };
	private final Pair<Integer, Integer>[] LAYOUT_2 = new Pair[] { new Pair<>(275, 400), new Pair<>(700, 400) };

	private Image unsolved_level;
	private FXMLLoader difficultyFXML;
	private FXMLLoader gameFXML;
	private Parent difficultySelection;
	private Parent game;

	@FXML
	private AnchorPane backgroundPane;

	@FXML
	private ImageView background;

	public void startLevel(String levelID) {
		GameManager.getLevel(levelID).ifPresent(level -> {
			try {
				game = gameFXML.load();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			GridController controller = gameFXML.getController();
			controller.startLevel(level);

			GameManager.startNewGame(level);

			Scene gameScene = new Scene(game);
			gameScene.getRoot().requestFocus();
			GameLauncher.setScene(gameScene);

			System.out.println("[LevelController] Trying to switch to game...");
		});
	}

	public void chooseDifficulty(Difficulty difficulty) {
		background.setImage(new Image(Main.getResource("/assets/background/difficulty_" + difficulty.toString().toLowerCase() + ".png").toString()));

		switch (difficulty) {
			case EASY:
				setupLevels(LAYOUT_4, GameManager.EASY_LEVELS);
				break;
			case NORMAL:
				setupLevels(LAYOUT_4, GameManager.NORMAL_LEVELS);
				break;
			case HARD:
				setupLevels(LAYOUT_2, GameManager.HARD_LEVELS);
				break;
		}
	}

	private void setupLevels(Pair<Integer, Integer>[] levelPos, List<Nonogram> levels) {
		for (int i = 0; i < levelPos.length; i++) {
			Pair<Integer, Integer> pos = levelPos[i];
			Nonogram level = levels.get(i);

			ImageView levelBlock = new ImageView(unsolved_level);
			levelBlock.setFitHeight(200);
			levelBlock.setFitWidth(200);
			levelBlock.setLayoutX(pos.getKey());
			levelBlock.setLayoutY(pos.getValue());

			ImageView levelImage = new ImageView(level.getImage());
			levelImage.setFitHeight(170);
			levelImage.setFitWidth(170);
			levelImage.setLayoutX(pos.getKey() + 15);
			levelImage.setLayoutY(pos.getValue() + 15);
			levelImage.setOnMouseClicked(e -> startLevel(level.getID()));
			levelImage.setVisible(GameManager.hasFinishedLevel(level.getID()));

			backgroundPane.getChildren().add(levelBlock);
			backgroundPane.getChildren().add(levelImage);
		}
	}

	@FXML
	private void onClickBack() {
		try {
			difficultySelection = difficultyFXML.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene difficultyScene = new Scene(difficultySelection);
		difficultyScene.getRoot().requestFocus();
		GameLauncher.setScene(difficultyScene);

		System.out.println("[LevelController] Going back to difficulty selection...");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		difficultyFXML = new FXMLLoader(Main.getResource("/assets/DifficultySelection.fxml"));
		gameFXML = new FXMLLoader(Main.getResource("/assets/Game.fxml"));

		unsolved_level = new Image(Main.getResource("/assets/background/unsolved_level.png").toString());
	}
}
