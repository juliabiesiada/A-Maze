package Model;

import java.awt.*;

public class Player {
    private Color color;
    private Position position;
    private String name;
    private Inventory inventory;

    public Player(Color color, Position position, String name, Inventory inventory) {
        this.color = color;
        this.position = position;
        this.name = name;
        this.inventory = inventory;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
