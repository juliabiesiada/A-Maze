package Controller;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Controller {
	
	@FXML
	private GridPane board_grid;
	@FXML
	private List<Pane> panes;
	
	public void onClicked(MouseEvent mouseEvent) {
		//code for ending turn
		System.out.println("test");
	}
	
	public void initialize() {
		panes = new ArrayList<Pane>();
		for (int i = 0; i<7; i++) {
			for (int j = 0; j<7; j++) {
				Pane pane = new Pane();
				pane.setId("p"+i+j);
				panes.add(pane);
				board_grid.add(pane, i, j);
			}
		}
	}
}
