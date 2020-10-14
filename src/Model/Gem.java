package Model;

import java.awt.*;

public class Gem {
    private Color color;
    private Position position;

    public Gem(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }
}
