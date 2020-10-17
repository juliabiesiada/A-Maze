package Controller;


import java.io.IOException;
import java.util.List;

import Model.*;
import Model.Card;
import Model.Game;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

	//for level choosing
	int size;
	int tileDimension;
	int levelSize;
	int numOfGems;

	public void onClicked(MouseEvent mouseEvent) throws IOException {
		showPopup();
	}
	
	public void initialize() {
	}

	public void createBoard(Game game) {
		
		levelSize = checkLevel(game);
		setVariables(game);
		
		CardsController cardsController = new CardsController(levelSize);
		Card[][] cards = cardsController.getCards();
		game.setCardsOnBoard(cards);

		//root is pane, contains a grid, allows us to be flexible with the size
		GridPane board_grid = new GridPane();
		board_root.getChildren().add(board_grid);

		//this creates a pane inside each cell of the grid
		StackPane[][] sPanes = new StackPane[levelSize][levelSize];
		for (int i = 0; i < levelSize; i++) {
			for (int j = 0; j < levelSize; j++) {
				StackPane sPane = new StackPane();
				sPane.setId("r"+i+"c"+j);
				sPanes[i][j] = sPane;
				board_grid.add(sPane, j, i);
				//sPane.setPadding(new Insets(1,1,1,1));
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

		//this put diamons on the board
		for (Player player : game.getPlayers()) {
			String urlGem = new String();
            if (player.getPlayerColor() == PlayerColor.BLUE)
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
}
