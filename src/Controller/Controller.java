package Controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.Cursor;
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
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
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
	Game game;
	private EventHandler<MouseEvent> onPaneDragDetected;
	private EventHandler<DragEvent> onPaneDragDone;
    private EventHandler<DragEvent> onPaneDragOver;
    private EventHandler<MouseEvent> onPaneClick;
    Image playerImage;
    ImageView playerImageView;
    StackPane[][] sPanes;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void onClicked(MouseEvent mouseEvent) throws IOException {
		showPopup();
	}
	
	public void initialize() {
		initHandlers();
	}

	public void createBoard() {
		
		levelSize = checkLevel(game);
		setVariables(game);
		
		CardsController cardsController = new CardsController(levelSize);
		Card[][] cards = cardsController.getCards();
		game.setCardsOnBoard(cards);

		//root is pane, contains a grid, allows us to be flexible with the size
		board_grid = new GridPane();
		board_root.getChildren().add(board_grid);

		//this creates a pane inside each cell of the grid
		sPanes = new StackPane[levelSize][levelSize];
		for (int i = 0; i < levelSize; i++) {
			for (int j = 0; j < levelSize; j++) {
				StackPane sPane = new StackPane();
				sPane.setId("r"+i+"c"+j);
				sPane.setOnDragDetected(onPaneDragDetected);
				sPane.setOnDragOver(onPaneDragOver);
				sPane.setOnDragDropped(onPaneDragDone);
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
							Image wall = new Image("/Assets/wall.png");
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

		//this put players on the board
        setPlayerInitialPosition(game);
        for (Player player : game.getPlayers()) {
            playerImage = new Image(player.getIconURL());
            playerImageView = new ImageView(playerImage);
            playerImageView.setFitHeight(tileDimension);
            playerImageView.setFitWidth(tileDimension);
            sPanes[player.getPosition().getRow()][player.getPosition().getColumn()].getChildren().add(playerImageView);
            sPanes[player.getPosition().getRow()][player.getPosition().getColumn()].setAlignment(playerImageView, Pos.CENTER);
            game.getCardsOnBoard()[player.getPosition().getRow()][player.getPosition().getColumn()].setOnCard(OnCard.PLAYER);
        }

		//this put diamonds on the board
		for (Player player : game.getPlayers()) {
			String urlGem = new String();
            switch (player.getPlayerColor()) {
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
			for (int i = 0; i<numOfGems; i++) {
				int randomRow = new Randomizer().randomize(levelSize);
				int randomCol = new Randomizer().randomize(levelSize);
				if (game.getCardsOnBoard()[randomRow][randomCol].isAvailable()) {
					Image gemImage = new Image(urlGem);
					ImageView gemImageView = new ImageView(gemImage);
					gemImageView.setFitHeight(tileDimension);
					gemImageView.setFitWidth(tileDimension);
					game.getCardsOnBoard()[randomRow][randomCol].setOnCard(OnCard.GEM);
					game.getCardsOnBoard()[randomRow][randomCol].setAvailable(false);
					sPanes[randomRow][randomCol].getChildren().add(gemImageView);
					sPanes[randomRow][randomCol].setAlignment(gemImageView, Pos.CENTER);
					game.getGems().add(new Gem(player.getPlayerColor(), new Position(randomRow,randomCol)));
				} else {
					i--;
				}

			}
		}
		
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
		popupLoader.setController(collectController);

		popupLoader.load();


		Parent root = popupLoader.getRoot();

		Scene scene = new Scene(root, 400, 400);
		//kind of a stupid solution but well, couldn't get scene in initialize
		collectController.assignListeners();
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
    private void handleIconDragDropped(DragEvent event) {
		/*
		 * ADD EVENT CONSUME!
		 * Node node = event.getPickResult().getIntersectedNode(); Integer cIndex =
		 * board_grid.getColumnIndex(node); Integer rIndex =
		 * board_grid.getRowIndex(node); Dragboard db = event.getDragboard();
		 * 
		 * for (int i = 0; i<game.getPlayers().length; i++) {
		 * 
		 * if (game.getPlayers()[i].getPosition().getColumn() == cIndex &&
		 * game.getPlayers()[i].getPosition().getRow() == rIndex) {
		 * 
		 * Player selectedPlayer = game.getPlayers()[i]; Turn currentTurn =
		 * game.getTurns()[game.getTurns().length - 1]; if (db.getImage() ==
		 * iconBuff.getImage()) {
		 * 
		 * //add turn }else if (db.getImage() == iconDebuff.getImage()) {
		 * 
		 * if (currentTurn.getTurns().get(0) != selectedPlayer) { //skip turns } } } }
		 */
    }
    
    @FXML 
    private void  handleIconDragDetected(MouseEvent event) {
    	
    	String imgID = ((ImageView) event.getSource()).getId();
    	switch (imgID) {
    	case "iconBuff":
    		if (Integer.parseInt(lblBuff.getText()) > 0) {
    			Dragboard db = iconBuff.startDragAndDrop(TransferMode.ANY);
    	        ClipboardContent cb = new ClipboardContent();
    	        cb.putImage(iconBuff.getImage());
    	        db.setContent(cb);
    	        event.consume();
    		}
    		break;
    	case "iconDebuff":
    		if (Integer.parseInt(lblDebuff.getText()) > 0) {
    			Dragboard db = iconDebuff.startDragAndDrop(TransferMode.ANY);
    	        ClipboardContent cb = new ClipboardContent();
    	        cb.putImage(iconDebuff.getImage());
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
				String rStr = "" + paneID.charAt(1);
				String cStr = "" + paneID.charAt(3);
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
        
        onPaneDragDone = new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				
				Dragboard db = event.getDragboard();
				String rcStart = db.getString();
				
				String paneID = ((Pane)event.getSource()).getId();
				String rStr = "" + paneID.charAt(1);
				String cStr = "" + paneID.charAt(3);
				int r = Integer.parseInt(rStr);
				int c = Integer.parseInt(cStr);
				
				System.out.println(r+""+c+" finished from " + rcStart);
				
			}
        	
        };
        
        onPaneClick = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				if(event.getButton().equals(MouseButton.PRIMARY)){
		            if(event.getClickCount() == 2){
		            	
		            	String paneID = ((Pane)event.getSource()).getId();
						String rStr = "" + paneID.charAt(1);
						String cStr = "" + paneID.charAt(3);
						int r = Integer.parseInt(rStr);
						int c = Integer.parseInt(cStr);
		            	
		                System.out.println(r+""+c+" double clicked");
		            }else {
		            	
		            	Position startPosition = game.getPlayers()[0].getPosition();
		            	int rStart = startPosition.getRow();
		            	int cStart = startPosition.getColumn();
		            	
		            	String paneID = ((Pane)event.getSource()).getId();
						String rStr = "" + paneID.charAt(1);
						String cStr = "" + paneID.charAt(3);
						int rEnd = Integer.parseInt(rStr);
						int cEnd = Integer.parseInt(cStr);
						
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
									movePlayer(rStart, cStart, rEnd, cEnd);
								}
								break;
							case "down":
								elStart = startCard.getCardMatrix()[2][1];
								elEnd = endCard.getCardMatrix()[0][1];
								if (elStart == 0 && elEnd == 0) {
									movePlayer(rStart, cStart, rEnd, cEnd);
								}
								break;
							case "left":
								elStart = startCard.getCardMatrix()[1][0];
								elEnd = endCard.getCardMatrix()[1][2];
								if (elStart == 0 && elEnd == 0) {
									movePlayer(rStart, cStart, rEnd, cEnd);
								}
								break;
							case "right":
								elStart = startCard.getCardMatrix()[1][2];
								elEnd = endCard.getCardMatrix()[1][0];
								if (elStart == 0 && elEnd == 0) {
									movePlayer(rStart, cStart, rEnd, cEnd);
								}
								break;
							}
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
    
    private void movePlayer(int rStart, int cStart, int rEnd, int cEnd) {
    	
    	OnCard onCardStart = game.getCardsOnBoard()[rStart][cStart].getOnCard();
    	OnCard onCardEnd = game.getCardsOnBoard()[rEnd][cEnd].getOnCard();
    	
    	if(onCardEnd != OnCard.PLAYER) {
    		
    		game.getPlayers()[0].setPosition(new Position(rEnd, cEnd));
    		
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
   
            sPanes[rStart][cStart].getChildren().remove(playerImageView);
            sPanes[rEnd][cEnd].getChildren().add(playerImageView);
            sPanes[rEnd][cEnd].setAlignment(playerImageView, Pos.CENTER);
            game.getCardsOnBoard()[rEnd][cEnd].setAvailable(false);
            
            if(onCardEnd == OnCard.GEM) {
            	game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER_AND_GEM);
            } else {
                game.getCardsOnBoard()[rEnd][cEnd].setOnCard(OnCard.PLAYER);
            }
    	}
    }
}
