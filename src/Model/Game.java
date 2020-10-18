package Model;

import java.util.ArrayList;

public class Game {
    private Player[] players;
    private Card[][] cardsOnBoard;
    private TurnManager turnsOrder;
    private Level level;
    private ArrayList<Gem> gems;
    private Status status;

    public Game() {
        this.gems = new ArrayList<Gem>();
        this.status = new Status();
    }

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

    public ArrayList<Gem> getGems() {
        return gems;
    }

    public void setTurnsOrders(TurnManager turns) {
        this.turnsOrder = turns;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
