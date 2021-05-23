package finalProject;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Optional;

public class DragHandler
{
	private static final LinkedList<Pair<Integer, Integer>> selectedPanes = new LinkedList<>();
	private static Optional<Pane> firstSelected = Optional.empty();

	public static void makeDraggable(Pane pane) {
		pane.setOnMousePressed(e -> handleMousePressed(pane, e));
		pane.setOnDragDetected(e -> handleDragDetected(pane, e));
		pane.setOnMouseDragEntered(e -> handleMouseDragEntered(pane, e));
		pane.setOnMouseReleased(e -> handleMouseReleased(pane, e));
	}

	private static void handleMousePressed(Pane pane, MouseEvent event) {
		if (firstSelected.isEmpty())
			firstSelected = Optional.of(pane);

		int x = GridPane.getRowIndex(pane), y = GridPane.getColumnIndex(pane);
		System.out.printf("[Pressed] x: %d, y: %d\n", x, y);

		selectedPanes.add(new Pair<>(x, y));
		event.consume();
	}

	private static void handleDragDetected(Pane pane, MouseEvent event) {
		pane.startFullDrag();
		event.consume();
	}

	private static void handleMouseDragEntered(Pane pane, MouseEvent event) {
		int x = GridPane.getRowIndex(pane), y = GridPane.getColumnIndex(pane);
		System.out.printf("[Dragged] x: %d, y: %d\n", x, y);
		if (firstSelected.isEmpty())
			return;
		Pane firstPane = firstSelected.get();

		while (!selectedPanes.isEmpty()) {
			Pair<Integer, Integer> selected = selectedPanes.pop();
			GridController.setNormalColor(selected.getKey(), selected.getValue());
		}

		// if the axis is different, we need to clear all selected panes
		int sx = GridPane.getRowIndex(firstPane), sy = GridPane.getColumnIndex(firstPane);
		int deltaX = Math.abs(x - sx), deltaY = Math.abs(y - sy);
		if (deltaX >= deltaY) {
			// select region on x-axis
			for (int i = 0; i <= deltaX; i++) {
				int dx = Math.min(x, sx) + i;
				GridController.setHoverColor(dx, sy);
				selectedPanes.add(new Pair<>(dx, sy));
			}
		} else {
			for (int i = 0; i <= deltaY; i++) {
				int dy = Math.min(y, sy) + i;
				GridController.setHoverColor(sx, dy);
				selectedPanes.add(new Pair<>(sx, dy));
			}
		}

		event.consume();
	}

	private static void handleMouseReleased(Pane pane, MouseEvent event) {
		int x = GridPane.getRowIndex(pane), y = GridPane.getColumnIndex(pane);
		System.out.printf("[Released] x: %d, y: %d\n", x, y);
		GameManager.selectPanes(selectedPanes);
		selectedPanes.clear();

		firstSelected = Optional.empty();
		event.consume();
	}
}
