package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Model.Character;
import Model.Game;
import Model.Player;
import Model.PlayerColor;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CharacterController {

    @FXML
    private TextField txtName;
    
    @FXML
    private Label labelOrder;

    @FXML
    private ImageView btnLeft;

    @FXML
    private ImageView btnRight;

    @FXML
    private ImageView imgCharacter;
    
    @FXML
    private HBox chosenCharacters;

    @FXML
    private Button btnStart;
    
    @FXML
    private Label txtError;

    int characterIndex;
    Game game;
    boolean player1active;
    boolean player2active;
    boolean player3active;
    boolean player4active;
    ArrayList<Character> characters = new ArrayList<>();
    ArrayList<VBox> previewVBoxs = new ArrayList<>();
    ArrayList<Label> previewLabels = new ArrayList<>();
    ArrayList<Label> previewLabelsSet = new ArrayList<>();
    ArrayList<ImageView> previewImageViews = new ArrayList<>();
    
    public void initialize() {
		//decides which image and name is currently displayed
    	characterIndex  = 0;
    	//determines which player the choice is being made for
    	player1active = true;
    	player2active = true;
    	player3active = false;
    	player4active = false;

		characters.add(new Character("Assets/indiana.png", "Indiana"));
		characters.add(new Character("/Assets/lara.png", "Lara"));
		characters.add(new Character("/Assets/sophia.png", "Sophia"));
		characters.add(new Character("/Assets/marcello.png", "Marcello"));
    	
    	imgCharacter.setImage(new Image(characters.get(characterIndex).getImageURL()));
    	txtName.setText(characters.get(characterIndex).getName());

    	//GUI elements for preview at the bottom of the screen
		previewVBoxs.add(new VBox());
		previewVBoxs.add(new VBox());
		previewVBoxs.add(new VBox());
		previewVBoxs.add(new VBox());
		previewLabelsSet.add(new Label ("Player 1"));
		previewLabelsSet.add(new Label ("Player 2"));
		previewLabelsSet.add(new Label ("Player 3"));
		previewLabelsSet.add(new Label ("Player 4"));
		previewLabels.add(new Label());
		previewLabels.add(new Label());
		previewLabels.add(new Label());
		previewLabels.add(new Label());
		previewImageViews.add(new ImageView());
		previewImageViews.add(new ImageView());
		previewImageViews.add(new ImageView());
		previewImageViews.add(new ImageView());
    }

	/**method called from the previous controller to pass the current game object
	 * @param game current game object
	 */
    public void startChoosing(Game game) {
    	this.game = game;
    	
    	//activating more players depending on the chosen amount of players
    	if (game.getPlayers().length == 3) {
    		player3active = true;
    	}else if (game.getPlayers().length == 4) {
    		player3active = true;
    		player4active = true;
    	}
    }

    @FXML
    void switchCharacter(MouseEvent event) {
    	
    	ImageView arrow = (ImageView) event.getSource();
    	int delta = 0;

    	if(arrow.getId().equals("btnLeft")) {
			delta = -1;
    		
    	}else if (arrow.getId().equals("btnRight")) {
    		delta = 1;
    	}
		switchView(delta);
    }

	/**Method switching the character image and name in reaction to clicking on arrows
	 * @param delta indicates the direction of the switch (left/right)
	 */
	private void switchView(int delta) {
		if (characterIndex + delta < 0) {
			characterIndex = 3;
		}else if (characterIndex + delta >= characters.size()) {
			characterIndex = 0;
		}else {
			characterIndex += delta;
		}
		if (characters.size() > 0) {
			imgCharacter.setImage(new Image(characters.get(characterIndex).getImageURL()));
			txtName.setText(characters.get(characterIndex).getName());
		}

	}

	/**
	 * Method to prevent the players from choosing identical names
	 */
	private boolean isNameUsed() {

    	for (int i = 0; i<game.getPlayers().length; i++) {
    		if (txtName.getText().equals(game.getPlayers()[i].getName())) {
    			return true;
			}
		}
    	return false;
	}

	/**Sets player's icon and name when the character icon is pressed
	 * Removes chosen characters from the list to prevent repeating characters
	 *
	 */
	@FXML
    void chooseCharacter() {
    	
    	txtError.setText("");
    	
    	if (txtName.getText().isEmpty()) {
    		txtError.setText("You know your hero needs a name, right?");
    	} else if (isNameUsed()) {
    		txtError.setText("This name is already used");
		}else {
    	
			if (player1active) {
				game.getPlayers()[0].setIconURL(characters.get(characterIndex).getImageURL());
				game.getPlayers()[0].setName(txtName.getText());
				game.getPlayers()[0].setPlayerColor(PlayerColor.BLUE);
				
				setPreview(0);
				player1active = false;
				characters.remove(characterIndex);
				labelOrder.setText("Player 2, choose your character:");
				
			}else if (player2active) {
				game.getPlayers()[1].setIconURL(characters.get(characterIndex).getImageURL());
				game.getPlayers()[1].setName(txtName.getText());
				game.getPlayers()[1].setPlayerColor(PlayerColor.GREEN);
				
				setPreview(1);
				player2active = false;
				characters.remove(characterIndex);
				if (player3active) {
					labelOrder.setText("Player 3, choose your character:");
				}else {
					labelOrder.setText("All set! Press confirm button to start.");
				}
				
			}else if (player3active) {
				game.getPlayers()[2].setIconURL(characters.get(characterIndex).getImageURL());
				game.getPlayers()[2].setName(txtName.getText());
				game.getPlayers()[2].setPlayerColor(PlayerColor.YELLOW);
				
				setPreview(2);
				player3active = false;
				characters.remove(characterIndex);
				if (player4active) {
					labelOrder.setText("Player 4, choose your character:");
				}else {
					labelOrder.setText("All set! Press confirm button to start.");
				}
				
			}else if (player4active) {
				game.getPlayers()[3].setIconURL(characters.get(characterIndex).getImageURL());
				game.getPlayers()[3].setName(txtName.getText());
				game.getPlayers()[3].setPlayerColor(PlayerColor.RED);

				setPreview(3);
				player4active = false;
				characters.remove(characterIndex);
				labelOrder.setText("All set! Press confirm button to start.");
	    		}
			switchView(1);
    		}

    	}
    
    @FXML
    private void startGame() {
    	
    	if(!player1active && !player2active && !player3active && !player4active) {
    		
    		try {
    			
    			Stage thisStage = (Stage) txtName.getScene().getWindow();
    			thisStage.close();
    			
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainBoard.fxml"));
    			Parent root = loader.load();
    			Controller mainController = loader.getController(); 
    			mainController.setGame(game);
    			mainController.createBoard();
    			
    			Stage stage = new Stage();
    			stage.setScene(new Scene(root, 600, 620));
    			
    			
    			stage.setTitle("A-maze");
    			
    			stage.show();
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    	}else {
    		txtError.setText("Character choice incomplete");
    	}
    	
    	
	}

	/**Setting the preview at the bottom of the screen
	 * @param index Which player image and name should be added to preview
	 */
    private void setPreview(int index) {

    	previewImageViews.get(index).setImage(new Image(characters.get(characterIndex).getImageURL()));
		previewImageViews.get(index).setFitHeight(25);
		previewImageViews.get(index).setFitWidth(30);
		previewLabels.get(index).setText(txtName.getText());
		previewVBoxs.get(index).setPadding(new Insets(0, 20, 0, 10));
		previewVBoxs.get(index).getChildren().addAll(previewLabelsSet.get(index), previewImageViews.get(index),
				previewLabels.get(index));
		previewVBoxs.get(index).setId(""+index);
		chosenCharacters.getChildren().add(previewVBoxs.get(index));

	}
}
