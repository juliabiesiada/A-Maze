package Controller;

import java.io.IOException;

import Model.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LevelController {

	Level difficulty;
	//TODO you should make the mouse event change this variable.
	// Then the controller should have access to the variable.
	
	int size;

	public void chooseBeginner(MouseEvent mouseEvent) {
		startGame(7);
	}

	public void chooseAdvanced(MouseEvent mouseEvent) {
		startGame(9);
	}
	
	public void chooseExpert(MouseEvent mouseEvent) {
		startGame(11);
	}
	
	private void startGame(int levelSize) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainBoard.fxml"));
			Parent root = (Parent) loader.load();
			Controller mainController = loader.getController(); 
			mainController.createBoard(levelSize);
			
			Stage stage = new Stage(); 
			stage.setScene(new Scene(root));
			
			
			stage.setTitle("A-maze");
			
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
