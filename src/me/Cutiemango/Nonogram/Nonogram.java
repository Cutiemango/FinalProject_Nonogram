package me.Cutiemango.Nonogram;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Nonogram
{
	public Nonogram(int size, boolean[][] map) {
		this.size = size;
		this.map = map;
		this.rowPanes = new ArrayList<>();
		this.colPanes = new ArrayList<>();

		countPanes();
	}

	private int size;
	private boolean[][] map;
	private ArrayList<ArrayList<Integer>> rowPanes, colPanes;
	private Image image;

	private void countPanes() {
		for (int i = 0; i < size; i++) {
			ArrayList<Integer> rowCount = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				if (map[i][j]) {
					if (j > 0 && map[i][j - 1]) {
						int last = rowCount.get(rowCount.size() - 1);
						rowCount.set(rowCount.size() - 1, last + 1);
					} else {
						rowCount.add(1);
					}
				}
			}
			rowPanes.add(rowCount);
		}

		for (int j = 0; j < size; j++) {
			ArrayList<Integer> colCount = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				if (map[i][j]) {
					if (i > 0 && map[i - 1][j]) {
						int last = colCount.get(colCount.size() - 1);
						colCount.set(colCount.size() - 1, last + 1);
					} else {
						colCount.add(1);
					}
				}
			}
			colPanes.add(colCount);
		}
	}

	public boolean isValid(int x, int y) {
		return map[x][y];
	}

	public int getSize() {
		return size;
	}

	public Image getImage() {
		return image;
	}

	public ArrayList<ArrayList<Integer>> getRowPanes() {
		return rowPanes;
	}

	public ArrayList<ArrayList<Integer>> getColPanes() {
		return colPanes;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
