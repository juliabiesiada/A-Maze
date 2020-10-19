package Controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Model.*;
import Model.Card;
import Model.Game;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.WindowEvent;


public class Controller {

	private static final int NUM_OF_COLORS = 4;
	@FXML
	private Pane board_root;
	@FXML
	private List<Pane> panes;
	@FXML
	private GridPane littleGrid;
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

	GridPane board_grid;
	//for level choosing
	int size;
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
	List<int[][]> historyMatrix = new ArrayList<int[][]>();
	List<Position> historyPosition = new ArrayList<Position>();
	String imgID;
	Boolean rotationMove;
	Boolean stairsSpawned;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void onClicked(MouseEvent mouseEvent) throws IOException {
		//End Turn
		//allowing player to move
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

	}
	
	public void initialize() {
		initHandlers();
		//to allow only one movement per turn
		moveAllowed = true;
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

		spawnGems();
		drawEverything();

	}

	public void drawEverything() {
		drawBoard();
		spawnPlayers();
		drawGems();
		spawnBufferDebuffer();
	}

	public void drawBoard() {
		//root is pane, contains a grid, allows us to be flexible with the size
		board_grid = new GridPane();
		board_root.getChildren().add(board_grid);
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
							String url = new String();
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
		board_grid.setMaxHeight(494);
		board_grid.setPrefHeight(494);
		board_grid.setMinHeight(494);
		board_grid.setMinWidth(550);
		board_grid.setPadding(new Insets(0,0,0,30));
	}

	public void spawnPlayers() {

		//this put players on the board
		for (Player player : game.getPlayers()) {
			playerImage = new Image(player.getIconURL());
			playerImageView = new ImageView(playerImage);
			playerImageView.setFitHeight(tileDimension);
			playerImageView.setFitWidth(tileDimension);
			sPanes[player.getPosition().getRow()][player.getPosition().getColumn()].getChildren().add(playerImageView);
			sPanes[player.getPosition().getRow()][player.getPosition().getColumn()].setAlignment(playerImageView, Pos.CENTER);
			game.getCardsOnBoard()[player.getPosition().getRow()][player.getPosition().getColumn()].setOnCard(OnCard.PLAYER);
		}
	}

	public void spawnGems() {

		//this put diamonds on the board
		for (Player player : game.getPlayers()) {
			String urlGem = colorToGemURL(player.getPlayerColor());

			for (int i = 0; i<numOfGems; i++) {
				int randomRow = new Randomizer().randomize(levelSize);
				int randomCol = new Randomizer().randomize(levelSize);
				if (game.getCardsOnBoard()[randomRow][randomCol].getOnCard() == OnCard.NOTHING &&
						game.getCardsOnBoard()[randomRow][randomCol].isAvailable() == true) {
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
			sPanes[row][col].setAlignment(gemImageView, Pos.CENTER);
		}
	}

	public void spawnBufferDebuffer() {
		//handles buffer and debuffer
		int val = game.getTurnsOrder().getCounter() % 10;
		if (val == 0 && buffDebuffAlreadySpawn == false) {
			game.updateBuffDebuff();
			buffDebuffAlreadySpawn = true;
		}
		for (int i = 0; i<game.getBuffPositions().size(); i++) {
			Image buffImage = new Image("/Assets/potion_icon.png");
			ImageView buffImageView = new ImageView(buffImage);
			buffImageView.setFitHeight(tileDimension);
			buffImageView.setFitWidth(tileDimension);
			sPanes[game.getBuffPositions().get(i).getRow()][game.getBuffPositions().get(i).getColumn()].getChildren().add(buffImageView);
			sPanes[game.getBuffPositions().get(i).getRow()][game.getBuffPositions().get(i).getColumn()].setAlignment(buffImageView, Pos.CENTER);
		}
		for (int i = 0; i<game.getDebuffPositions().size(); i++) {
			Image debuffImage = new Image("/Assets/poison_icon.png");
			ImageView debuffImageView = new ImageView(debuffImage);
			debuffImageView.setFitHeight(tileDimension);
			debuffImageView.setFitWidth(tileDimension);
			sPanes[game.getDebuffPositions().get(i).getRow()][game.getDebuffPositions().get(i).getColumn()].getChildren().add(debuffImageView);
			sPanes[game.getDebuffPositions().get(i).getRow()][game.getDebuffPositions().get(i).getColumn()].setAlignment(debuffImageView, Pos.CENTER);
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
		case EASY: tileDimension = 25;
			numOfGems = 4;
		break;
		case MEDIUM: tileDimension = 20;
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

    private void showPopup() throws IOException {

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

		Scene scene = new Scene(root, 400, 400);
		//kind of a stupid solution but well, couldn't get scene in initialize
		collectController.assignListeners(game, this);
		scene.getStylesheets().add("View/application.css");
		popup.setScene(scene);
		popup.show();

		//to stop user from closing the window
		Platform.setImplicitExit(false);
		popup.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        event.consume();
		    }
		});

		popup.setHeight(200);
		popup.setWidth(400);

		popup.setMinHeight(200);
		popup.setMinWidth(400);

	}
    
    @FXML
    private void handleIconDragOver(DragEvent event) {
    	event.acceptTransferModes(TransferMode.ANY);
    	event.consume();
    }

    @FXML
	void easterEggMagic(MouseEvent event) {
		System.out.println("magic");
		for (Player player : game.getPlayers()) {
			if (player.getName() == "Raphael" || player.getName() == "raphael" || player.getName() == "Raphaël") {
				player.setIconURL("/Assets/umbreon.gif");
			}
			else if (player.getName() == "Mehdi" || player.getName() == "mehdi") {
				player.setIconURL("/Assets/cyndaquil.gif");
			}
		}
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
    	onPaneDragDetected = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				//need to check to what the constraints are applied in slider method
				String paneID = ((Pane)event.getSource()).getId();
            	paneID = paneID.replaceAll("\\D+"," ");
            	String[] splited = paneID.split("\\s+");
				String rStr = splited[1];
				String cStr = splited[2];
				int r = Integer.parseInt(rStr);
				int c = Integer.parseInt(cStr);
				
				Dragboard db = ((Pane)event.getSource()).startDragAndDrop(TransferMode.ANY);
    	        ClipboardContent cb = new ClipboardContent();
    	        cb.putString(rStr + cStr);
    	        db.setContent(cb);
				
				System.out.println(r+""+c+" start drag");
				event.consume();
			}
    		
    	};
    	
