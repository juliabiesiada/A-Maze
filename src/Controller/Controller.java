package Controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Model.*;
import Model.Card;
import Model.Game;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Controller {

	@FXML
	private StackPane board_root;
	@FXML 
	private ImageView iconGems;
	@FXML
	private ImageView iconBuff;
	@FXML
	private ImageView iconDebuff;
	@FXML
	private Label lblGems;
	@FXML
	private Label lblBuff;
	@FXML 
	private Label lblDebuff;
	@FXML
	Button btnEndTurn;
	@FXML
	Label labelStatus;

	GridPane board_grid;
	int tileDimension;
	int levelSize;
	int numOfGems;
	boolean buffDebuffAlreadySpawn = false;
	Game game;
	private EventHandler<MouseEvent> onPaneDragDetected;
	private EventHandler<DragEvent> onDragDropped;
    private EventHandler<DragEvent> onPaneDragOver;
    private EventHandler<MouseEvent> onPaneClick;
    Image playerImage;
    ImageView playerImageView;
    StackPane[][] sPanes;
    OnCard onCardEnd;
    Card destination;
    boolean moveAllowed;
	List<int[][]> historyMatrix = new ArrayList<>();
	List<Position> historyPosition = new ArrayList<>();
	String imgID;
	Boolean rotationMove;
	Boolean stairsSpawned;
	Position stairsPosition;


	public void setGame(Game game) {
		this.game = game;
	}

	public void onEndTurnClicked() {
		//End Turn
		//allowing player to move
		rotationMove = false;
		moveAllowed = true;
		buffDebuffAlreadySpawn = false;
		//resetting history for rotation
		historyMatrix.clear();
		historyPosition.clear();
		game.getTurnsOrder().turnCompleted(game.getTurnsOrder().whosPlaying());
		String gemURL = colorToGemURL(game.getTurnsOrder().whosPlaying().getPlayerColor());
		iconGems.setImage(new Image(gemURL));
		if (game.getTurnsOrder().whosPlaying().getInventory() != null) {

				lblGems.setText(""+game.getTurnsOrder().whosPlaying().getInventory().getGemsCollected());
				lblBuff.setText(""+game.getTurnsOrder().whosPlaying().getInventory().getBufferCollected());
				lblDebuff.setText(""+game.getTurnsOrder().whosPlaying().getInventory().getDebufferCollected());

		}else {
			lblBuff.setText("0");
			lblDebuff.setText("0");
			lblGems.setText("0");
		}

		game.getStatus().getStatusList().add(game.getTurnsOrder().whosPlaying().getName() + " is playing");
		labelStatus.setText(game.getStatus().getStatusList().get(game.getStatus().getStatusList().size()-1));

	}
	
	public void initialize() {
		initHandlers();
		//to allow only one movement per turn
		moveAllowed = true;
		rotationMove = false;
		stairsSpawned = false;
	}

	public void createBoard() {
		
		levelSize = checkLevel(game);
		setVariables(game);
		TurnManager turnManager = new TurnManager(game.getPlayers());
		game.setTurnsOrders(turnManager);
		
		CardsController cardsController = new CardsController(levelSize);
		Card[][] cards = cardsController.getCards();
		game.setCardsOnBoard(cards);
		setPlayerInitialPosition(game);

		game.getStatus().getStatusList().add(game.getPlayers()[0].getName() + " is playing");
		labelStatus.setText(game.getStatus().getStatusList().get(game.getStatus().getStatusList().size()-1));

		spawnGems();
		drawEverything();

		for (int i=0; i<game.getCardsOnBoard().length; i++) {
			for (int j=0; j<game.getCardsOnBoard().length; j++) {
				System.out.println(game.getCardsOnBoard()[i][j].getOnCard().toString());
			}
		}

	}

	public void drawEverything() {
		drawBoard();
		drawGems();
		spawnBufferDebuffer();
		if (stairsSpawned) {
			spawnStairs();
		}
		spawnPlayers();
	}

	public void drawBoard() {
		//root is pane, contains a grid, allows us to be flexible with the size
		board_root.getChildren().clear();
		board_grid = new GridPane();
		board_root.getChildren().add(board_grid);
		board_grid.setAlignment(Pos.CENTER);
		Card[][] cards = game.getCardsOnBoard();
		levelSize = cards.length;

		//this creates a pane inside each cell of the grid
		sPanes = new StackPane[levelSize][levelSize];
		for (int i = 0; i < levelSize; i++) {
			for (int j = 0; j < levelSize; j++) {
				StackPane sPane = new StackPane();
				sPane.setId("r"+i+"c"+j);
				sPane.setOnDragDetected(onPaneDragDetected);
				sPane.setOnDragOver(onPaneDragOver);
				sPane.setOnDragDropped(onDragDropped);
				sPane.setOnMouseClicked(onPaneClick);
				sPanes[i][j] = sPane;
				board_grid.add(sPane, j, i);
			}
		}
		board_grid.setGridLinesVisible(true);

		//this colors the cards
		for (int i = 0; i<cards.length; i++) {
			for (int j = 0; j<cards.length; j++) {
				GridPane tinyGrid = new GridPane();
				for (int l = 0; l < 3; l++) {
					for (int m = 0; m < 3; m++) {
						Pane pane = new Pane();
						if (cards[i][j].getCardMatrix()[l][m] == 0) {
							Image path = new Image("/Assets/path.png");
							ImageView pathImageView = new ImageView(path);
							pathImageView.setFitWidth(tileDimension);
							pathImageView.setFitHeight(tileDimension);
							pane.getChildren().add(pathImageView);
						} else {
							String url = "";
							switch (cards[i][j].getCardMatrix()[l][m]) {
								case 1:
									url = "/Assets/wall1.png";
									break;
								case 2:
									url = "/Assets/wall2.png";
									break;
								case 3:
									url = "/Assets/wall3.png";
									break;
								case 4:
									url = "/Assets/wall4.png";
									break;
								case 5:
									url = "/Assets/wall5.png";
									break;
								case 6:
									url = "/Assets/wall6.png";
									break;
								case 7:
									url = "/Assets/wall7.png";
									break;
								case 8:
									url = "/Assets/wall8.png";
									break;
							}
							Image wall = new Image(url);
							ImageView wallImageView = new ImageView(wall);
							wallImageView.setFitWidth(tileDimension);
							wallImageView.setFitHeight(tileDimension);
							pane.getChildren().add(wallImageView);
						}
						tinyGrid.add(pane, m, l);
					}
				}
				sPanes[i][j].getChildren().add(tinyGrid);
			}
		}
	}

	public void spawnPlayers() {

		//this put players on the board
		for (Player player : game.getPlayers()) {
			playerImage = new Image(player.getIconURL());
			playerImageView = new ImageView(playerImage);
			playerImageView.setFitHeight(tileDimension);
			playerImageView.setFitWidth(tileDimension);
			sPanes[player.getPosition().getRow()][player.getPosition().getColumn()].getChildren().add(playerImageView);
			StackPane.setAlignment(playerImageView, Pos.CENTER);
			game.getCardsOnBoard()[player.getPosition().getRow()][player.getPosition().getColumn()].setOnCard(OnCard.PLAYER);
		}
	}

	public void spawnGems() {

		//this put diamonds on the board
		for (Player player : game.getPlayers()) {

			for (int i = 0; i<numOfGems; i++) {
				int randomRow = new Randomizer().randomize(levelSize);
				int randomCol = new Randomizer().randomize(levelSize);
				if (game.getCardsOnBoard()[randomRow][randomCol].getOnCard() == OnCard.NOTHING &&
						game.getCardsOnBoard()[randomRow][randomCol].isAvailable()) {
					game.getCardsOnBoard()[randomRow][randomCol].setOnCard(OnCard.GEM);
					game.getGems().add(new Gem(player.getPlayerColor(), new Position(randomRow,randomCol)));
				} else {
					i--;
				}

			}
		}
	}

	public void drawGems() {
		for (int i = 0; i<game.getGems().size(); i++) {
			int row = game.getGems().get(i).getPosition().getRow();
			int col = game.getGems().get(i).getPosition().getColumn();
			Image gemImage = new Image(colorToGemURL(game.getGems().get(i).getPlayerColor()));
			ImageView gemImageView = new ImageView(gemImage);
			gemImageView.setFitHeight(tileDimension);
			gemImageView.setFitWidth(tileDimension);
			sPanes[row][col].getChildren().add(gemImageView);
			StackPane.setAlignment(gemImageView, Pos.CENTER);
		}
	}

	public void spawnBufferDebuffer() {
		//handles buffer and debuffer
		int val = game.getTurnsOrder().getCounter() % 10;
		if (val == 0 && !buffDebuffAlreadySpawn) {
			game.updateBuffDebuff();
			buffDebuffAlreadySpawn = true;
		}
		for (int i = 0; i<game.getBuffPositions().size(); i++) {
			Image buffImage = new Image("/Assets/potion_icon.png");
			ImageView buffImageView = new ImageView(buffImage);
			buffImageView.setFitHeight(tileDimension);
			buffImageView.setFitWidth(tileDimension);
			sPanes[game.getBuffPositions().get(i).getRow()][game.getBuffPositions().get(i).getColumn()].getChildren().add(buffImageView);
			StackPane.setAlignment(buffImageView, Pos.CENTER);
		}
		for (int i = 0; i<game.getDebuffPositions().size(); i++) {
			Image debuffImage = new Image("/Assets/poison_icon.png");
			ImageView debuffImageView = new ImageView(debuffImage);
			debuffImageView.setFitHeight(tileDimension);
			debuffImageView.setFitWidth(tileDimension);
			sPanes[game.getDebuffPositions().get(i).getRow()][game.getDebuffPositions().get(i).getColumn()].getChildren().add(debuffImageView);
			StackPane.setAlignment(debuffImageView, Pos.CENTER);
		}
	}

	public String colorToGemURL(PlayerColor color) {
		String urlGem = "";
		switch (color) {
			case RED:
				urlGem = "/Assets/gem_red.png";
				break;
			case BLUE:
				urlGem = "/Assets/gem_icon.png";
				break;
			case YELLOW:
				urlGem = "/Assets/gem_yellow.png";
				break;
			case GREEN:
				urlGem = "/Assets/gem_green.png";
				break;
		}
		return urlGem;
	}


	/**
	 * This method set the variables (as tileDimension or numOfGems) based on the level of the game
	 * @param game to have the level of the game
	 */
	private void setVariables(Game game) {
	switch (game.getLevel()) {
		case EASY: tileDimension = 22;
			numOfGems = 4;
		break;
		case MEDIUM: tileDimension = 18;
			numOfGems = 7;
		break;
		case HARD: tileDimension = 15;
			numOfGems = 10;
		break;
	}
	}

	/**
	 * This method check the level of the game and set a value for designing the grid
	 * @param game to check the level
	 * @return the value of how big the grid will be
	 */
	private int checkLevel(Game game) {
		int levelSize = 0;
		switch (game.getLevel()) {
			case EASY:
				levelSize = 7;
				break;
			case MEDIUM:
				levelSize = 9;
				break;
			case HARD:
				levelSize = 11;
				break;
		}
		return levelSize;
	}

    /**
     * This method sets the initial position of each player
     * @param game to check the player color
     */
	private void setPlayerInitialPosition(Game game) {
	    for (Player player : game.getPlayers()) {
            switch (player.getPlayerColor()) {
                case BLUE:
                    player.setPosition(new Position(0,0));
                    break;
                case GREEN:
                    player.setPosition(new Position(0,levelSize-1));
                    break;
                case YELLOW:
                    player.setPosition(new Position(levelSize-1,0));
                    break;
                case RED:
                    player.setPosition(new Position(levelSize-1,levelSize-1));
                    break;
            }
        }
    }

    private void showPopup(Position pos, OnCard onThisCard) throws IOException {

		game.removeBuffDebuff(pos, onThisCard);
		Stage popup = new Stage();
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.setTitle("Collect the potion!");

		FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("/View/popupCollect.fxml"));
		CollectController collectController = new CollectController();
		collectController.setPosition(destination.getPosition());
		collectController.setOnCard(onCardEnd);
		popupLoader.setController(collectController);

		popupLoader.load();


		Parent root = popupLoader.getRoot();

		Scene scene = new Scene(root, 349, 192);
		//kind of a stupid solution but well, couldn't get scene in initialize
		collectController.assignListeners(this);
		scene.getStylesheets().add("View/application.css");
		popup.setScene(scene);
		popup.setResizable(false);
		popup.show();

		//to stop user from closing the window
		Platform.setImplicitExit(false);
		popup.setOnCloseRequest(Event::consume);

		popup.setHeight(192);
		popup.setWidth(349);

		popup.setMinHeight(192);
		popup.setMinWidth(349);

	}
    
    @FXML
    private void handleIconDragOver(DragEvent event) {
    	event.acceptTransferModes(TransferMode.ANY);
    	event.consume();
    }

    @FXML
	void easterEggMagic() {

		for (Player player : game.getPlayers()) {
			if (player.getName().equals("Raphael") || player.getName().equals("raphael") || player.getName().equals("Raphaël")) {
				player.setIconURL("/Assets/umbreon.gif");
			}
			else if (player.getName().equals("Mehdi") || player.getName().equals("mehdi")) {
				player.setIconURL("/Assets/cyndaquil.gif");
			}
		}
		drawEverything();
	}
    
    @FXML 
    private void  handleIconDragDetected(MouseEvent event) {
    	
    	imgID = ((ImageView) event.getSource()).getId();
    	Image img;
    	switch (imgID) {
    	case "iconBuff":
    		if (Integer.parseInt(lblBuff.getText()) > 0) {
    			Dragboard db = iconBuff.startDragAndDrop(TransferMode.ANY);
    	        ClipboardContent cb = new ClipboardContent();
    	        img = new Image("/Assets/potion_icon.png", 50, 50, false, false);
    	        cb.putImage(img);
    	        db.setContent(cb);
    	        event.consume();
    		}
    		break;
    	case "iconDebuff":
    		if (Integer.parseInt(lblDebuff.getText()) > 0) {
    			Dragboard db = iconDebuff.startDragAndDrop(TransferMode.ANY);
    	        ClipboardContent cb = new ClipboardContent();
				img = new Image("/Assets/poison_icon.png", 50, 50, false, false);
    	        cb.putImage(img);
    	        db.setContent(cb);
    	        event.consume();
    		}
    		break;
    	}
    	
    }
    
    private void initHandlers() {
    	//for panes
    	onPaneDragDetected = event -> {

			Position rc = strToID(((Pane)event.getSource()).getId());
			Dragboard db = ((Pane)event.getSource()).startDragAndDrop(TransferMode.ANY);
			ClipboardContent cb = new ClipboardContent();
			cb.putString(""+rc.getRow()+""+rc.getColumn());
			db.setContent(cb);

			event.consume();
		};
    	
        onPaneDragOver = event -> {
			event.acceptTransferModes(TransferMode.ANY);
			event.consume();
		};
        
        onDragDropped = event -> {

			Dragboard db = event.getDragboard();

			String paneID = ((Pane)event.getSource()).getId();

			if (db.hasImage()) {
				
				Node node = (Node)event.getSource();
				Integer cIndex = GridPane.getColumnIndex(node);
				Integer rIndex = GridPane.getRowIndex(node);
				Player selectedPlayer = null;

				for (int i = 0; i<game.getPlayers().length; i++) {
					if (game.getPlayers()[i].getPosition().getColumn() == cIndex &&
							game.getPlayers()[i].getPosition().getRow() == rIndex) {

						selectedPlayer = game.getPlayers()[i];

					}
				}
				if (selectedPlayer != null) {

					//check if the same images

					if (imgID.equals(iconBuff.getId())) {
						game.getTurnsOrder().bufferReceived(selectedPlayer);
						game.getTurnsOrder().whosPlaying().getInventory().setBufferCollected
								(game.getTurnsOrder().whosPlaying().getInventory().getBufferCollected()-1);
						lblBuff.setText(""+game.getTurnsOrder().whosPlaying().getInventory().getBufferCollected());
						game.getStatus().getStatusList().add(selectedPlayer.getName() + " has been buffed");
					} else if (imgID.equals(iconDebuff.getId())) {
						if (!selectedPlayer.equals(game.getTurnsOrder().whosPlaying())) {
							game.getTurnsOrder().debufferReceived(selectedPlayer);
							game.getTurnsOrder().whosPlaying().getInventory().setDebufferCollected
									(game.getTurnsOrder().whosPlaying().getInventory().getDebufferCollected()-1);
							lblDebuff.setText(""+game.getTurnsOrder().whosPlaying().getInventory().getDebufferCollected());
							game.getStatus().getStatusList().add(selectedPlayer.getName() + " has been debuffed");
						}
					}
					labelStatus.setText(game.getStatus().getStatusList().get(game.getStatus().getStatusList().size()-1));
				}

				event.consume();
			} else if (moveAllowed){

				Card[][] thisMatrix = new Card[game.getCardsOnBoard().length][];
				for (int i = 0; i < game.getCardsOnBoard().length; i++) {
					thisMatrix[i] = game.getCardsOnBoard()[i].clone();
				}

				String rcStart = db.getString();
				int rStart = Integer.parseInt(""+rcStart.charAt(0));
				int cStart = Integer.parseInt(""+rcStart.charAt(1));

				Position rc = strToID(paneID);

				Position startPos = new Position(rStart, cStart);
				Position endPos = new Position(rc.getRow(), rc.getColumn());

				Card[][] slideMatrix = game.getCardsOnBoard();
				game = CardsController.cardsSlider(game, slideMatrix, startPos, endPos, slideMatrix.length);

				drawEverything();
				for (int i=0; i<thisMatrix.length; i++) {
					for (int j=0; j<thisMatrix.length; j++) {
						if (!thisMatrix[i][j].equals(game.getCardsOnBoard()[i][j])) {
							moveAllowed = false;
						}
					}

				}
			}
		};
        
        onPaneClick = event -> {

			Position rc = strToID(((Pane)event.getSource()).getId());

			if(event.getButton().equals(MouseButton.PRIMARY) && event.isShiftDown() && moveAllowed){

				//Clock Wise Rotations
				rotationMove = true;
				history(game.getCardsOnBoard()[rc.getRow()][rc.getColumn()].getCardMatrix(), rc.getRow(), rc.getColumn());
				int[][] rotMatrix = game.getCardsOnBoard()[rc.getRow()][rc.getColumn()].getCardMatrix();
				rotMatrix = CardsController.clockWiseRotation(rotMatrix);
				game.getCardsOnBoard()[rc.getRow()][rc.getColumn()].setCardMatrix(rotMatrix);
				drawEverything();

}else if (event.getButton().equals(MouseButton.PRIMARY) && moveAllowed && !rotationMove){

Position startPosition = game.getTurnsOrder().whosPlaying().getPosition();
int rStart = startPosition.getRow();
int cStart = startPosition.getColumn();
				int rEnd = rc.getRow();
				int cEnd = rc.getColumn();

				String direction = getDirection(rStart, cStart, rEnd, cEnd);

				if (!direction.equals("")) {

					Card startCard = game.getCardsOnBoard()[rStart][cStart];
					Card endCard = game.getCardsOnBoard()[rEnd][cEnd];
					int elStart;
					int elEnd;

					switch (direction) {
					case "up":
						elStart = startCard.getCardMatrix()[0][1];
						elEnd = endCard.getCardMatrix()[2][1];
						if (elStart == 0 && elEnd == 0) {
							try {
								movePlayer(rStart, cStart, rEnd, cEnd);
								moveAllowed = false;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					case "down":
						elStart = startCard.getCardMatrix()[2][1];
						elEnd = endCard.getCardMatrix()[0][1];
						if (elStart == 0 && elEnd == 0) {
							try {
								movePlayer(rStart, cStart, rEnd, cEnd);
								moveAllowed = false;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					case "left":
						elStart = startCard.getCardMatrix()[1][0];
						elEnd = endCard.getCardMatrix()[1][2];
						if (elStart == 0 && elEnd == 0) {
							try {
								movePlayer(rStart, cStart, rEnd, cEnd);
								moveAllowed = false;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					case "right":
						elStart = startCard.getCardMatrix()[1][2];
						elEnd = endCard.getCardMatrix()[1][0];
						if (elStart == 0 && elEnd == 0) {
							try {
								movePlayer(rStart, cStart, rEnd, cEnd);
								moveAllowed = false;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					}
				}

}

		};
    	
    }
    
    private String getDirection(int rStart, int cStart, int rEnd, int cEnd) {
    	
    	String direction = "";
    	if(rEnd == rStart+1 && cEnd == cStart) {
    		direction = "down";
    	}
    	
    	if (rEnd == rStart-1 && cEnd == cStart) {
    		direction = "up";
    	}
    	
    	if (rEnd == rStart && cEnd == cStart+1) {
    		direction = "right";
    	}
    	if (rEnd == rStart && cEnd == cStart-1) {
    		direction = "left";
    	}
     		
    	return direction;
    }
    
    private void movePlayer(int rStart, int cStart, int rEnd, int cEnd) throws IOException {

    	OnCard onCardStart = game.getCardsOnBoard()[rStart][cStart].getOnCard();
    	onCardEnd = game.getCardsOnBoard()[rEnd][cEnd].getOnCard();
    	Player thisPlayer = game.getTurnsOrder().whosPlaying();
		destination = game.getCardsOnBoard()[rEnd][cEnd];

		//if the move happens: set new player position and update the cards: card left by the player
		//and card entered by the player
		if (onCardEnd != OnCard.PLAYER && onCardEnd != OnCard.PLAYER_AND_GEM && onCardEnd != OnCard.PLAYER_AND_STAIRS) {

			thisPlayer.setPosition(new Position(rEnd, cEnd));

			switch (onCardStart) {
				case PLAYER_AND_STAIRS:
					game.getCardsOnBoard()[rStart][cStart].setOnCard(OnCard.STAIRS);
					break;
				case PLAYER_AND_GEM:
					game.getCardsOnBoard()[rStart][cStart].setOnCard(OnCard.GEM);
					break;
				default:
					game.getCardsOnBoard()[rStart][cStart].setOnCard(OnCard.NOTHING);
			}

			switch (onCardEnd) {
				case STAIRS:
					if (isGameWon()) {
						btnEndTurn.setDisable(true);
						victory();
					}else {
						game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER_AND_STAIRS);
					}
					break;
				case GEM:
					if (findGemByPosition(rEnd, cEnd).getPlayerColor() == game.getTurnsOrder().whosPlaying().getPlayerColor()) {
						collectGem(findGemByPosition(rEnd, cEnd));
						game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER);
					}else {
						game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER_AND_GEM);
					}
					break;
				case DEBUFFER:
				case BUFFER:
					showPopup(new Position(rEnd, cEnd), onCardEnd);
					game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER);
					break;
				case NOTHING:
					game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER);
					break;
			}
			drawEverything();
		}
    }

	public Gem findGemByPosition(int row, int col) {
		Gem thisGem = null;
		for (int i = 0; i<game.getGems().size(); i++) {
			if (game.getGems().get(i).getPosition().getRow() == row &&
					game.getGems().get(i).getPosition().getColumn() == col) {
				thisGem = game.getGems().get(i);
			}
		}
		return thisGem;
	}

	public void collectGem(Gem gem) {
		Player thisPlayer = game.getTurnsOrder().whosPlaying();
		if (thisPlayer.getInventory() == null) {
			thisPlayer.setInventory(new Inventory());
		}
		thisPlayer.getInventory().setGemsCollected(thisPlayer.getInventory().getGemsCollected() + 1);
		lblGems.setText(""+thisPlayer.getInventory().getGemsCollected());
		game.getGems().remove(gem);
		drawEverything();
		game.getStatus().getStatusList().add(game.getTurnsOrder().whosPlaying().getName() + " collected a gem");
		if (isGameWon() && !stairsSpawned) {
			spawnStairs();
			stairsSpawned = true;
		}
		labelStatus.setText(game.getStatus().getStatusList().get(game.getStatus().getStatusList().size()-1));

	}

	public void collectBuffDebuff() {
		Player thisPlayer = game.getTurnsOrder().whosPlaying();
		if (thisPlayer.getInventory() == null) {
			thisPlayer.setInventory(new Inventory());
		}
		if (onCardEnd == OnCard.BUFFER) {
			thisPlayer.getInventory().setBufferCollected(thisPlayer.getInventory().getBufferCollected() + 1);
			lblBuff.setText(""+thisPlayer.getInventory().getBufferCollected());
			game.getStatus().getStatusList().add(game.getTurnsOrder().whosPlaying().getName() + " collected a potion");
		}else if (onCardEnd == OnCard.DEBUFFER) {
			thisPlayer.getInventory().setDebufferCollected(thisPlayer.getInventory().getDebufferCollected() + 1);
			lblDebuff.setText(""+thisPlayer.getInventory().getDebufferCollected());
			game.getStatus().getStatusList().add(game.getTurnsOrder().whosPlaying().getName() + " collected a poison");

		}
		labelStatus.setText(game.getStatus().getStatusList().get(game.getStatus().getStatusList().size()-1));
	}

	public void history(int[][] matrix, int r, int c) {

		boolean newPosition = true;
		for (Position position : historyPosition) {
			if (position.getRow() == r && position.getColumn() == c) {
				newPosition = false;
				break;
			}
		}
		if(newPosition) {
			if (historyPosition.size() > 0) {
				int previousRow = historyPosition.get(historyPosition.size()-1).getRow();
				int previousCol = historyPosition.get(historyPosition.size()-1).getColumn();
				int[][] previousMatrix = historyMatrix.get(historyMatrix.size()-1);
				game.getCardsOnBoard()[previousRow][previousCol].setCardMatrix(previousMatrix);
			}
			historyPosition.add(new Position(r,c));
			int[][] thisMatrix = new int[matrix.length][];
			for (int i = 0; i < matrix.length; i++)
				thisMatrix[i] = matrix[i].clone();

			historyMatrix.add(thisMatrix);
		}


	}

	public boolean isGameWon() {

		int counter = 0;

		for (Gem gem: game.getGems()) {
			if (gem.getPlayerColor() == game.getTurnsOrder().whosPlaying().getPlayerColor()) {
				counter +=1;
			}
		}

		if(counter == 0) {
			game.getStatus().getStatusList().add(game.getTurnsOrder().whosPlaying().getName() + " collected all gems!");
			return true;
		}
		return false;
	}

	public void spawnStairs() {
		//stairs have to appear

		Image stairsImg = new Image("/Assets/exit.png");
		ImageView stairsIV = new ImageView(stairsImg);
		stairsIV.setFitHeight(tileDimension);
		stairsIV.setFitWidth(tileDimension);

		if (!stairsSpawned) {

			ArrayList<Card> cardsAvailable = new ArrayList<>();
			for (int i = 0; i<game.getCardsOnBoard().length; i++) {
				for (int j = 0; j<game.getCardsOnBoard().length; j++) {
					if (game.getCardsOnBoard()[i][j].getOnCard() == OnCard.NOTHING) {
						cardsAvailable.add(game.getCardsOnBoard()[i][j]);
					}
				}
			}
			Random rand = new Random();
			int randomIndex = rand.nextInt(cardsAvailable.size());
			Card randomCard = cardsAvailable.get(randomIndex);
			stairsPosition = cardsAvailable.get(randomIndex).getPosition();
			game.getCardsOnBoard()[randomCard.getPosition().getRow()]
					[randomCard.getPosition().getColumn()].setOnCard(OnCard.STAIRS);

			sPanes[randomCard.getPosition().getRow()][randomCard.getPosition().getColumn()].getChildren().add(stairsIV);
			StackPane.setAlignment(stairsIV, Pos.CENTER);

		} else {

			sPanes[stairsPosition.getRow()][stairsPosition.getColumn()].getChildren().add(stairsIV);
			StackPane.setAlignment(stairsIV, Pos.CENTER);
		}


	}

	private void victory() {

		Stage winStage = new Stage();
		//winStage.initModality(Modality.APPLICATION_MODAL);
		winStage.setTitle("WINNER");

		FXMLLoader winnerLoader = new FXMLLoader(getClass().getResource("/View/youWin.fxml"));
		WinController winController = new WinController();
		winnerLoader.setController(winController);

		try {
			winnerLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Parent root = winnerLoader.getRoot();
		Scene scene = new Scene(root, 349, 192);
		winStage.setScene(scene);
		winStage.setResizable(false);
		winStage.show();
	}

	public Position strToID(String strID) {

		strID = strID.replaceAll("\\D+"," ");
		String[] splited = strID.split("\\s+");
		String rStr = splited[1];
		String cStr = splited[2];
		int r = Integer.parseInt(rStr);
		int c = Integer.parseInt(cStr);

		return new Position(r,c);
	}

}
