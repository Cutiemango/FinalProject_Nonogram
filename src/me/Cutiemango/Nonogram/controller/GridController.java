package me.Cutiemango.Nonogram.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import me.Cutiemango.Nonogram.DragHandler;
import me.Cutiemango.Nonogram.GameManager;
import me.Cutiemango.Nonogram.Nonogram;

import java.util.ArrayList;
import java.util.ListIterator;

public class GridController
{
	private static final Color UNKNOWN_PANE_COLOR = Color.web("#E3CE96");
	private static final Color HOVER_PANE_COLOR = Color.web("#8C8973");
	private static final Color WRONG_PANE_COLOR = Color.web("#FA4646");
	private static final Color BORDER_COLOR = Color.web("#222222");

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

	private static Pane[][] paneEntries;

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
		}

		// spawn horizontal lines
		for (int i = 0; i <= size; i++) {
			Line line = new Line(-160, 0, grid.getPrefWidth(), 0);
			line.setLayoutX(layoutX);
			line.setLayoutY(layoutY + i * paneSize);
			line.setStroke(BORDER_COLOR);
			backgroundPane.getChildren().add(line);
		}
	}

	private void generateText(Nonogram level, GridPane grid, int size) {
		double layoutX = grid.getLayoutX(), layoutY = grid.getLayoutY();
		int paneSize = (int) (grid.getPrefWidth() / size);
		ArrayList<ArrayList<Integer>> rowPanes = level.getRowPanes(), colPanes = level.getColPanes();

		final int MAGIC_X, MAGIC_Y;
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
			int offset = 0;
			while (it.hasPrevious()) {
				int c = it.previous();
				Text text = new Text(Integer.toString(c));
				if (c >= 10)
					offset -= 15;
				text.setLayoutX(layoutX + offset - 20);
				text.setLayoutY(layoutY + i * paneSize + MAGIC_Y);
				text.setFont(NUMBER_FONT);
				backgroundPane.getChildren().add(text);
				offset -= 20;
			}
		}

		// generate text for columns
		for (int i = 0; i < size; i++) {
			ArrayList<Integer> colCount = colPanes.get(i);
			ListIterator<Integer> it = colCount.listIterator(colCount.size());
			int offset = 0;
			while (it.hasPrevious()) {
				int c = it.previous();
				Text text = new Text(Integer.toString(c));
				text.setLayoutX(layoutX + i * paneSize + MAGIC_X);
				text.setLayoutY(layoutY + offset - 10);
				text.setFont(NUMBER_FONT);
				backgroundPane.getChildren().add(text);
				offset -= 25;
			}
		}
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
