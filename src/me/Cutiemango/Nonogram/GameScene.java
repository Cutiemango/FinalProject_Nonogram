package me.Cutiemango.Nonogram;

public enum GameScene
{
	MENU("/assets/Menu.fxml"),
	DIFFICULTY_SELECTION("/assets/DifficultySelection.fxml"),
	LEVEL_SELECTION("/assets/LevelSelection.fxml"),
	GAME("/assets/Game.fxml");

	GameScene(String fxmlLocation) {
		this.fxmlLocation = fxmlLocation;
	}

	private final String fxmlLocation;

	public String getFxmlLocation() {
		return fxmlLocation;
	}
}
