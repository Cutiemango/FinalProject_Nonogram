package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import me.Cutiemango.Nonogram.Difficulty;
import me.Cutiemango.Nonogram.GameLauncher;
import me.Cutiemango.Nonogram.Main;
import me.Cutiemango.Nonogram.Vector2D;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DifficultyController implements Initializable
{
	private final Vector2D pivot = new Vector2D(580, 570);

	private FXMLLoader levelFXML;
	private FXMLLoader menuFXML;
	private Parent levelSelection;
	private Parent menu;

	@FXML
	private ImageView arrow;

	@FXML
	private void onMouseMove(MouseEvent e) {
		rotate(e.getSceneX(), e.getSceneY());
		arrow.setLayoutX(pivot.x);
		arrow.setLayoutY(pivot.y);
	}

	private void rotate(double mouseX, double mouseY) {
		// create a new coordinate system that is centered at the pivot
		Vector2D vec = new Vector2D(mouseX, mouseY - 50).subtract(pivot);
		vec.normalize();

		double degree = vec.getAngle();
		Rotate rotation = (Rotate) arrow.getTransforms().get(0);
		if (degree < 175 && degree > 5)
			return;
		rotation.setAngle(degree);
	}

	@FXML
	private void onMouseClicked(MouseEvent e) {
		double mouseX = e.getSceneX(), mouseY = e.getSceneY();
		Vector2D vec = new Vector2D(mouseX, mouseY - 50).subtract(pivot);
		vec.normalize();
		double degree = vec.getAngle();
		if (degree < 175 && degree > 5)
			return;

		try {
			levelSelection = levelFXML.load();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}

		LevelController controller = levelFXML.getController();
		if (degree > -60 && degree <= 5)
			controller.chooseDifficulty(Difficulty.HARD);
		else if (degree <= -60 && degree > -120)
			controller.chooseDifficulty(Difficulty.NORMAL);
		else
			controller.chooseDifficulty(Difficulty.EASY);

		Scene levelScene = new Scene(levelSelection);
		levelScene.getRoot().requestFocus();
		GameLauncher.setScene(levelScene);
		System.out.println("[DifficultyController] Switching to level selection...");
	}

	@FXML
	private void onClickBack() {
		try {
			menu = menuFXML.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene difficultyScene = new Scene(menu);
		difficultyScene.getRoot().requestFocus();
		GameLauncher.setScene(difficultyScene);

		System.out.println("[LevelController] Going back to difficulty selection...");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		levelFXML = new FXMLLoader(Main.getResource("/assets/LevelSelection.fxml"));
		menuFXML = new FXMLLoader(Main.getResource("/assets/Menu.fxml"));

		arrow.getTransforms().add(new Rotate(0, 0, 50));
	}
}
