package Model;

import java.util.ArrayList;

public class Game {
    private Player[] players;
    private Card[][] cardsOnBoard;
    private Turn[] turns;
    private Level level;
    private ArrayList<Gem> gems;

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Card[][] getCardsOnBoard() {
        return cardsOnBoard;
    }

    public void setCardsOnBoard(Card[][] cardsOnBoard) {
        this.cardsOnBoard = cardsOnBoard;
    }

    public Turn[] getTurns() {
        return turns;
    }

    public void setTurns(Turn[] turns) {
        this.turns = turns;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
