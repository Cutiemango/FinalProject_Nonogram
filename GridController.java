package finalProject;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GridController
{
	private static final Color UNKNOWN_PANE_COLOR = Color.web("#E3CE96");
	private static final Color HOVER_PANE_COLOR = Color.web("#8C8973");
	private static final Color WRONG_PANE_COLOR = Color.web("#FA4646");
	private static final Color BORDER_COLOR = Color.web("#474641");

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
				pane.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

				pane.setOnMouseEntered(e -> onMouseEntered(pane));
				pane.setOnMouseExited(e -> onMouseExited(pane));

				DragHandler.makeDraggable(pane);
				grid.add(pane, col, row);
				paneEntries[row][col] = pane;
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
				break;
			case 10:
				initializePane(grid_10, 10);
				break;
			case 15:
				initializePane(grid_15, 15);
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
