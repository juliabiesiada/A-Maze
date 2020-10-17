package Model;

public class Gem {
    private PlayerColor playerColor;
    private Position position;

    public Gem(PlayerColor playerColor, Position position) {
        this.playerColor = playerColor;
        this.position = position;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public Position getPosition() {
        return position;
    }
}
