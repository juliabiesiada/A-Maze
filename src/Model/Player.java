package Model;

import java.awt.*;

public class Player {
    private PlayerColor playerColor;
    private Position position;
    private String name;
    private Inventory inventory;
    private String iconURL;

    public Player(PlayerColor playerColor, Position position, String name, Inventory inventory, String iconURL) {
        this.playerColor = playerColor;
        this.position = position;
        this.name = name;
        this.inventory = inventory;
        this.iconURL = iconURL;
    }
    
    public Player() {
    	super();
    }

    public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
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
