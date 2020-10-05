package Controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Model.Card;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class Controller {
	
	@FXML
	private GridPane board_grid;
	@FXML
	private List<Pane> panes;
	@FXML
	private GridPane littleGrid;
	
	public void onClicked(MouseEvent mouseEvent) {
		//code for ending turn
		System.out.println("test");
	}
	
	public void initialize() {

		CardsController cardsController = new CardsController(7);
		Card[] cards = cardsController.getCards();

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

		//this creates a pane inside each cell of the grid
		panes = new ArrayList<Pane>();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Pane pane = new Pane();
				pane.setId("r"+i+"c"+j);
				panes.add(pane);
				board_grid.add(pane, j, i);
			}
		}

		for (int k = 0; k<panes.size(); k++) {
			GridPane tinyGrid = new GridPane();
			tinyGrid.setPrefSize(60,60);
			tinyGrid.setMaxSize(60,60);
			tinyGrid.setMinSize(60,60);
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
					tinyGrid.addRow(i, new Text("     "));
					tinyGrid.add(pane, j, i);
				}
			}
			panes.get(k).getChildren().add(tinyGrid);
		}

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
}
