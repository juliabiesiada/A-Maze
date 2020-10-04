package Model;

import java.awt.*;

public class Gem {
    private Color color;
    private int[][] position;

    public Gem(Color color, int[][] position) {
        this.color = color;
        this.position = position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPosition(int[][] position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public int[][] getPosition() {
        return position;
    }
}
