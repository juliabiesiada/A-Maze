package Controller;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CollectController {
	
	@FXML
    private ImageView iv1;

    @FXML
    private ImageView iv2;

    @FXML
    private ImageView iv3;

    @FXML
    private ImageView iv4;

    @FXML
    private Label lblTimer;
    
    Image arrowUp;
    Image arrowDown;
    Image arrowRight;
    Image arrowLeft;
    
    List<String> keyOrder;
    List<Image> imgOrder;
    List<Integer> intOrder;
    Random rand;
    		
	public void initialize() {
		
		arrowUp = new Image("Assets/arrowUp.png");
		arrowRight = new Image("Assets/arrowRight.png");
		arrowDown = new Image("Assets/arrowDown.png");
		arrowLeft = new Image("Assets/arrowLeft.png");
		
		keyOrder = new ArrayList<String>();
		imgOrder = new ArrayList<Image>();
		intOrder = new ArrayList<Integer>();

    }
	
	public void randomize() {
		
		rand = new Random();
		int r;
		boolean error;
		
		//1 UP 2 RIGHT 3 DOWN 4 LEFT
		while(keyOrder.size() != 4) {
			
			error = false;
			r = rand.nextInt(5);
			for (int i = 0; i<intOrder.size(); i++) {
				
			}
			if (r != 0 && !error) {
				
			}
			
		}
		
	}
	
}