        onPaneDragOver = new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);
				event.consume();
			}
        	
        };
        
        onDragDropped = new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				
				Dragboard db = event.getDragboard();
				
				String paneID = ((Pane)event.getSource()).getId();

				if (db.hasImage()) {

					Node node = (Node)event.getSource();
					Integer cIndex = board_grid.getColumnIndex(node);
					Integer rIndex = board_grid.getRowIndex(node);
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
						} else if (imgID.equals(iconDebuff.getId())) {
							if (!selectedPlayer.equals(game.getTurnsOrder().whosPlaying())) {
								game.getTurnsOrder().debufferReceived(selectedPlayer);
								game.getTurnsOrder().whosPlaying().getInventory().setDebufferCollected
										(game.getTurnsOrder().whosPlaying().getInventory().getDebufferCollected()-1);
								lblDebuff.setText(""+game.getTurnsOrder().whosPlaying().getInventory().getDebufferCollected());
							}
						}
					}

					event.consume();
				} else {

					String rcStart = db.getString();
					int rStart = Integer.parseInt(""+rcStart.charAt(0));
					int cStart = Integer.parseInt(""+rcStart.charAt(1));

					paneID = paneID.replaceAll("\\D+"," ");
					String[] splited = paneID.split("\\s+");
					String rStr = splited[1];
					String cStr = splited[2];
					int rEnd = Integer.parseInt(rStr);
					int cEnd = Integer.parseInt(cStr);

					Position startPos = new Position(rStart, cStart);
					Position endPos = new Position(rEnd, cEnd);

					Card[][] slideMatrix = game.getCardsOnBoard();
					slideMatrix = CardsController.cardsSlider(slideMatrix, startPos, endPos, slideMatrix.length);
					game.setCardsOnBoard(slideMatrix);

					drawEverything();
				}
			}
        	
        };
        
        onPaneClick = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				String paneID = ((Pane)event.getSource()).getId();
            	paneID = paneID.replaceAll("\\D+"," ");
            	String[] splited = paneID.split("\\s+");
				String rStr = splited[1];
				String cStr = splited[2];
				int r = Integer.parseInt(rStr);
				int c = Integer.parseInt(cStr);
				
				if(event.getButton().equals(MouseButton.PRIMARY) && event.isShiftDown() && moveAllowed){

					//Clock Wise Rotationssss
					//make history
					rotationMove = true;
					history(game.getCardsOnBoard()[r][c].getCardMatrix(), r, c);
					int[][] rotMatrix = game.getCardsOnBoard()[r][c].getCardMatrix();
					rotMatrix = CardsController.clockWiseRotation(rotMatrix);
					game.getCardsOnBoard()[r][c].setCardMatrix(rotMatrix);
					drawEverything();
	                
	            }else if (event.getButton().equals(MouseButton.PRIMARY) && moveAllowed && !rotationMove){
		            	
	            	Position startPosition = game.getTurnsOrder().whosPlaying().getPosition();
	            	int rStart = startPosition.getRow();
	            	int cStart = startPosition.getColumn();
					int rEnd = r;
					int cEnd = c;
					
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

    	
    	if(onCardEnd != OnCard.PLAYER) {
    		
    		thisPlayer.setPosition(new Position(rEnd, cEnd));
    		
            ObservableList<Node> paneContent = sPanes[rStart][cStart].getChildren();
            Node playerImageView;
            //see later with collecting of gems if it disappears first
            if (onCardStart == OnCard.PLAYER_AND_GEM) {
            	playerImageView = paneContent.get(2);
            	game.getCardsOnBoard()[rStart][cStart].setOnCard(OnCard.GEM);
            }else {
            	playerImageView = paneContent.get(1);
            	game.getCardsOnBoard()[rStart][cStart].setOnCard(OnCard.NOTHING);
            }
            
            if(onCardEnd == OnCard.GEM) {
            	if (findGemByPosition(rEnd, cEnd).getPlayerColor() == game.getTurnsOrder().whosPlaying().getPlayerColor()) {
					collectGem(findGemByPosition(rEnd, cEnd));
				}else {
					game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER_AND_GEM);
				}
            } else if (onCardEnd == OnCard.BUFFER || onCardEnd == OnCard.DEBUFFER){
                showPopup();
            }else {
				game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER);
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
		game.getCardsOnBoard()[thisPlayer.getPosition().getRow()][thisPlayer.getPosition().getColumn()].setOnCard(OnCard.PLAYER);

		if (isGameWon()) {
			spawnStairs();
		}

	}

	public void collectBuffDebuff() {
		Player thisPlayer = game.getTurnsOrder().whosPlaying();
		if (thisPlayer.getInventory() == null) {
			thisPlayer.setInventory(new Inventory());
		}
		if (onCardEnd == OnCard.BUFFER) {
			thisPlayer.getInventory().setBufferCollected(thisPlayer.getInventory().getBufferCollected() + 1);
			lblBuff.setText(""+thisPlayer.getInventory().getBufferCollected());
		}else if (onCardEnd == OnCard.DEBUFFER) {
			thisPlayer.getInventory().setDebufferCollected(thisPlayer.getInventory().getDebufferCollected() + 1);
			lblDebuff.setText(""+thisPlayer.getInventory().getDebufferCollected());
		}

		game.removeBuffDebuff(destination.getPosition(), onCardEnd);
	}

	public void history(int[][] matrix, int r, int c) {

		boolean newPosition = true;
		for (int i = 0; i<historyPosition.size(); i++) {
			if (historyPosition.get(i).getRow() == r && historyPosition.get(i).getColumn() == c) {
				newPosition = false;
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
			return true;
		}
		return false;
	}

	public void spawnStairs() {
		//stairs have to appear
		ArrayList<Card> cardsAvailable = new ArrayList<Card>();
		for (int i = 0; i<game.getCardsOnBoard().length; i++) {
			for (int j = 0; j<game.getCardsOnBoard().length; j++) {
				if (game.getCardsOnBoard()[i][j].getOnCard() == OnCard.NOTHING) {
					cardsAvailable.add(game.getCardsOnBoard()[i][j]);
				}
			}
		}
		Random rand = new Random();
		int randomIndex = rand.nextInt(cardsAvailable.size());
		game.getCardsOnBoard()[cardsAvailable.get(randomIndex).getPosition().getRow()]
				[cardsAvailable.get(randomIndex).getPosition().getColumn()].setOnCard(OnCard.STAIRS);
		//add imageView with stairs
		//cardsAvailable.get(randomIndex).add
		stairsSpawned = true;
	}

}
