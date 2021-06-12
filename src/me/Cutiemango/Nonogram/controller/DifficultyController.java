package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import me.Cutiemango.Nonogram.Vector2D;

import java.net.URL;
import java.util.ResourceBundle;

public class DifficultyController implements Initializable
{
	private final Vector2D pivot = new Vector2D(580, 570);

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

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		arrow.getTransforms().add(new Rotate(0, 0, 50));
	}
}
