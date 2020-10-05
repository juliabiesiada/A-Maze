package Controller;


import java.util.ArrayList;
import java.util.List;

import Model.Card;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
	
	public void onClicked(MouseEvent mouseEvent) {
		//code for ending turn
		System.out.println("test");
	}
	
	public void initialize() {
		
		
		//this method actually print the first card but only if the 3x3 grid has an id.
		/*for (int k = 0; k<1; k++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					Pane pane = new Pane();
					if (cards[k].getCardMatrix()[i][j] == 0) {
						pane.setBackground(new Background(
								new BackgroundFill(Paint.valueOf("white"), null, null)));
					} else {
						pane.setBackground(new Background(
								new BackgroundFill(Paint.valueOf("brown"), null, null)));
					}
					littleGrid.add(pane, j, i);
				}
			}
		}*/

		/*
		for (int k = 0; k<1; k++) {
			GridPane prova = new GridPane();
			prova.setPrefSize(60,60);
			prova.setMaxSize(60,60);
			prova.setMinSize(60,60);
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					Pane pane = new Pane();
					if (cards[k].getCardMatrix()[i][j] == 0) {
						pane.setBackground(new Background(
								new BackgroundFill(Paint.valueOf("white"), null, null)));
					} else {
						pane.setBackground(new Background(
								new BackgroundFill(Paint.valueOf("brown"), null, null)));
					}
					prova.addRow(i, new Text("    "));
					prova.add(pane, j, i);
					prova.setGridLinesVisible(true);
				}
			}
			board_grid.add(prova, 0,0);
			System.out.println(board_grid.getChildren().get(0).getId());
		}*/

		

		/*
		//this create a grid 3x3 inside each pane
		for (int k = 0; k<panes.size(); k++) {
			System.out.println(panes.get(k).getId());
			//GridPane gridCard = new GridPane();
			//panes.get(k).getChildren().add(gridCard);
			//gridCard.toFront();

			for (int i = 0; i<3; i++) {
				for (int j = 0; j<3; j++) {
					Pane pane = new Pane();
					if (cards[k].getCardMatrix()[i][j] == 0) {
						pane.setBackground(new Background(
								new BackgroundFill(Paint.valueOf("white"), null, null)));
					} else {
						pane.setBackground(new Background(
								new BackgroundFill(Paint.valueOf("brown"), null, null)));
					}
					gridCard.add(pane, j, i);
					pane.toFront();
				}
			}

		}*/

	}
	
	public void createBoard(int levelSize) {
		
		CardsController cardsController = new CardsController(levelSize);
		Card[] cards = cardsController.getCards();
		
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
				
				
				for (int k = 0; k<panes.size(); k++) {
					GridPane tinyGrid = new GridPane();
					//tinyGrid.setPrefSize(50,50);
					//tinyGrid.setMaxSize(60,60);
					//tinyGrid.setMinSize(60,60);
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							Pane pane = new Pane();
							if (cards[k].getCardMatrix()[i][j] == 0) {
								pane.setBackground(new Background(
										new BackgroundFill(Paint.valueOf("white"), null, null)));
							} else {
								pane.setBackground(new Background(
										new BackgroundFill(Paint.valueOf("brown"), null, null)));
							}
							//ask in class!!!!
							tinyGrid.addRow(i, new Text("     "));
							tinyGrid.add(pane, j, i);
						}
					}
					panes.get(k).getChildren().add(tinyGrid);
				}
				
				board_grid.setMaxHeight(494);
				board_grid.setPrefHeight(494);
				board_grid.setMinHeight(494);
				board_grid.setMinWidth(550);
				board_grid.setPadding(new Insets(0,0,0,30));
	}
}
