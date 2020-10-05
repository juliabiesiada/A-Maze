package Application;
	
import Controller.Controller;
import Controller.levelController;
import Model.Gem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.awt.*;


public class Main extends Application {

	private FXMLLoader loader = new FXMLLoader();

	@Override
	public void start(Stage primaryStage) {
		try {

			//Controller myController = new Controller();

			//FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainBoard.fxml"));
			//loader.setController(myController);
			//loader.load();

			levelController controller = new levelController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Levels.fxml"));
			loader.setController(controller);
			loader.load();
			
			Parent root = loader.getRoot();
			primaryStage.setScene(new Scene(root,400, 400));
			primaryStage.setTitle("A-Maze");

			primaryStage.setHeight(450);
			primaryStage.setMaxHeight(500);
			primaryStage.setMinHeight(400);
			
			primaryStage.setWidth(700);
			primaryStage.setMaxWidth(700);
			primaryStage.setMinWidth(530); 
			
			primaryStage.toFront();
			primaryStage.show();


		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
