package application;

import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.GemsLabel;
import view.PotionsLabel;
import view.StatusLabel;

public class App extends BorderPane {

	private BorderPane borderPane;
	private VBox top;
	private HBox inventory;
	private Image gemsImg;
	private ImageView gemsIc;
	private Image potionsImg;
	private ImageView potionsIc;
	private GridPane board;
	private GridPane card;
	private HBox bottomControlls;
	private Button endTurn;
	private Label inventoryL;
	
	public App() throws FileNotFoundException {
		GemsLabel myGemsLabel;
		PotionsLabel myPotionsLabel;
		StatusLabel myStatusLabel;
		borderPane = new BorderPane();
		
		//initialize views
		myGemsLabel = new GemsLabel();
		myPotionsLabel = new PotionsLabel();
		myStatusLabel = new StatusLabel();
		
		initGUI(borderPane);
	}

	private void initGUI(BorderPane root) throws FileNotFoundException {
		top = new VBox();
		top.setSpacing(10);
		
		inventory = new HBox();
		inventory.setSpacing(20);
		
		inventoryL = new Label("Inventory"); 
		
		gemsImg = new Image(new FileInputStream("C:\\Users\\jbies\\eclipse-workspace\\A-maze v1\\img\\gem_icon.png"));
		potionsImg = new Image(new FileInputStream("C:\\Users\\jbies\\eclipse-workspace\\A-maze v1\\img\\potion_icon.png"));
		gemsIc = new ImageView(gemsImg);
		potionsIc = new ImageView(potionsImg);
		
		inventory.getChildren().addAll(gemsIc, potionsIc);
		top.getChildren().addAll(inventoryL, inventory);
		
		board = new GridPane();
		card = new GridPane();
		
		for (int i = 0; i<7; i++) {
			for (int j = 0; j<7; j++) {
				GridPane gridPane = new GridPane();
				for (int x = 0; x<3; x++) {
					for (int z = 0; z<3; z++) {
						Pane pane = new Pane();
						gridPane.add(pane, x, z);
					}
				}
				board.add(gridPane, i, j);
			}
		}
		
		bottomControlls = new HBox();
		endTurn = new Button("End turn");
		bottomControlls.getChildren().add(endTurn);
		
		root.setTop(top);
		root.setCenter(board);
		root.setBottom(bottomControlls);
	}
}
