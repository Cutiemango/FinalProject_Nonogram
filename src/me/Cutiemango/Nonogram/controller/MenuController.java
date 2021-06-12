package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import me.Cutiemango.Nonogram.GameLauncher;
import me.Cutiemango.Nonogram.Main;

import java.io.IOException;

public class MenuController
{
	@FXML
	private ImageView startArrow;

	@FXML
	private ImageView exitArrow;

	@FXML
	private void selectLevel() {
		try {
			Parent difficultySelection = FXMLLoader.load(Main.getResource("/assets/DifficultySelection.fxml"));
			Scene scene = new Scene(difficultySelection);
			scene.getRoot().requestFocus();

			GameLauncher.setScene(scene);
		} catch (IOException e) {
			System.out.println("[MenuController] An error occurred while switching to difficulty selection!");
			e.printStackTrace();
		}
	}

	@FXML
	private void exit() {
		GameLauncher.exit();
	}
}
