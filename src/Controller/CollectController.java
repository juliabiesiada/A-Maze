package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Model.Game;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CollectController {
	
	@FXML
    private ImageView iv1;

    @FXML
    private ImageView iv2;

    @FXML
    private ImageView iv3;

    @FXML
    private ImageView iv4;

    @FXML
    private Label lblTimer;
    
    @FXML
    private VBox popupRoot;
    
    Image arrowUp;
    Image arrowDown;
    Image arrowRight;
    Image arrowLeft;
    
    List<KeyCode> keyOrder;
    List<Image> imgOrder;
    List<Integer> intOrder;
    List<KeyCode> pressedKeys;
    Random rand;
    private static final Integer STARTTIME = 5;
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
    private Timeline timeline;
    Scene thisScene;
    EventHandler<KeyEvent> keyHandler;
    boolean success;
    Game currentGame;
    Controller mainController;

	public void initialize() {
		
		arrowUp = new Image("Assets/arrowUp.png");
		arrowRight = new Image("Assets/arrowRight.png");
		arrowDown = new Image("Assets/arrowDown.png");
		arrowLeft = new Image("Assets/arrowLeft.png");
		
		keyOrder = new ArrayList<KeyCode>();
		imgOrder = new ArrayList<Image>();
		pressedKeys = new ArrayList<KeyCode>();
		
		randomize();
		iv1.setImage(imgOrder.get(0));
		iv2.setImage(imgOrder.get(1));
		iv3.setImage(imgOrder.get(2));
		iv4.setImage(imgOrder.get(3));
		
		initListeners();
		popupRoot.setOnKeyPressed(keyHandler);
		startTimer();

    }

	public void randomize() {
		
		rand = new Random();
		int r;
		
		//1 UP 2 RIGHT 3 DOWN 4 LEFT
		while(keyOrder.size() != 4) {
			
			r = rand.nextInt(5);
			switch (r) {
			case 0:
				break;
			case 1:
				keyOrder.add(KeyCode.UP);
				imgOrder.add(arrowUp);
				break;
			case 2: 
				keyOrder.add(KeyCode.RIGHT);
				imgOrder.add(arrowRight);
				break;
			case 3:
				keyOrder.add(KeyCode.DOWN);
				imgOrder.add(arrowDown);
				break;
			case 4: 
				keyOrder.add(KeyCode.LEFT);
				imgOrder.add(arrowLeft);
				break;
			}
			
		}
		
	}
	
	private void startTimer() {
		
		lblTimer.textProperty().bind(timeSeconds.asString());
		timeSeconds.set(STARTTIME);
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME+1),
				 new KeyValue(timeSeconds, 0)));
		timeline.setCycleCount(1);
		timeline.setAutoReverse(true);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Stage stage = (Stage) lblTimer.getScene().getWindow();
		    	stage.close();
		    	timeline.stop();
		    	mainController.removeBuffDebuff();
			}
		};
		timeline.setOnFinished(onFinished);
		timeline.playFromStart();
	}

	public void assignListeners(Game game, Controller controller) {
		popupRoot.getScene().setOnKeyPressed(keyHandler);
		currentGame = game;
		mainController = controller;
	}
	
	private void initListeners() {
		

		keyHandler = new EventHandler<KeyEvent>() {
			
			//last index from the array
			int x;

			@Override
			public void handle(KeyEvent event) {
				
				if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.RIGHT || 
						event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.LEFT) {
					
					pressedKeys.add(event.getCode());
					
					//since the list is not limited on each key pressed I'm checking this key and 3 before it
					if (pressedKeys.size() >= 4) {
						
						x = pressedKeys.size() - 1;
						
						if (keyOrder.get(0) == pressedKeys.get(x-3) && keyOrder.get(1) == pressedKeys.get(x-2) &&
								keyOrder.get(2) == pressedKeys.get(x-1) && keyOrder.get(3) == event.getCode()) {
							Stage stage = (Stage) lblTimer.getScene().getWindow();
							stage.close();
							timeline.stop();
							mainController.collectBuffDebuff();
							
						}
						
					}
				}
			}
		};
		
	}
}
