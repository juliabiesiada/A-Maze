package Controller;

import java.io.IOException;

import Model.Game;
import Model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartController {
	
	  @FXML
	    private BorderPane rootStart;

	    @FXML
	    private Button twoPlayers;

	    @FXML
	    private Button threePlayers;

	    @FXML
	    private Button fourPlayers;
	    
	    Game game = new Game();

	    @FXML
	    void startGame(MouseEvent event) {
	    	
	    	String bID = ((Button) event.getSource()).getId();
	    	switch (bID) {
	    	case "twoPlayers":
	    		game.setPlayers(setPlayers(2));
	    		break;
	    	case "threePlayers":
	    		game.setPlayers(setPlayers(3));
	    		break;
	    	case "fourPlayers":
	    		game.setPlayers(setPlayers(4));
	    	}
	    	
	    	try {
				
				Stage thisStage = (Stage) twoPlayers.getScene().getWindow();
				thisStage.close();
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/levels.fxml"));
				Parent root = (Parent) loader.load();
				LevelController lvlController = loader.getController(); 
				lvlController.startChoosing(game);
				
				Stage stage = new Stage(); 
				Scene scene = new Scene(root, 400, 400);
				scene.getStylesheets().add("View/application.css");
				stage.setScene(scene);
				
				
				stage.setTitle("A-maze");
				
				stage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }


        private Player[] setPlayers(int i) {

            if (i == 2) {
				Player[] players = {new Player(), new Player()};
				return players;
			} else if (i == 3) {
				Player[] players = {new Player(), new Player(), new Player()};
				return players;
			}else {
				Player[] players = {new Player(), new Player(), new Player(), new Player()};
				return players;
			}
		}
	
}
