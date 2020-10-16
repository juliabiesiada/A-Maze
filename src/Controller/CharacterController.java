package Controller;

import java.io.IOException;

import Model.Game;
import Model.Player;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CharacterController {

    @FXML
    private TextField txtName;
    
    @FXML
    private Text labelOrder;

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
    private Text txtError;
    
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
    Image indy;
    Image lara;
    Image alan;
    Image someone;
    VBox preview1;
    VBox preview2;
    VBox preview3;
    VBox preview4;
    Label player1preview = new Label ("Player 1");
    Label player2preview = new Label ("Player 2");
    Label player3preview = new Label ("Player 3");
    Label player4preview = new Label ("Player 4");
    Label name1preview;
    Label name2preview;
    Label name3preview;
    Label name4preview;
    ImageView imgPreview1;
    ImageView imgPreview2;
    ImageView imgPreview3;
    ImageView imgPreview4;
    
    public void initialize() {
    	
    	txtName.setText("Indiana");
    	characterIndex  = 1;
    	player1active = true;
    	player2active = true;
    	player3active = false;
    	player4active = false;
    	
    	indy = new Image("Assets/indy.png");
    	lara = new Image("/Assets/lara.png");
    	alan = new Image("/Assets/alan.png");
    	someone = new Image ("/Assets/someone.png");
    	
    	imgCharacter.setImage(indy);
    	
    	
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
    	
    	if(arrow.getId().equals("btnLeft")) {
    		
    		if (characterIndex == 1) {
    			characterIndex = 4;
    		}else {
    			characterIndex -=1;
    		}
    		
    	}else if (arrow.getId().equals("btnRight")) {
    		if (characterIndex == 4) {
    			characterIndex = 1;
    		}else {
    			characterIndex +=1;
    		}
    	}
    	
		  
		switch (characterIndex) { 
			case 1: 
				imgCharacter.setImage(indy);
				txtName.setText("Indiana"); 
				break; 
			case 2:
				imgCharacter.setImage(lara); 
				txtName.setText("Lara"); 
				break; 
			case 3: 
				imgCharacter.setImage(alan); 
				txtName.setText("Alan"); 
				break;
			case 4: 
				imgCharacter.setImage(someone);
				txtName.setText("Someone"); 
				break;
			}
		 
    }
    
    @FXML
    void chooseCharacter(MouseEvent event) {
    	
    	txtError.setText("");
    	
    	if (namePlayer().isEmpty()) {
    		txtError.setText("You know your hero needs a name, right?");
    	}else {
    	
			if (player1active) {
				game.getPlayers()[0].setIconURL(urlPlayer(characterIndex));
				game.getPlayers()[0].setName(namePlayer());
				
				setPreview(urlPlayer(characterIndex), namePlayer(), 1);
				player1active = false;
				labelOrder.setText("Player 1, choose your character:");
				
			}else if (player2active) {
				game.getPlayers()[1].setIconURL(urlPlayer(characterIndex));
				game.getPlayers()[1].setName(namePlayer());
				
				setPreview(urlPlayer(characterIndex), namePlayer(), 2);
				player2active = false;
				labelOrder.setText("Player 3, choose your character:");
				
			}else if (player3active) {
				game.getPlayers()[2].setIconURL(urlPlayer(characterIndex));
				game.getPlayers()[2].setName(namePlayer());
				
				setPreview(urlPlayer(characterIndex), namePlayer(), 3);
				player3active = false;
				labelOrder.setText("Player 4, choose your character:");
				
			}else if (player4active) {
	    			game.getPlayers()[3].setIconURL(urlPlayer(characterIndex));
	    			game.getPlayers()[3].setName(namePlayer());
	    			
	    			setPreview(urlPlayer(characterIndex), namePlayer(), 4);
	    			player4active = false;
	    			
	    		}
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
    			mainController.createBoard(game);
    			
    			Stage stage = new Stage(); 
    			stage.setScene(new Scene(root));
    			
    			
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
    
    private String urlPlayer(int characterIndex) {
    	String url = "";
    	switch (characterIndex) {
    	case 1:
    		url = "/Assets/indy.png";
    		break;
    	case 2:
    		url = "/Assets/lara.png";
    		break;
    	case 3:
    		url = "/Assets/alan.png";
    		break;
    	case 4:
    		url = "/Assets/someone.png";
    		break;
    	}
    	return url;
    }
    
    private void setPreview(String urlPlayer, String namePlayer, int index) {
		
		switch (index) {
			case 1:
				imgPreview1 = new ImageView(new Image(urlPlayer));
				imgPreview1.setFitHeight(30);
				imgPreview1.setFitWidth(30);
				name1preview = new Label(namePlayer);
				preview1 = new VBox();
				preview1.setPadding(new Insets(0, 20, 0, 10));
				preview1.getChildren().addAll(player1preview, imgPreview1, name1preview);
				chosenCharacters.getChildren().add(preview1);
				break;
			case 2:
				imgPreview2 = new ImageView(new Image(urlPlayer));
				imgPreview2.setFitHeight(30);
				imgPreview2.setFitWidth(30);
				name2preview = new Label(namePlayer);
				preview2 = new VBox();
				preview2.setPadding(new Insets(0, 20, 0, 0));
				preview2.getChildren().addAll(player2preview, imgPreview2, name2preview);
				chosenCharacters.getChildren().add(preview2);
				break;
			case 3:
				imgPreview3 = new ImageView(new Image(urlPlayer));
				imgPreview3.setFitHeight(30);
				imgPreview3.setFitWidth(30);
				name3preview = new Label(namePlayer);
				preview3 = new VBox();
				preview3.setPadding(new Insets(0, 20, 0, 0));
				preview3.getChildren().addAll(player3preview, imgPreview3, name3preview);
				chosenCharacters.getChildren().add(preview3);
				break;
			case 4:
				imgPreview4 = new ImageView(new Image(urlPlayer));
				imgPreview4.setFitHeight(30);
				imgPreview4.setFitWidth(30);
				name4preview = new Label(namePlayer);
				preview4 = new VBox();
				preview4.setPadding(new Insets(0, 20, 0, 0));
				preview4.getChildren().addAll(player4preview, imgPreview4, name4preview);
				chosenCharacters.getChildren().add(preview4);
				break;
		}
	}
	
}
