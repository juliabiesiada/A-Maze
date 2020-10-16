package Controller;


import java.util.ArrayList;
import java.util.List;

import Model.Card;
import Model.Game;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class Controller {
	
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
	
	public void onClicked(MouseEvent mouseEvent) {
		//code for ending turn
		System.out.println("test");
	}
	
	public void initialize() {

	}

	public void createBoard(Game game) {
		
		levelSize = checkLevel(game);
		setTileDimension(game);
		
		CardsController cardsController = new CardsController(levelSize);
		Card[][] cards = cardsController.getCards();
		
		//root is pane, contains a grid, allows us to be flexible with the size
		GridPane board_grid = new GridPane();
		board_root.getChildren().add(board_grid);

		//this creates a pane inside each cell of the grid
				panes = new ArrayList<Pane>();
				for (int i = 0; i < levelSize; i++) {
					for (int j = 0; j < levelSize; j++) {
						Pane pane = new Pane();
						pane.setId("r"+i+"c"+j);
						panes.add(pane);
						board_grid.add(pane, j, i);
						pane.setPadding(new Insets(1,1,1,1));
					}
				}
				
				//this colors the cards
				int k = 0;
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
						panes.get(k).getChildren().add(tinyGrid);
						k++;
					}
				}
				
				board_grid.setMaxHeight(494);
				board_grid.setPrefHeight(494);
				board_grid.setMinHeight(494);
				board_grid.setMinWidth(550);
				board_grid.setPadding(new Insets(0,0,0,30));

	}

	private void setTileDimension(Game game) {

	switch (game.getLevel()) {
		case EASY: tileDimension = 25;
		break;
		case MEDIUM: tileDimension = 20;
		break;
		case HARD: tileDimension = 15;
		break;
	}
	}

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
}
