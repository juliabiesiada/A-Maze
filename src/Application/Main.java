package Application;

import Controller.CharacterController;
import Controller.LevelController;
import Controller.StartController;
import Application.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {

    private FXMLLoader loader = new FXMLLoader();

    @Override
    public void start(Stage primaryStage) {
        try {

            StartController controller = new StartController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/startScreen.fxml"));
            loader.setController(controller);
            loader.load();

            Parent root = loader.getRoot();
            Scene scene = new Scene(root, 300, 225);
            scene.getStylesheets().add("View/application.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("A-Maze");
            primaryStage.setResizable(false);

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
