package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Model.Character;
import Model.Game;
import Model.Player;
import Model.PlayerColor;
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
    
    Player player1 = new Player();
    Player player2 = new Player();
    Player player3 = new Player();
    Player player4 = new Player();
    int characterIndex;
    Game game;
    boolean player1active;
    boolean player2active;
    boolean player3active;
    boolean player4active;
    ArrayList<Character> characters = new ArrayList<Character>();
    ArrayList<VBox> previewVBoxs = new ArrayList<VBox>();
    ArrayList<Label> previewLabels = new ArrayList<Label>();
    ArrayList<Label> previewLabelsSet = new ArrayList<Label>();
    ArrayList<ImageView> previewImageViews = new ArrayList<ImageView>();
    
    public void initialize() {

    	characterIndex  = 0;
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

	private boolean isNameUsed() {
    	boolean isUsed = false;
    	for (int i = 0; i<game.getPlayers().length; i++) {
    		if (namePlayer().equals(game.getPlayers()[i].getName())) {
    			isUsed = true;
    			return isUsed;
			}
		}
    	return isUsed;
	}

	@FXML
    void chooseCharacter(MouseEvent event) {
    	
    	txtError.setText("");
    	
    	if (namePlayer().isEmpty()) {
    		txtError.setText("You know your hero needs a name, right?");
    	} else if (isNameUsed()) {
    		txtError.setText("This name is already used");
		}else {
    	
			if (player1active) {
				game.getPlayers()[0].setIconURL(characters.get(characterIndex).getImageURL());
				game.getPlayers()[0].setName(namePlayer());
				game.getPlayers()[0].setPlayerColor(PlayerColor.BLUE);
				
				setPreview(0);
				player1active = false;
				characters.remove(characterIndex);
				labelOrder.setText("Player 2, choose your character:");
				
			}else if (player2active) {
				game.getPlayers()[1].setIconURL(characters.get(characterIndex).getImageURL());
				game.getPlayers()[1].setName(namePlayer());
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
				game.getPlayers()[2].setName(namePlayer());
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
				game.getPlayers()[3].setName(namePlayer());
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
    			Parent root = (Parent) loader.load();
    			Controller mainController = loader.getController(); 
    			mainController.setGame(game);
    			mainController.createBoard();
    			
    			Stage stage = new Stage();
    			stage.setScene(new Scene(root, 600, 670));
    			
    			
    			stage.setTitle("A-maze");
    			
    			stage.show();
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    	}else {
    		txtError.setText("Character choice incomplete");
    	}
    	
    	
	}

	private String namePlayer() {
    	String name = txtName.getText();
    	return name;
    }
    
    private void setPreview(int index) {

    	previewImageViews.get(index).setImage(new Image(characters.get(characterIndex).getImageURL()));
		previewImageViews.get(index).setFitHeight(25);
		previewImageViews.get(index).setFitWidth(30);
		previewLabels.get(index).setText(namePlayer());
		previewVBoxs.get(index).setPadding(new Insets(0, 20, 0, 10));
		previewVBoxs.get(index).getChildren().addAll(previewLabelsSet.get(index), previewImageViews.get(index),
				previewLabels.get(index));
		chosenCharacters.getChildren().add(previewVBoxs.get(index));

	}
	
}
