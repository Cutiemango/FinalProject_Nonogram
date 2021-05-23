package finalProject;

import javafx.scene.image.Image;

public class Nonogram
{
	public Nonogram(int size, boolean[][] map) {
		this.size = size;
		this.map = map;
	}

	private int size;
	private boolean[][] map;
	private Image image;

	public boolean isValid(int x, int y) {
		return map[x][y];
	}

	public int getSize() {
		return size;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
