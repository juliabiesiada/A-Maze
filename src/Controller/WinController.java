package Controller;

import Application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WinController {

    @FXML
    private Button btnReplay;

    public Stage getPreviousStage() {
        return previousStage;
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    Stage previousStage;

    @FXML
    void replay(MouseEvent event) {

        try {

            ((Stage)btnReplay.getScene().getWindow()).close();

            previousStage.close();

            Stage startStage = new Stage();
            StartController controller = new StartController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/startScreen.fxml"));
            loader.setController(controller);
            loader.load();

            Parent root = loader.getRoot();
            Scene scene = new Scene(root, 300, 225);
            scene.getStylesheets().add("View/application.css");
            startStage.setScene(scene);
            startStage.setTitle("A-Maze");
            startStage.setResizable(false);

            startStage.toFront();
            startStage.show();


        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
