package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Model.OnCard;
import Model.Position;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
    List<KeyCode> pressedKeys;
    Random rand;
    private static final Integer STARTTIME = 3;
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
    private Timeline timeline;
    EventHandler<KeyEvent> keyHandler;
    Controller mainController;
    Position position;
    OnCard onCard;

	public void setOnCard(OnCard on) {
    	this.onCard = on;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void initialize() {

		arrowUp = new Image("Assets/arrowUp.png");
		arrowRight = new Image("Assets/arrowRight.png");
		arrowDown = new Image("Assets/arrowDown.png");
		arrowLeft = new Image("Assets/arrowLeft.png");

		//correct order of keys pressed
		keyOrder = new ArrayList<>();
		//order of images, corresponding to correct key order
		imgOrder = new ArrayList<>();
		//list of all the keys pressed
		pressedKeys = new ArrayList<>();

		randomize();
		iv1.setImage(imgOrder.get(0));
		iv2.setImage(imgOrder.get(1));
		iv3.setImage(imgOrder.get(2));
		iv4.setImage(imgOrder.get(3));

		initListeners();
		popupRoot.setOnKeyPressed(keyHandler);
		startTimer();

    }

	/**
	 * This method randomizes the correct order of keys and corresponding images
	 */
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

	/**
	 * Starting the timer, user has 5 seconds to enter correct key combination
	 */
	private void startTimer() {

		lblTimer.textProperty().bind(timeSeconds.asString());
		timeSeconds.set(STARTTIME);
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME+1),
				 new KeyValue(timeSeconds, 0)));
		timeline.setCycleCount(1);
		timeline.setAutoReverse(true);
		EventHandler<ActionEvent> onFinished = arg0 -> {
			Stage stage = (Stage) lblTimer.getScene().getWindow();
			stage.close();
			timeline.stop();
		};
		timeline.setOnFinished(onFinished);
		timeline.playFromStart();
	}

	/**
	 * this method only exists here, because of not being able to call getScene from initialize
	 * or any other method that is called in initialize. The params are also used to call a controller
	 * method to collect a buffer/debuffer
	 * @param controller current instance of Controller
	 */
	public void assignListeners(Controller controller) {
		popupRoot.getScene().setOnKeyPressed(keyHandler);
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
