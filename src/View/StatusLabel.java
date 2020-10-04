package View;

import javafx.scene.control.Label;

public class StatusLabel extends Label {
	//for the status on the bottom e.g player 2 is playing
	//probs should put controller here but I didn't make it yet
	public StatusLabel() {
	//default state at the beggining of the game
	this.setText("Player 1 is playing");
	}
}
