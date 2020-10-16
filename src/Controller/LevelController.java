package Controller;

import java.io.IOException;

import Model.Game;
import Model.Level;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LevelController {
	
	@FXML
	GridPane boardBeginner;

	Game game = new Game();
	//TODO you should make the mouse event change this variable.
	// Then the controller should have access to the variable.

	public void startChoosing(Game game) {
		this.game = game;
	}
	
	public void chooseBeginner(MouseEvent mouseEvent) {
		game.setLevel(Level.EASY);
		chooseLevel(game);
	}

	public void chooseAdvanced(MouseEvent mouseEvent) {
		game.setLevel(Level.MEDIUM);
		chooseLevel(game);
	}
	
	public void chooseExpert(MouseEvent mouseEvent) {
		game.setLevel(Level.HARD);
		chooseLevel(game);
	}
	
	public void chooseLevel(Game game) {
			
		try {
			
			Stage thisStage = (Stage) boardBeginner.getScene().getWindow();
			thisStage.close();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/characters.fxml"));
			Parent root = (Parent) loader.load();
			CharacterController charController = loader.getController(); 
			charController.startChoosing(game);
			
			Stage stage = new Stage(); 
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add("View/application.css");
			stage.setScene(scene);
			
			stage.setWidth(420);
			stage.setHeight(500);
			
			
			stage.setTitle("A-maze");
			
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
