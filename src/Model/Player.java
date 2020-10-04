package Model;

import java.awt.*;

public class Player {
    private Color color;
    private int[][] position;
    private String name;
    private Inventory inventory;
    //does he have an icon?


    public Player(Color color, int[][] position, String name, Inventory inventory) {
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

    public int[][] getPosition() {
        return position;
    }

    public void setPosition(int[][] position) {
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
