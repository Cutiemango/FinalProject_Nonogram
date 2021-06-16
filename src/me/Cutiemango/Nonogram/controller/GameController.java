package me.Cutiemango.Nonogram.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;
import me.Cutiemango.Nonogram.DragHandler;
import me.Cutiemango.Nonogram.GameLauncher;
import me.Cutiemango.Nonogram.GameManager;
import me.Cutiemango.Nonogram.GameScene;
import me.Cutiemango.Nonogram.Main;
import me.Cutiemango.Nonogram.Nonogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class GameController
{
	private static final Color UNKNOWN_PANE_COLOR = Color.web("#FFFFFF");
	private static final Color HOVER_PANE_COLOR = Color.web("#8C8973");
	private static final Color WRONG_PANE_COLOR = Color.web("#FA4646");
	private static final Color BORDER_COLOR = Color.web("#222222");
	private static final Color FONT_COLOR = Color.web("#FFFFFF");

	private static final Font NUMBER_FONT = new Font("Cambria", 24);

	@FXML
	private AnchorPane backgroundPane;

	@FXML
	private GridPane grid_5;
	@FXML
	private GridPane grid_10;
	@FXML
	private GridPane grid_15;

	@FXML
	private ImageView image;

	@FXML
	private ImageView lever;

	private LeverState state = LeverState.O;
	private boolean isTransitioning = false;
	private final List<Node> addedNodes = new ArrayList<>();

	private static Pane[][] paneEntries;
	private static final List<Pair<Integer, Integer>> O_TO_X, X_TO_R;
	private static final List<ImageView> HEART_PENS = new ArrayList<>();

	static {
		O_TO_X = new ArrayList<>();
		O_TO_X.add(new Pair<>(950, 210));
		O_TO_X.add(new Pair<>(1000, 210));
		O_TO_X.add(new Pair<>(1000, 300));
		O_TO_X.add(new Pair<>(970, 320));

		X_TO_R = new ArrayList<>();
		X_TO_R.add(new Pair<>(950, 360));
		X_TO_R.add(new Pair<>(935, 360));
		X_TO_R.add(new Pair<>(935, 390));
		X_TO_R.add(new Pair<>(975, 390));
		X_TO_R.add(new Pair<>(975, 450));
	}

	private void initializePane(GridPane grid, int size) {
		paneEntries = new Pane[size][size];
		grid.setVisible(true);
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				Pane pane = new Pane();
				pane.setBackground(new Background(new BackgroundFill(UNKNOWN_PANE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
				pane.setOnMouseEntered(e -> onMouseEntered(pane));
				pane.setOnMouseExited(e -> onMouseExited(pane));

				DragHandler.makeDraggable(pane);
				grid.add(pane, col, row);
				paneEntries[row][col] = pane;
			}
		}
	}

	private void spawnHeartPens() {
		int posX = 770, posY = 700;
		Image heartPen =  new Image(Main.getResource("/assets/background/heartpen.png").toString());
		for (int i = 0; i < GameManager.MAX_HEALTH; i++) {
			ImageView heart = new ImageView(heartPen);
			heart.setLayoutX(posX + i * 80);
			heart.setLayoutY(posY);
			heart.setFitWidth(100);
			heart.setFitHeight(150);
			backgroundPane.getChildren().add(heart);
			HEART_PENS.add(heart);
		}
	}

	public static void removeHeartPen() {
		int index = GameManager.getCurrentHealth();
		HEART_PENS.get(index).setVisible(false);
	}

	private void drawLines(GridPane grid, int size) {
		double layoutX = grid.getLayoutX(), layoutY = grid.getLayoutY();
		int paneSize = (int) (grid.getPrefWidth() / size);
		// spawn vertical lines
		for (int i = 0; i <= size; i++) {
			Line line = new Line(0, -160, 0, grid.getPrefHeight());
			line.setLayoutX(layoutX + i * paneSize);
			line.setLayoutY(layoutY);
			line.setStroke(BORDER_COLOR);
			backgroundPane.getChildren().add(line);
			addedNodes.add(line);
		}

		// spawn horizontal lines
		for (int i = 0; i <= size; i++) {
			Line line = new Line(-160, 0, grid.getPrefWidth(), 0);
			line.setLayoutX(layoutX);
			line.setLayoutY(layoutY + i * paneSize);
			line.setStroke(BORDER_COLOR);
			backgroundPane.getChildren().add(line);
			addedNodes.add(line);
		}
	}

	private void generateText(Nonogram level, GridPane grid, int size) {
		double layoutX = grid.getLayoutX(), layoutY = grid.getLayoutY();
		int paneSize = (int) (grid.getPrefWidth() / size);
		ArrayList<ArrayList<Integer>> rowPanes = level.getRowPanes(), colPanes = level.getColPanes();

		int MAGIC_X, MAGIC_Y;
		switch (size) {
			case 5:
				MAGIC_X = 55;
				MAGIC_Y = 70;
				break;
			case 10:
				MAGIC_X = 25;
				MAGIC_Y = 40;
				break;
			case 15:
				MAGIC_X = 15;
				MAGIC_Y = 30;
				break;
			default:
				System.out.println("[GridController] Invalid input size.");
				return;
		}

		// generate text for rows
		for (int i = 0; i < size; i++) {
			ArrayList<Integer> rowCount = rowPanes.get(i);
			ListIterator<Integer> it = rowCount.listIterator(rowCount.size());
			int offsetX = 0;
			while (it.hasPrevious()) {
				int c = it.previous();
				Text text = new Text(Integer.toString(c));
				if (c >= 10)
					offsetX -= 15;
				text.setLayoutX(layoutX + offsetX - 20);
				text.setLayoutY(layoutY + i * paneSize + MAGIC_Y);
				text.setFont(NUMBER_FONT);
				text.setFill(FONT_COLOR);
				backgroundPane.getChildren().add(text);
				addedNodes.add(text);
				offsetX -= 20;
			}
		}

		// generate text for columns
		for (int i = 0; i < size; i++) {
			ArrayList<Integer> colCount = colPanes.get(i);
			ListIterator<Integer> it = colCount.listIterator(colCount.size());
			int offsetX = 0, offsetY = 0;
			while (it.hasPrevious()) {
				int c = it.previous();
				Text text = new Text(Integer.toString(c));
				if (c >= 10)
					offsetX -= 10;
				text.setLayoutX(layoutX + i * paneSize + MAGIC_X + offsetX);
				text.setLayoutY(layoutY + offsetY - 10);
				text.setFont(NUMBER_FONT);
				text.setFill(FONT_COLOR);
				backgroundPane.getChildren().add(text);
				addedNodes.add(text);
				offsetY -= 25;
			}
		}
	}

	public void resetNodes() {
		backgroundPane.getChildren().removeAll(addedNodes);
		backgroundPane.getChildren().removeAll(HEART_PENS);
		grid_5.setVisible(false);
		grid_10.setVisible(false);
		grid_15.setVisible(false);
	}

	private enum LeverState
	{
		O,
		X,
		R
	}

	@FXML
	private void handleClickLever() {
		if (isTransitioning)
			return;
		isTransitioning = true;
		List<Pair<Integer, Integer>> seq = new ArrayList<>();
		switch (state) {
			case O:
				seq.addAll(O_TO_X);
				state = LeverState.X;
				break;
			case X:
				seq.addAll(X_TO_R);
				state = LeverState.R;
				break;
			case R:
				seq.addAll(O_TO_X);
				seq.addAll(X_TO_R);
				Collections.reverse(seq);
				state = LeverState.O;
				break;
		}
		Timeline animation = new Timeline();
		double duration = 1500, interval = duration / (seq.size() - 1);
		for (int i = 0; i < seq.size(); i++) {
			Pair<Integer, Integer> loc = seq.get(i);
			int x = loc.getKey(), y = loc.getValue();
			KeyFrame frame = new KeyFrame(new Duration(i * interval), new KeyValue(lever.layoutXProperty(), x), new KeyValue(lever.layoutYProperty(), y));
			animation.getKeyFrames().add(frame);
		}
		animation.getKeyFrames().add(new KeyFrame(new Duration(duration), e -> isTransitioning = false));
		animation.play();
	}

	@FXML
	private void onClickBack() {
		LevelController controller = GameLauncher.transitionTo(GameScene.LEVEL_SELECTION);
		controller.resetNodes();
		controller.chooseDifficulty(GameManager.SELECTED_DIFFICULTY);
	}

	public static void reveal(int x, int y) {
		paneEntries[x][y].setVisible(false);
	}

	public void startLevel(Nonogram level) {
		switch (level.getSize()) {
			case 5:
				initializePane(grid_5, 5);
				drawLines(grid_5, 5);
				generateText(level, grid_5, 5);
				break;
			case 10:
				initializePane(grid_10, 10);
				drawLines(grid_10, 10);
				generateText(level, grid_10, 10);
				break;
			case 15:
				initializePane(grid_15, 15);
				drawLines(grid_15, 15);
				generateText(level, grid_15, 15);
				break;
			default:
				return;
		}
		image.setImage(level.getImage());
		spawnHeartPens();
	}

	private static void onMouseEntered(Pane pane) {
		int x = GridPane.getRowIndex(pane), y = GridPane.getColumnIndex(pane);
		if (!GameManager.hasSelected(x, y))
			setHoverColor(x, y);
	}

	private static void onMouseExited(Pane pane) {
		int x = GridPane.getRowIndex(pane), y = GridPane.getColumnIndex(pane);
		if (!GameManager.hasSelected(x, y))
			setNormalColor(x, y);
	}

	private static void setHoverColor(Pane pane) {
		pane.setBackground(new Background(new BackgroundFill(HOVER_PANE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private static void setNormalColor(Pane pane) {
		pane.setBackground(new Background(new BackgroundFill(UNKNOWN_PANE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private static void setWrongColor(Pane pane) {
		pane.setBackground(new Background(new BackgroundFill(WRONG_PANE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	public static void setHoverColor(int x, int y) {
		setHoverColor(paneEntries[x][y]);
	}

	public static void setNormalColor(int x, int y) {
		setNormalColor(paneEntries[x][y]);
	}

	public static void setWrongColor(int x, int y) {
		setWrongColor(paneEntries[x][y]);
	}
}
